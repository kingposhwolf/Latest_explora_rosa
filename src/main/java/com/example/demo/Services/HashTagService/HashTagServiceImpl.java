package com.example.demo.Services.HashTagService;

import com.example.demo.Dto.HashTagDto;
import com.example.demo.Models.HashTag;
import com.example.demo.Repositories.HashTagRepository;
import com.example.demo.Services.CityService.CityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class HashTagServiceImpl implements HashTagService {

    private static final Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);

    private final HashTagRepository hashTagRepository;

    public HashTagServiceImpl(HashTagRepository hashTagRepository){
        this.hashTagRepository = hashTagRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<Object> getAllHashTags(){
        try{
            Iterable<HashTag> hashTag = hashTagRepository.findAll();

        if(hashTag.iterator().hasNext()){
            logger.error("\nThere is Request for Fetching All Suggestions, But Nothing Registered to the database ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is No  HashTag registered in the Database");
        }else{
            logger.info("\nSuccessful fetched all Suggestions");
            return ResponseEntity.status(200).body(hashTag);
        }

        }catch (Exception exception) {
            logger.error("\nFailed to fetch Countries, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }

    }

    @Override
    public ResponseEntity<Object> saveHashTag(HashTagDto hashTagDto) {
        try {
            Optional<HashTag> existingHashTag = hashTagRepository.findByName(hashTagDto.getName());

            if(existingHashTag.isPresent()){
                logger.error("\nFailed to save HashTag, HashTag Already Exists Error");
                return ResponseEntity.status(400).body("HashTag Already Exists!");
            }
            else{
                HashTag hashTag = new HashTag();
                hashTag.setName(hashTagDto.getName());
                hashTagRepository.save(hashTag);

                logger.info("HashTag saved Sucessfully\n" + hashTag);
                return ResponseEntity.status(201).body("HashTag Created Successful ");
            }
        } catch (Exception exception) {
            logger.error("\nFailed to save HashTag, Server Error: \n" + exception.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
    @Override
    public ResponseEntity<Object> getHashTagByName(String name) {
        try {
            Optional<HashTag> hashTagOptional = hashTagRepository.findByName(name);
            if (hashTagOptional.isPresent()) {
                HashTag hashTag = hashTagOptional.get();
                logger.info("HashTag found: {}", hashTag);
                return ResponseEntity.status(HttpStatus.OK).body(hashTag);
            } else {
                logger.error("HashTag not found for name: {}", name);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("HashTag not found for name: " + name);
            }
        } catch (Exception e) {
            logger.error("Failed to get hashTag by name: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
