package com.example.demo.Components.QueueListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.InputDto.CommentDto;
import com.example.demo.InputDto.CommentReplyDto;
import com.example.demo.InputDto.FavoritesDto;
import com.example.demo.InputDto.LikeDto;
import com.example.demo.InputDto.ProfileVisitDto;
import com.example.demo.InputDto.ShareDto;
import com.example.demo.InputDto.ViewDto;
import com.example.demo.Models.SocialMedia.HashTag;
import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.SocialMedia.Interactions.Comment;
import com.example.demo.Models.SocialMedia.Interactions.Favorites;
import com.example.demo.Models.SocialMedia.Interactions.Like;
import com.example.demo.Models.Tracking.UserToTopicTracking.TopicEngagement;
import com.example.demo.Models.Tracking.UserToUserTracking.UserEngagement;
import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.Repositories.CommentRepository;
import com.example.demo.Repositories.FavoritesRepository;
import com.example.demo.Repositories.LikeRepository;
import com.example.demo.Repositories.ProfileRepository;
import com.example.demo.Repositories.TopicEngagementRepository;
import com.example.demo.Repositories.UserEngagementRepository;
import com.example.demo.Repositories.UserPostRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RabbitMqConsumers {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqConsumers.class);

    private SimpMessagingTemplate messagingTemplate;

    private final ProfileRepository profileRepository;

    private final UserPostRepository userPostRespository;

    private final LikeRepository likeRepository;

    private final TopicEngagementRepository topicEngagementRepository;

    private final UserEngagementRepository userEngagementRepository;

    private final CommentRepository commentRepository;

    private final FavoritesRepository favoritesRepository;
    

    @SuppressWarnings("null")
    @Transactional
    @RabbitListener(queues = "saveLike")
    public void likeSave(String message) {
        try {
            LikeDto likeDto = LikeDto.fromJson(message);

        Optional<Profile> profile = profileRepository.findById(likeDto.getLikerId());
        Optional<UserPost> userPost = userPostRespository.findById(likeDto.getPostId());

            if(!profile.isPresent()){
                logger.error("User profile not found for profile ID: "+ likeDto.getLikerId());
            }
            else if(!userPost.isPresent()){
                logger.error("User post not found for post ID: "+ likeDto.getPostId());
            }
            else{
                Optional<Like> like = likeRepository.findByLikerAndPost(profile.get(),userPost.get());
            
            if (like.isPresent()) {

                UserPost post = userPost.get();

                int newLikes = post.getLikes() - 1;

                post.setLikes(newLikes);
                userPostRespository.save(post);

                messagingTemplate.convertAndSend("/topic/like" + post.getId(),newLikes);

                processTopicEngagement(profile.get(), userPost.get().getHashTags(),-1);
                processUserEngagement(profile.get(), userPost.get().getProfile(),-1);

                likeRepository.delete(like.get());
                logger.info("Like deleted successfully");
                
            } else {
                Like likeNew = new Like();
                likeNew.setLiker(profile.get());
                likeNew.setPost(userPost.get());
                Like savedLike = likeRepository.save(likeNew);

                int newLikes = userPost.get().getLikes() + 1;

                userPost.get().setLikes(newLikes);
                userPostRespository.save(userPost.get());

                messagingTemplate.convertAndSend("/topic/like" + userPost.get().getId(),newLikes);

                processTopicEngagement(profile.get(), userPost.get().getHashTags(),1);
                processUserEngagement(profile.get(), userPost.get().getProfile(),1);

                logger.info("Like Operationand Tracking Performed successful, with information : "+ savedLike);
            }
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : "+ exception.getMessage());
        }
    }
    

    @SuppressWarnings("null")
    @Transactional
    @RabbitListener(queues = "saveComment")
    public void commentSave(String message) {
        try {
            CommentDto commentDto = CommentDto.fromJson(message);
            Optional<Profile> profile = profileRepository.findById(commentDto.getProfileId());
            Optional<UserPost> userPost = userPostRespository.findById(commentDto.getPostId());

            if(!profile.isPresent()){
                logger.error("User profile not found for profile ID: "+ commentDto.getProfileId());
            }else if(!userPost.isPresent()){
                logger.error("User post not found for post ID: "+ commentDto.getPostId());
            }else{
                Comment comment = new Comment();
                comment.setMessage(commentDto.getMessage());
                comment.setCommenter(profile.get());
                comment.setTimestamp(LocalDateTime.now());

                int newComments = userPost.get().getComments() + 1;

                userPost.get().setComments(newComments);
                userPostRespository.save(userPost.get());

                // Save the comment to the database
                Comment savedComment = commentRepository.save(comment);

                messagingTemplate.convertAndSend("/topic/commentCount" + userPost.get().getId(),newComments);
                messagingTemplate.convertAndSend("/topic/comment" + userPost.get().getId(),savedComment);

                logger.info("Comment Operation Performed successful, with information : ", savedComment);
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : "+ exception.getMessage());
        }
    }

    @SuppressWarnings("null")
    @Transactional
    @RabbitListener(queues = "trackComment")
    public void commentTracking(String message) {
        try {
            CommentDto commentDto = CommentDto.fromJson(message);
    
            Optional<Profile> profile = profileRepository.findById(commentDto.getProfileId());
            Optional<UserPost> userPost = userPostRespository.findById(commentDto.getPostId());
    
            if (!profile.isPresent()) {
                logger.error("User profile not found for profile ID: "+ commentDto.getProfileId());
            } else if (!userPost.isPresent()) {
                logger.error("User post not found for post ID: "+ commentDto.getPostId());
            } else {
                processTopicEngagement(profile.get(), userPost.get().getHashTags(),2);
                processUserEngagement(profile.get(), userPost.get().getProfile(),2);

                logger.info("Comment Operation tracked successful");
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : "+ exception.getMessage());
        }
    }

    @SuppressWarnings("null")
    @Transactional
    @RabbitListener(queues = "commentReply")
    public void commentReplySave(String message) {
        try {
            CommentReplyDto commentReplyDto = CommentReplyDto.fromJson(message);
            Optional<Profile> profile = profileRepository.findById(commentReplyDto.getProfileId());
            Optional<UserPost> userPost = userPostRespository.findById(commentReplyDto.getPostId());
            Optional<Comment> parentComment = commentRepository.findById(commentReplyDto.getParentId());

            if(!profile.isPresent()){
                logger.error("User profile not found for profile ID: "+ commentReplyDto.getProfileId());
            }else if(!userPost.isPresent()){
                logger.error("User post not found for post ID: "+ commentReplyDto.getPostId());
            }else if(!parentComment.isPresent()){
                logger.error("Parent Comment not found for ID: "+ commentReplyDto.getParentId());
            }else{
                Comment comment = new Comment();
                comment.setMessage(commentReplyDto.getMessage());
                comment.setCommenter(profile.get());
                comment.setTimestamp(LocalDateTime.now());
                comment.setParentComment(parentComment.get());
                parentComment.get().getReplies().add(comment);

                int newComments = userPost.get().getComments() + 1;

                userPost.get().setComments(newComments);
                userPostRespository.save(userPost.get());

                // Save the comment to the database
                Comment savedComment = commentRepository.save(parentComment.get());

                messagingTemplate.convertAndSend("/topic/commentCount" + userPost.get().getId(),newComments);
                messagingTemplate.convertAndSend("/topic/comment" + userPost.get().getId(),savedComment);

                logger.info("Comment Reply Operation Performed successful, with information : "+ savedComment);
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : "+ exception.getMessage());
        }
    }

    @SuppressWarnings("null")
    @Transactional
    @RabbitListener(queues = "trackCommentReply")
    public void commentReplyTracking(String message) {
        try {
            CommentReplyDto commentReplyDto = CommentReplyDto.fromJson(message);
    
            Optional<Profile> profile = profileRepository.findById(commentReplyDto.getProfileId());
            Optional<UserPost> userPost = userPostRespository.findById(commentReplyDto.getPostId());
    
            if (!profile.isPresent()) {
                logger.error("User profile not found for profile ID: "+ commentReplyDto.getProfileId());
            } else if (!userPost.isPresent()) {
                logger.error("User post not found for post ID: "+ commentReplyDto.getPostId());
            } else {
                processTopicEngagement(profile.get(), userPost.get().getHashTags(),2);
                processUserEngagement(profile.get(), userPost.get().getProfile(),2);
                logger.info("Comment Reply Operation tracked successful");
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : "+ exception.getMessage());
        }
    }

    @SuppressWarnings("null")
    @Transactional
    @RabbitListener(queues = "trackProfileVisit")
    public void trackProfileVisit(String message) {
        try {
            ProfileVisitDto brandVisitDto = ProfileVisitDto.fromJson(message);
            Optional<Profile> owner = profileRepository.findById(brandVisitDto.getOwnerId());

            Optional<Profile> visitor = profileRepository.findById(brandVisitDto.getVisitorId());

            processUserEngagement(visitor.get(), owner.get(),5);

            logger.info("Profile visit Operation tracked successful");
            
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : "+ exception.getMessage());
        }
    }

    @SuppressWarnings("null")
    @Transactional
    @RabbitListener(queues = "saveFavorites")
    public void saveFavorites(String message) {
        try {
            FavoritesDto favoritesDto = FavoritesDto.fromJson(message);
    
            Optional<Profile> profile = profileRepository.findById(favoritesDto.getProfileId());
            Optional<UserPost> post = userPostRespository.findById(favoritesDto.getPostId());

            if(!profile.isEmpty()){
                logger.error("During Saving Favorites User profile not found for profile ID: "+ favoritesDto.getProfileId());
            }else if(post.isEmpty()){
                logger.error("During Saving Favorites User post not found for post ID: "+ favoritesDto.getPostId());
            }else{
                Favorites favorites = new Favorites();
                favorites.setPost(post.get());
                favorites.setProfile(profile.get());

                Favorites savedFavorites = favoritesRepository.save(favorites);

                logger.info("Favorites Operation Performed successful, with information : "+ savedFavorites);
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : "+ exception.getMessage());
        }
    }

    @SuppressWarnings("null")
    @Transactional
    @RabbitListener(queues = "trackFavorites")
    public void trackFavorites(String message) {
        try {
            FavoritesDto favoritesDto = FavoritesDto.fromJson(message);
    
            Optional<Profile> profile = profileRepository.findById(favoritesDto.getProfileId());
            Optional<UserPost> post = userPostRespository.findById(favoritesDto.getPostId());
    
            if (!profile.isPresent()) {
                logger.error("During Tacking Favorites Action, User profile not found for profile ID: "+ favoritesDto.getProfileId());
            } else if (!post.isPresent()) {
                logger.error("During Tacking Favorites Action, User post not found for post ID: "+ favoritesDto.getPostId());
            } else {
                processTopicEngagement(profile.get(), post.get().getHashTags(),4);
                processUserEngagement(profile.get(), post.get().getProfile(),4);

                logger.info("Favorites Operation tracked successful");
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : "+ exception.getMessage());
        }
    }

    @SuppressWarnings("null")
    @Transactional
    @RabbitListener(queues = "trackShare")
    public void trackShares(String message) {
        try {
            ShareDto shareDto = ShareDto.fromJson(message);
    
            Optional<Profile> profile = profileRepository.findById(shareDto.getProfileId());
            Optional<UserPost> post = userPostRespository.findById(shareDto.getPostId());
    
            if (!profile.isPresent()) {
                logger.error("During Tacking Share Action, User profile not found for profile ID: "+ shareDto.getProfileId());
            } else if (!post.isPresent()) {
                logger.error("During Tacking Share Action, User post not found for post ID: "+ shareDto.getPostId());
            } else {
                processTopicEngagement(profile.get(), post.get().getHashTags(),3);
                processUserEngagement(profile.get(), post.get().getProfile(),3);

                logger.info("Share Operation tracked successful");
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : "+ exception.getMessage());
        }
    }

    @SuppressWarnings("null")
    @Transactional
    @RabbitListener(queues = "trackView")
    public void trackViews(String message) {
        try {
            ViewDto viewDto = ViewDto.fromJson(message);
    
            Optional<Profile> profile = profileRepository.findById(viewDto.getProfileId());
            Optional<UserPost> post = userPostRespository.findById(viewDto.getPostId());
    
            if (!profile.isPresent()) {
                logger.error("During Tacking View Action, User profile not found for profile ID: "+ viewDto.getProfileId());
            } else if (!post.isPresent()) {
                logger.error("During Tacking View Action, User post not found for post ID: "+ viewDto.getPostId());
            } else {
                processTopicEngagement(profile.get(), post.get().getHashTags(),3);
                processUserEngagement(profile.get(), post.get().getProfile(),3);

                logger.info("View Operation tracked successful");
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : "+ exception.getMessage());
        }
    }

    private void processTopicEngagement(Profile profile, List<HashTag> hashTags, int score2) {
        hashTags.forEach(hashTag -> {
            topicEngagementRepository.findByProfileAndHashTags(profile, hashTag)
                    .ifPresentOrElse(
                            topicEngage -> {
                                int score = topicEngage.getScore() + score2;
                                topicEngage.setScore(score);
                                topicEngagementRepository.save(topicEngage);
                            },
                            () -> {
                                TopicEngagement topicEngagement = new TopicEngagement();
                                topicEngagement.setProfile(profile);
                                topicEngagement.setHashTags(hashTag);
                                topicEngagement.setScore(score2);
                                topicEngagementRepository.save(topicEngagement);
                            }
                    );
        });
    }
    
    private void processUserEngagement(Profile target, Profile topic, int score) {
        Optional<UserEngagement> userEngagementOptional = userEngagementRepository.findByTargetAndTopic(target, topic);
    
        userEngagementOptional.ifPresentOrElse(
                userEngagement -> {
                    userEngagement.setScore(userEngagement.getScore() + score);
                    userEngagementRepository.save(userEngagement);
                },
                () -> {
                    UserEngagement newUserEngagement = new UserEngagement();
                    newUserEngagement.setTarget(target);
                    newUserEngagement.setTopic(topic);
                    newUserEngagement.setScore(score);
                    userEngagementRepository.save(newUserEngagement);
                }
        );
    }
}