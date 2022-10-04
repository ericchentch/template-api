package edunhnil.project.forum.api.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edunhnil.project.forum.api.constant.DateTime;
import edunhnil.project.forum.api.dao.commentRepository.Comment;
import edunhnil.project.forum.api.dao.likeRepository.LikeRepository;
import edunhnil.project.forum.api.dto.commentDTO.CommentResponse;
import edunhnil.project.forum.api.dto.userDTO.UserResponse;
import edunhnil.project.forum.api.service.userService.UserService;

@Component
public class CommentUtils {

    @Autowired
    private UserService userService;

    @Autowired
    private LikeRepository likeRepository;

    public CommentResponse generateCommentResponse(Comment c, String type, String loginId) {
        UserResponse ownerInformation = userService.getPublicUserById(c.getOwnerId()).get();
        Map<String, String> allParams = new HashMap<>();
        allParams.put("targetId", Integer.toString(c.getId()));
        allParams.put("type", "comment");

        Map<String, String> paramsLiked = new HashMap<>();
        paramsLiked.put("targetId", Integer.toString(c.getId()));
        paramsLiked.put("type", "comment");
        paramsLiked.put("ownerId", loginId);
        paramsLiked.put("deleted", "0");

        if (type.compareTo("public") == 0) {
            return new CommentResponse(c.getId(), c.getOwnerId(),
                    ownerInformation,
                    c.getPostId(),
                    c.getContent(), likeRepository.getTotalLike(allParams),
                    DateFormat.toDateString(c.getCreated(), DateTime.YYYY_MM_DD),
                    DateFormat.toDateString(c.getModified(), DateTime.YYYY_MM_DD),
                    likeRepository.getTotalLike(paramsLiked) != 0,
                    0);
        } else {
            return new CommentResponse(c.getId(), c.getOwnerId(),
                    ownerInformation,
                    c.getPostId(),
                    c.getContent(), likeRepository.getTotalLike(allParams),
                    DateFormat.toDateString(c.getCreated(), DateTime.YYYY_MM_DD),
                    DateFormat.toDateString(c.getModified(), DateTime.YYYY_MM_DD),
                    likeRepository.getTotalLike(paramsLiked) != 0,
                    0);
        }
    }
}
