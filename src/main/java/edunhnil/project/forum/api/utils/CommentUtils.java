package edunhnil.project.forum.api.utils;

import static java.util.Map.entry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edunhnil.project.forum.api.constant.DateTime;
import edunhnil.project.forum.api.dao.commentRepository.Comment;
import edunhnil.project.forum.api.dao.likeRepository.LikeRepository;
import edunhnil.project.forum.api.dao.userRepository.User;
import edunhnil.project.forum.api.dao.userRepository.UserRepository;
import edunhnil.project.forum.api.dto.commentDTO.CommentResponse;
import edunhnil.project.forum.api.dto.commonDTO.UserInformation;

@Component
public class CommentUtils {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    public CommentResponse generateCommentResponse(Comment c, String type, String loginId) {
        Map<String, String> allParams = new HashMap<>();
        allParams.put("targetId", c.getId());
        allParams.put("type", "comment");

        Map<String, String> paramsLiked = new HashMap<>();
        paramsLiked.put("targetId", c.getId());
        paramsLiked.put("type", "comment");
        paramsLiked.put("ownerId", loginId);

        if (type.compareTo("public") == 0) {
            allParams.put("deleted", "0");
            paramsLiked.put("deleted", "0");
        }

        if (type.compareTo("public") == 0) {
            return new CommentResponse(c.getId(),
                    returnUser(c.getOwnerId()),
                    c.getPostId(),
                    c.getContent(), likeRepository.getTotalLike(allParams),
                    DateFormat.toDateString(c.getCreated(), DateTime.YYYY_MM_DD),
                    DateFormat.toDateString(c.getModified(), DateTime.YYYY_MM_DD),
                    false,
                    0);
        } else {
            return new CommentResponse(c.getId(),
                    returnUser(c.getOwnerId()),
                    c.getPostId(),
                    c.getContent(), likeRepository.getTotalLike(allParams),
                    DateFormat.toDateString(c.getCreated(), DateTime.YYYY_MM_DD),
                    DateFormat.toDateString(c.getModified(), DateTime.YYYY_MM_DD),
                    likeRepository.getTotalLike(paramsLiked) != 0,
                    c.getDeleted());
        }
    }

    private UserInformation returnUser(String userId) {
        List<User> users = userRepository.getUsers(Map.ofEntries(entry("_id", userId)), "", 0, 0, "").get();
        if (users.size() == 0) {
            return new UserInformation("", "", "Deleted user");
        } else {
            return new UserInformation(userId, users.get(0).getLastName(), users.get(0).getFirstName());
        }
    }
}
