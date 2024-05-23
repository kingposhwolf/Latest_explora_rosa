package com.example.demo.Services.SearchService;

import java.time.Duration;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Components.Algorithms.SearchAlgorithm;
import com.example.demo.Components.Helper.Helper;
import com.example.demo.InputDto.SearchDto.SearchDto;
import com.example.demo.InputDto.SearchDto.SearchHashTagDto;
import com.example.demo.Repositories.SearchOperation.UserSearchHistoryRepository;
import com.example.demo.Repositories.SocialMedia.Content.UserPostRepository;
import com.example.demo.Repositories.SocialMedia.HashTag.HashTagRepository;
import com.example.demo.Repositories.UserManagement.AccountManagement.ProfileRepository;
import com.example.demo.Services.RedisService.RedisService;
import com.example.demo.TempDto.PageDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SearchServiceImpl implements SearchService{
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final ProfileRepository profileRepository;

    private AmqpTemplate rabbitTemplate;

    private final SearchAlgorithm searchAlgorithm;

    private final UserSearchHistoryRepository searchHistoryRepository;

    private final UserPostRepository userPostRepository;

    private final HashTagRepository hashTagRepository;

    private final RedisService redisService;

    private final Helper helper;
    

    @Override
    public ResponseEntity<Object> suggestiveProfiles(SearchDto searchDto) {
        try {

            Long profile = profileRepository.findProfileIdById(searchDto.getProfileId());

            if(profile == null){
                logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
                return ResponseEntity.badRequest().body("Your profile ID is Invalid");
            }
            else{
                rabbitTemplate.convertAndSend("searchSaveOperation", searchDto.toJson());

                List<Map<String, Object>> data = searchAlgorithm.suggestiveProfiles(searchDto);

                List<Long> profileIds = data.stream().map(map -> (Long) map.get("profileId")).collect(Collectors.toList());

                redisService.saveDataWithDynamicExpiration("suggestive"+searchDto.getProfileId().toString()+searchDto.getKeyword(),profileIds,Duration.ofMinutes(5));

                return ResponseEntity.status(200).body(data);
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @Override
    public ResponseEntity<Object> fetchSearchHistory(Long profileId) {
        try {
            Long profile = profileRepository.findProfileIdById(profileId);

            if(profile == null){
                logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
                return ResponseEntity.badRequest().body("Your profile ID is Invalid");
            }
            else{
                Optional<Map<String, Object>> historyOpt = searchHistoryRepository.findHistoryByProfileId(profileId);
                Map<String, Object> history = historyOpt.get();
            if (history.get("id") != null) {
                String[] words = ((String) history.get("keyword")).split("\\s*,\\s*");
                
                return ResponseEntity.status(200).body(words);
            } else {
                return ResponseEntity.status(200).body(history);
            }
            }
        } catch (Exception exception) {
            logger.error("\nSearch history fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @Override
    public ResponseEntity<Object> profileResults(SearchDto searchDto) {
        try {
            Long profile = profileRepository.findProfileIdById(searchDto.getProfileId());

            if(profile == null){
                logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
                return ResponseEntity.badRequest().body("Your profile ID is Invalid");
            }
            else{

                return ResponseEntity.status(200).body(searchAlgorithm.resultsProfiles(searchDto));
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @Override
    public ResponseEntity<Object> searchPostResults(SearchDto searchDto) {
        try {
            List<Map<String, Object>> data = null;
            Long profile = profileRepository.findProfileIdById(searchDto.getProfileId());

            if(profile == null){
                logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
                return ResponseEntity.badRequest().body("Your profile ID is Invalid");
            }
            else{
                Object fromRedis = redisService.getDataByKey(searchDto.getProfileId().toString()+searchDto.getKeyword());

                Object profileIds = redisService.getDataByKey("suggestive"+searchDto.getProfileId().toString()+searchDto.getKeyword());
                if(profileIds == null){
                    profileIds = Arrays.asList(0L);
                }

                if(fromRedis == null){
                List<Long> excludedIds = Arrays.asList(0L);
                data = userPostRepository.findUserPostData(0,searchDto.getKeyword(),excludedIds);
                List<Map<String, Object>> fromUsers = userPostRepository.findSpecificUsersPostsData(0,(List<Long>)profileIds,excludedIds);
                data.addAll(fromUsers);

                    // Using Java Streams to retrieve all values associated with the key "id" and store them in a List<Long>
                    List<Long> ids = data.stream().map(map -> (Long) map.get("id")).collect(Collectors.toList());

                    Long maxId = ids.stream().max(Long::compareTo).orElse(0L);

                    PageDto page = new PageDto();
                    page.setContents(ids);
                    page.setPagenumber(searchDto.getPageNumber());
                    page.setOffset(maxId.intValue());
                    List<PageDto> list2 = new ArrayList<>();
                    list2.add(page);
                    redisService.saveDataWithDynamicExpiration(searchDto.getProfileId().toString()+searchDto.getKeyword(),list2,Duration.ofMinutes(5));
                }else{
                List<PageDto> list1 = (List<PageDto>) fromRedis;
                List<Long> excludedIds = list1.stream().flatMap(page -> page.getContents().stream()).collect(Collectors.toList());

                    int track = 0 ;
                    int maxOffset = 0;

                    for (PageDto p : list1) {
                        if (p.getPagenumber() == searchDto.getPageNumber()) {
                            redisService.updateExpiration(searchDto.getProfileId().toString()+searchDto.getKeyword(),Duration.ofMinutes(5));
                            data = userPostRepository.findUserPostsDataByIds(p.getContents());
                            track = 1;
                            break;
                        }
                        Integer offset = p.getOffset();
                        if (offset > maxOffset) {
                            maxOffset = offset;
                        }
                    }

                    if(track == 0){
                        data = userPostRepository.findUserPostData(maxOffset,searchDto.getKeyword(),excludedIds);
                        List<Map<String, Object>> fromUsers = userPostRepository.findSpecificUsersPostsData(maxOffset,(List<Long>)profileIds,excludedIds);
                data.addAll(fromUsers);

                        List<Long> ids = data.stream().map(map -> (Long) map.get("id")).collect(Collectors.toList());

                        PageDto page = new PageDto();
                        page.setContents(ids);
                        page.setPagenumber(searchDto.getPageNumber());
                        list1.add(page);
                        redisService.saveDataWithDynamicExpiration(searchDto.getProfileId().toString()+searchDto.getKeyword(),list1,Duration.ofMinutes(5));
                    }
                }
                // Shuffle the list
                Collections.shuffle(data);
                return ResponseEntity.status(200).body(helper.postMapTimer(data,searchDto.getProfileId()));
            }
        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }


    @Override
    public ResponseEntity<Object> fetchHashTags(SearchHashTagDto hashTagDto) {
        try {

            
                // rabbitTemplate.convertAndSend("searchSaveOperation", searchDto.toJson());

                return ResponseEntity.status(200).body(hashTagRepository.findActiveHashTagsWithPosts(hashTagDto.getKeyword()));

        } catch (Exception exception) {
            logger.error("\nBrand fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @Override
    public ResponseEntity<Object> discover(Long profileId) {
        try {
            Long profile = profileRepository.findProfileIdById(profileId);

            if(profile == null){
                logger.error("Failed it seems like your profile does not exist, or you try to Hack us");
                return ResponseEntity.badRequest().body("Your profile ID is Invalid");
            }
            else{
                List<Long> profileIds = Arrays.asList(1L,2L);
                List<Long> excludedIds = Arrays.asList(16L,2L,13L,15L);
                List<Map<String, Object>> data = userPostRepository.findUsersPostsData(0,profileIds,excludedIds);
                return ResponseEntity.status(200).body(helper.postMapTimer(data,profileId));
            }
        } catch (Exception exception) {
            logger.error("\nSearch history fetching failed , Server Error : " + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
    
}
