package com.example.demo.Components;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.demo.Dto.LikeDto;
import com.example.demo.Models.HashTag;
import com.example.demo.Models.Like;
import com.example.demo.Models.Profile;
import com.example.demo.Models.TopicEngagement;
import com.example.demo.Models.UserEngagement;
import com.example.demo.Models.UserPost;
import com.example.demo.Repositories.LikeRepository;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.Repositories.TopicEngagementRepository;
import com.example.demo.Repositories.UserEngagementRepository;
import com.example.demo.Repositories.UserPostRespository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RabbitMqConsumers {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqConsumers.class);

    private final ProfileRepository profileRepository;


    private final UserPostRespository userPostRespository;

    private final LikeRepository likeRepository;

    private final TopicEngagementRepository topicEngagementRepository;

    private final UserEngagementRepository userEngagementRepository;

    @SuppressWarnings("null")
    @RabbitListener(queues = "saveLike")
    public void likeSave(String message) {
        try {
            LikeDto likeDto = LikeDto.fromJson(message);

        Optional<Profile> profile = profileRepository.findById(likeDto.getProfileId());
        Optional<UserPost> userPost = userPostRespository.findById(likeDto.getPostId());

            if(!profile.isPresent()){
                logger.error("User profile not found for profile ID: ", likeDto.getProfileId());
            }
            else if(!userPost.isPresent()){
                logger.error("User post not found for post ID: ", likeDto.getPostId());
            }
            else{
                Like like = new Like();
                like.setProfile(profile.get());
                like.setUserPost(userPost.get());
                Like savedLike = likeRepository.save(like);
                logger.info("Like Operation Performed successful, with information : ", savedLike);
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : ", exception.getMessage());
        }
    }

    @SuppressWarnings("null")
    @RabbitListener(queues = "trackLike")
    public void likeTracking(String message){
        try {
            LikeDto likeDto = LikeDto.fromJson(message);

        Optional<Profile> profile = profileRepository.findById(likeDto.getProfileId());
        Optional<UserPost> userPost = userPostRespository.findById(likeDto.getPostId());

            if(!profile.isPresent()){
                logger.error("User profile not found for profile ID: ", likeDto.getProfileId());
            }
            else if(!userPost.isPresent()){
                logger.error("User post not found for post ID: ", likeDto.getPostId());
            }
            else{
                //Topic Engagement
                List<HashTag> topicList = userPost.get().getHashTags();
                    topicList.forEach(hashTag -> {
                        topicEngagementRepository.findByProfileAndHashTags(profile.get(), hashTag)
                                .ifPresentOrElse(
                                        topicEngage -> {
                                            int score = topicEngage.getScore() + 1;
                                            topicEngage.setScore(score);
                                            topicEngagementRepository.save(topicEngage);
                                        },
                                        () -> {
                                            TopicEngagement topicEngagement = new TopicEngagement();
                                            topicEngagement.setProfile(profile.get());
                                            topicEngagement.setHashTags(hashTag);
                                            topicEngagement.setScore(1);
                                            topicEngagementRepository.save(topicEngagement);
                                        }
                                );
                    });

                //Handle User Engagement
            Optional<UserEngagement> userEngagementOptional = userEngagementRepository.findByTargetAndTopic(profile.get(), userPost.get().getProfile());

            userEngagementOptional.ifPresentOrElse(
                userEngagement -> {
                    userEngagement.setScore(userEngagement.getScore() + 1);
                    userEngagementRepository.save(userEngagement);
                },
                () -> {
                    UserEngagement newUserEngagement = new UserEngagement();
                    newUserEngagement.setTarget(profile.get());
                    newUserEngagement.setTopic(userPost.get().getProfile());
                    newUserEngagement.setScore(1);
                    userEngagementRepository.save(newUserEngagement);
                }
            );
            
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : ", exception.getMessage());
        }
    }
}