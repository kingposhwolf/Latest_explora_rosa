package com.example.demo.Components.QueueListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Components.Helper.Helper;
import com.example.demo.InputDto.SocialMedia.Comment.CommentDto;
import com.example.demo.InputDto.SocialMedia.Comment.CommentLikeDto;
import com.example.demo.InputDto.SocialMedia.Comment.CommentReplyDto;
import com.example.demo.InputDto.SocialMedia.Favorites.FavoritesDto;
import com.example.demo.InputDto.SocialMedia.FollowUnFollow.FollowUnFollowDto;
import com.example.demo.InputDto.SocialMedia.Like.LikeDto;
import com.example.demo.InputDto.SocialMedia.Share.ShareDto;
import com.example.demo.InputDto.SocialMedia.View.ViewDto;
import com.example.demo.InputDto.UserManagement.Profile.ProfileVisitDto;
import com.example.demo.Models.SocialMedia.FollowUnFollow;
import com.example.demo.Models.SocialMedia.HashTag;
import com.example.demo.Models.SocialMedia.UserPost;
import com.example.demo.Models.SocialMedia.Interactions.Comment;
import com.example.demo.Models.SocialMedia.Interactions.CommentLike;
import com.example.demo.Models.SocialMedia.Interactions.Favorites;
import com.example.demo.Models.SocialMedia.Interactions.Like;
import com.example.demo.Models.Tracking.UserToTopicTracking.TopicEngagement;
import com.example.demo.Models.Tracking.UserToUserTracking.UserEngagement;
import com.example.demo.Models.UserManagement.Profile;
import com.example.demo.OutputDto.CommentOutputDto;
import com.example.demo.OutputDto.CommentReplyOutputDto;
import com.example.demo.OutputDto.SocialMediaInstantChange;
import com.example.demo.Repositories.CommentLikeRepository;
import com.example.demo.Repositories.CommentRepository;
import com.example.demo.Repositories.FavoritesRepository;
import com.example.demo.Repositories.FollowUnFollowRepository;
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

    private final FollowUnFollowRepository followUnFollowRepository;

    private final UserEngagementRepository userEngagementRepository;

    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;

    // private AmqpTemplate rabbitTemplate;

    private final Helper helper;

    private final FavoritesRepository favoritesRepository;

    @SuppressWarnings("null")
    @Transactional
    @RabbitListener(queues = "saveLike")
    public void likeSave(String message) {
        try {
            LikeDto likeDto = LikeDto.fromJson(message);

            Optional<Profile> profile = profileRepository.findById(likeDto.getLikerId());
            Optional<UserPost> userPost = userPostRespository.findById(likeDto.getPostId());

            if (!profile.isPresent()) {
                logger.error("User profile not found for profile ID: " + likeDto.getLikerId());
            } else if (!userPost.isPresent()) {
                logger.error("User post not found for post ID: " + likeDto.getPostId());
            } else {
                Optional<Like> like = likeRepository.findByLikerAndPost(profile.get(), userPost.get());

                if (like.isPresent()) {
                    Like x = like.get();

                    UserPost post = userPost.get();

                    int change = post.getLikes() - 1;

                    post.setLikes(change);
                    userPostRespository.save(post);

                    likeRepository.delete(x);

                   // String endpoint = "/topic/like/" + Long.toString(post.getId());
                    SocialMediaInstantChange social = new SocialMediaInstantChange();
                    social.setInteract("Likes");
                    social.setPostId(post.getId());
                    social.setNewNumber(change);

                    messagingTemplate.convertAndSend("/topic/social/", social);

                    processTopicEngagement(profile.get(), userPost.get().getHashTags(),-1);
                    processUserEngagement(profile.get(), userPost.get().getProfile(),-1);

                    logger.info("Like deleted successfully");

                } else {
                    Optional<Like> likeDeletedOptional = likeRepository.findDeletedLikesByPostAndLiker(userPost.get().getId(),profile.get().getId());
                    if(likeDeletedOptional.isPresent()){
                        Like likeDeleted = likeDeletedOptional.get();
                        likeDeleted.setDeleted(false);
                        likeRepository.save(likeDeleted);
                    }else{
                        Like likes = new Like();
                        likes.setLiker(profile.get());
                        likes.setPost(userPost.get());
                        likeRepository.save(likes);
                    
                    }
                    UserPost post = userPost.get();

                    int newLikes = post.getLikes() + 1;

                    post.setLikes(newLikes);
                    userPostRespository.save(post);

                    // String endpoint = "/topic/like/" + Long.toString(post.getId());

                    // messagingTemplate.convertAndSend(endpoint, newLikes);

                    SocialMediaInstantChange social = new SocialMediaInstantChange();
                    social.setInteract("Likes");
                    social.setPostId(post.getId());
                    social.setNewNumber(newLikes);

                    messagingTemplate.convertAndSend("/topic/social/", social);

                    processTopicEngagement(profile.get(), userPost.get().getHashTags(),1);
                    processUserEngagement(profile.get(), userPost.get().getProfile(),1);

                    // logger.info("Like Operationand Tracking Performed successful, with
                    // information : "+ savedLike);
                }
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : " + exception.getMessage());
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

            if (!profile.isPresent()) {
                logger.error("User profile not found for profile ID: " + commentDto.getProfileId());
            } else if (!userPost.isPresent()) {
                logger.error("User post not found for post ID: " + commentDto.getPostId());
            } else {
                Comment comment = new Comment();
                comment.setMessage(commentDto.getMessage());
                comment.setCommenter(profile.get());
                comment.setTimestamp(LocalDateTime.now());
                comment.setUserPost(userPost.get());
                comment.setLikes(0);

                // Save the comment to the database
                Comment savedComment = commentRepository.save(comment);

                int newComments = userPost.get().getComments() + 1;

                userPost.get().setComments(newComments);
                userPostRespository.save(userPost.get());

                CommentOutputDto commentOutput = new CommentOutputDto();
                commentOutput.setMessage(commentDto.getMessage());
                commentOutput.setPostId(commentDto.getPostId());
                commentOutput.setProfileId(commentDto.getProfileId());
                commentOutput.setTime(helper.calculateTimeDifference(comment.getTimestamp()));
                commentOutput.setId(savedComment.getId());


                // String countEndpoint = "/topic/commentCount/" + userPost.get().getId();

                SocialMediaInstantChange social = new SocialMediaInstantChange();
                social.setInteract("Comment");
                social.setPostId(userPost.get().getId());
                social.setNewNumber(newComments);

                messagingTemplate.convertAndSend("/topic/social/", social);

                String commentEndpoint = "/topic/comment/" + userPost.get().getId();

                // messagingTemplate.convertAndSend(countEndpoint, newComments);
                messagingTemplate.convertAndSend(commentEndpoint, commentOutput);

                logger.info("Comment Operation Performed successful, with information : ", savedComment);
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : " + exception.getMessage());
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
                logger.error("User profile not found for profile ID: " + commentDto.getProfileId());
            } else if (!userPost.isPresent()) {
                logger.error("User post not found for post ID: " + commentDto.getPostId());
            } else {
                processTopicEngagement(profile.get(), userPost.get().getHashTags(), 2);
                processUserEngagement(profile.get(), userPost.get().getProfile(), 2);

                logger.info("Comment Operation tracked successful");
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : " + exception.getMessage());
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

            if (!profile.isPresent()) {
                logger.error("User profile not found for profile ID: " + commentReplyDto.getProfileId());
            } else if (!userPost.isPresent()) {
                logger.error("User post not found for post ID: " + commentReplyDto.getPostId());
            } else if (!parentComment.isPresent()) {
                logger.error("Parent Comment not found for ID: " + commentReplyDto.getParentId());
            } else {
                Comment comment = new Comment();
                comment.setMessage(commentReplyDto.getMessage());
                comment.setCommenter(profile.get());
                comment.setUserPost(userPost.get());
                comment.setTimestamp(LocalDateTime.now());
                comment.setParentComment(parentComment.get());
                comment.setLikes(0);
               // parentComment.get().getReplies().add(comment);
                Comment savedComment = commentRepository.save(comment);

                CommentReplyOutputDto commentOutput = new CommentReplyOutputDto();
                commentOutput.setMessage(commentReplyDto.getMessage());
                commentOutput.setPostId(commentReplyDto.getPostId());
                commentOutput.setProfileId(commentReplyDto.getProfileId());
                commentOutput.setParentId(commentReplyDto.getParentId());
                commentOutput.setTime(helper.calculateTimeDifference(comment.getTimestamp()));
                commentOutput.setId(savedComment.getId());

                // int newComments = userPost.get().getComments() + 1;

                // userPost.get().setComments(newComments);
                // userPostRespository.save(userPost.get());

                // Save the comment to the database
                

                String commentEndpoint = "/topic/comment/reply/" + userPost.get().getId();

                messagingTemplate.convertAndSend(commentEndpoint, commentOutput);

                logger.info("Comment Reply Operation Performed successful, with information : " + savedComment);
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : " + exception.getMessage());
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
                logger.error("User profile not found for profile ID: " + commentReplyDto.getProfileId());
            } else if (!userPost.isPresent()) {
                logger.error("User post not found for post ID: " + commentReplyDto.getPostId());
            } else {
                processTopicEngagement(profile.get(), userPost.get().getHashTags(), 2);
                processUserEngagement(profile.get(), userPost.get().getProfile(), 2);
                logger.info("Comment Reply Operation tracked successful");
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : " + exception.getMessage());
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

            processUserEngagement(visitor.get(), owner.get(), 5);

            logger.info("Profile visit Operation tracked successful");

        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : " + exception.getMessage());
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

            if (profile.isEmpty()) {
                logger.error("During Saving Favorites User profile not found for profile ID: "
                        + favoritesDto.getProfileId());
            } else if (post.isEmpty()) {
                logger.error("During Saving Favorites User post not found for post ID: " + favoritesDto.getPostId());
            } else {
                Long existingFavorite = favoritesRepository.findFavoriteByPostAndUser(post.get().getId(), profile.get().getId());
                if(existingFavorite == null){
                    UserPost post1 = post.get();
                    Favorites favorites = new Favorites();
                favorites.setPost(post1);
                favorites.setProfile(profile.get());

                Favorites savedFavorites = favoritesRepository.save(favorites);
                
                int newFavorites = post1.getFavorites() + 1;
                //update number of favorites in the posts
                post1.setFavorites(newFavorites);
                userPostRespository.save(post1);

                SocialMediaInstantChange social = new SocialMediaInstantChange();
                social.setInteract("Favorites");
                social.setPostId(post1.getId());
                social.setNewNumber(newFavorites);

                messagingTemplate.convertAndSend("/topic/social/", social);
                
                // String endpoint = "/topic/favorites/" + post1.getId();

                // messagingTemplate.convertAndSend(endpoint, newFavorites );

                logger.info("Favorites Operation Performed successful, with information : " + savedFavorites.toString());
                }else{
                    logger.info("Post already exist in the favorites");
                }
                
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : " + exception.getMessage());
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
                logger.error("During Tacking Favorites Action, User profile not found for profile ID: "
                        + favoritesDto.getProfileId());
            } else if (!post.isPresent()) {
                logger.error("During Tacking Favorites Action, User post not found for post ID: "
                        + favoritesDto.getPostId());
            } else {
                processTopicEngagement(profile.get(), post.get().getHashTags(), 4);
                processUserEngagement(profile.get(), post.get().getProfile(), 4);

                logger.info("Favorites Operation tracked successful");
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : " + exception.getMessage());
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
                logger.error("During Tacking Share Action, User profile not found for profile ID: "
                        + shareDto.getProfileId());
            } else if (!post.isPresent()) {
                logger.error("During Tacking Share Action, User post not found for post ID: " + shareDto.getPostId());
            } else {
                processTopicEngagement(profile.get(), post.get().getHashTags(), 3);
                processUserEngagement(profile.get(), post.get().getProfile(), 3);

                logger.info("Share Operation tracked successful");
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : " + exception.getMessage());
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
                logger.error(
                        "During Tacking View Action, User profile not found for profile ID: " + viewDto.getProfileId());
            } else if (!post.isPresent()) {
                logger.error("During Tacking View Action, User post not found for post ID: " + viewDto.getPostId());
            } else {
                processTopicEngagement(profile.get(), post.get().getHashTags(), 3);
                processUserEngagement(profile.get(), post.get().getProfile(), 3);

                logger.info("View Operation tracked successful");
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : " + exception.getMessage());
        }
    }

    @Transactional
    @RabbitListener(queues = "likeComment")
    public void commentLike(String message) {
        try {
            CommentLikeDto commentLikeDto = CommentLikeDto.fromJson(message);

            Optional<Profile> profile = profileRepository.findById(commentLikeDto.getLikerId());
            Optional<Comment> comment = commentRepository.findById(commentLikeDto.getCommentId());

            if (profile.isEmpty()) {
                logger.error("User profile not found for profile ID: " + commentLikeDto.getLikerId());
            } else if (comment.isEmpty()) {
                logger.error("Comment not found for comment ID: " + commentLikeDto.getCommentId());
            } else {
                Optional<CommentLike> commentLike = commentLikeRepository.findByCommentAndLiker(comment.get(), profile.get());

                if (commentLike.isPresent()) {
                    CommentLike commentLike2 = commentLike.get();

                    Comment comment2 = comment.get();

                    int change = comment2.getLikes() - 1;

                    comment2.setLikes(change);
                    commentRepository.save(comment2);

                    commentLikeRepository.delete(commentLike2);

                    // String endpoint = "/topic/like/" + Long.toString(post.getId());

                    // messagingTemplate.convertAndSend(endpoint, change);

                    // processTopicEngagement(profile.get(), userPost.get().getHashTags(),-1);
                    // processUserEngagement(profile.get(), userPost.get().getProfile(),-1);

                    logger.info("Comment disliked successful");

                } else {
                    Optional<CommentLike> deletedCommentLikeOptional = commentLikeRepository.findDeletedLikesByPostAndLiker(comment.get().getId(), profile.get().getId());

                    if(deletedCommentLikeOptional.isPresent()){
                        CommentLike deletedCommentLike = deletedCommentLikeOptional.get();
                        deletedCommentLike.setDeleted(false);
                        commentLikeRepository.save(deletedCommentLike);
                    }else{
                        CommentLike commentLike2 = new CommentLike();
                        commentLike2.setLiker(profile.get());
                        commentLike2.setComment(comment.get());
                        commentLikeRepository.save(commentLike2);
                    }

                    Comment comment2 = comment.get();

                    int newLikes = comment2.getLikes() + 1;

                    comment2.setLikes(newLikes);
                    commentRepository.save(comment2);

                    // String endpoint = "/topic/like/" + Long.toString(post.getId());

                    // messagingTemplate.convertAndSend(endpoint, newLikes);

                    // processTopicEngagement(profile.get(), userPost.get().getHashTags(),1);
                    // processUserEngagement(profile.get(), userPost.get().getProfile(),1);

                    // logger.info("Like Operationand Tracking Performed successful, with
                    // information : "+ savedLike);
                }
            }
        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : " + exception.getMessage());
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
                            });
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
                });
    }

    // @SuppressWarnings("null")
    // @Transactional
    // @RabbitListener(queues = "follow")
    // public void followOperation(String message) {
    //     try {
    //         FollowUnFollowDto followUnFollowDto = FollowUnFollowDto.fromJson(message);
    //         Optional<Profile> accountOpt = profileRepository.findById(followUnFollowDto.getAccountId());

    //         Optional<Profile> userOpt = profileRepository.findById(followUnFollowDto.getUser());

    //         if(accountOpt.isEmpty()){
    //             logger.error("Follow Operation failed the acoount to be followed is not present, provided id: "+ followUnFollowDto.getAccountId());
    //         }else if(userOpt.isEmpty()){
    //             logger.error("Follow Operation failed the user to be follow is not present, provided id: "+ followUnFollowDto.getUser());
    //         }else{
    //             Profile account = accountOpt.get();
    //             Profile user = userOpt.get();

    //             Optional<FollowUnFollow> engagement = followUnFollowRepository.findByFollowerAndFollowing(account,user);
                
    //             if(engagement.isPresent()){
    //                 logger.error("User already follow that account : "+ message);
    //             }else{
    //                 FollowUnFollow newFollow = new FollowUnFollow();
    //                 newFollow.setFollowing(account);
    //                 newFollow.setFollower(user);
    //                 followUnFollowRepository.save(newFollow);

    //                 account.setFollowers(account.getFollowers() + 1);
    //                 profileRepository.save(account);

    //                 user.setFollowing(user.getFollowing() + 1);
    //                 profileRepository.save(user);
    //                 logger.info("Follow Operation tracked successful");
    //             }
    //         }

    //     } catch (Exception exception) {
    //         logger.error("INTERNAL SERVER ERROR : " + exception.getMessage());
    //     }
    // }

    @SuppressWarnings("null")
    @Transactional
    @RabbitListener(queues = "followOperation")
    public void unFollowOperation(String message) {
        try {
            FollowUnFollowDto followUnFollowDto = FollowUnFollowDto.fromJson(message);
            Optional<Long> engageId = followUnFollowRepository.findEngangeId(followUnFollowDto.getUser(),followUnFollowDto.getAccountId());

            if(engageId.isPresent()){
                Optional<Profile> accountOpt = profileRepository.findById(followUnFollowDto.getAccountId());

                Optional<Profile> userOpt = profileRepository.findById(followUnFollowDto.getUser());

                if(accountOpt.isEmpty()){
                    logger.error("Follow Operation failed the acoount to be followed is not present, provided id: "+ followUnFollowDto.getAccountId());
                }else if(userOpt.isEmpty()){
                    logger.error("Follow Operation failed the user to be follow is not present, provided id: "+ followUnFollowDto.getUser());
                }else{
                    Profile account = accountOpt.get();
                    Profile user = userOpt.get();

                
                    followUnFollowRepository.deleteById(engageId.get());

                    account.setFollowers(account.getFollowers() - 1);
                    profileRepository.save(account);

                    user.setFollowing(user.getFollowing() - 1);
                    profileRepository.save(user);
                    logger.info("Unfollow Operation tracked successful");
                
            }
            }else{
                Optional<FollowUnFollow> deletedEng = followUnFollowRepository.findDeletedEngagement(followUnFollowDto.getUser(),followUnFollowDto.getAccountId());

                Optional<Profile> accountOpt = profileRepository.findById(followUnFollowDto.getAccountId());

                Optional<Profile> userOpt = profileRepository.findById(followUnFollowDto.getUser());

                Profile account = accountOpt.get();
                Profile user = userOpt.get();
                
                if(deletedEng.isPresent()){
                    FollowUnFollow deletedEngagement = deletedEng.get();
                    deletedEngagement.setDeleted(false);
                    followUnFollowRepository.save(deletedEngagement);
                }else{
                    FollowUnFollow newFollow = new FollowUnFollow();
                    newFollow.setFollowing(account);
                    newFollow.setFollower(user);
                    followUnFollowRepository.save(newFollow);
                }

                account.setFollowers(account.getFollowers() + 1);
                    profileRepository.save(account);

                    user.setFollowing(user.getFollowing() + 1);
                    profileRepository.save(user);
                    logger.info("Follow Operation tracked successful");
            }

        } catch (Exception exception) {
            logger.error("INTERNAL SERVER ERROR : " + exception.getMessage());
        }
    }
}