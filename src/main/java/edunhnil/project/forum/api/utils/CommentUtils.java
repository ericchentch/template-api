package edunhnil.project.forum.api.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edunhnil.project.forum.api.constant.DateTime;
import edunhnil.project.forum.api.dao.commentRepository.Comment;
import edunhnil.project.forum.api.dao.likeRepository.LikeRepository;
import edunhnil.project.forum.api.dao.userRepository.User;
import edunhnil.project.forum.api.dao.userRepository.UserRepository;
import edunhnil.project.forum.api.dto.commentDTO.CommentResponse;
import edunhnil.project.forum.api.dto.userDTO.UserResponse;

@Component
public class CommentUtils {

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    UserRepository userRepository;

    public CommentResponse generateCommentResponse(Comment c, String type, String loginId) {
        Map<String, String> allParams = new HashMap<>();
        allParams.put("targetId", c.getId());
        allParams.put("type", "comment");

        Map<String, String> paramsLiked = new HashMap<>();
        paramsLiked.put("targetId", c.getId());
        paramsLiked.put("type", "comment");
        paramsLiked.put("ownerId", loginId);

        if (type.compareTo("public") == 0) {
            paramsLiked.put("deleted", "0");
        }

        if (type.compareTo("public") == 0) {
            return new CommentResponse(c.getId(), c.getOwnerId(),
                    returnUser(c.getOwnerId(), type),
                    c.getPostId(),
                    c.getContent(), likeRepository.getTotalLike(allParams),
                    DateFormat.toDateString(c.getCreated(), DateTime.YYYY_MM_DD),
                    DateFormat.toDateString(c.getModified(), DateTime.YYYY_MM_DD),
                    false,
                    0);
        } else {
            return new CommentResponse(c.getId(), c.getOwnerId(),
                    returnUser(c.getOwnerId(), type),
                    c.getPostId(),
                    c.getContent(), likeRepository.getTotalLike(allParams),
                    DateFormat.toDateString(c.getCreated(), DateTime.YYYY_MM_DD),
                    DateFormat.toDateString(c.getModified(), DateTime.YYYY_MM_DD),
                    likeRepository.getTotalLike(paramsLiked) != 0,
                    c.getDeleted());
        }
    }

    private UserResponse returnUser(String userId, String type) {
        Optional<User> user = userRepository.getUserById(userId);
        User deletedUser = new User(new ObjectId(userId), "", "", 0, "", "", "Deleted user", "", "", "", "",
                null,
                null, "",
                false,
                false, null, 0);
        if (user.isEmpty()) {
            return userUtils.generateUserResponse(deletedUser, type);
        } else {
            return userUtils.generateUserResponse(user.get(), type);
        }
    }
}
