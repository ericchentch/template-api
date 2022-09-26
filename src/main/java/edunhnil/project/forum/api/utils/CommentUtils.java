package edunhnil.project.forum.api.utils;

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

    public CommentResponse generateCommentResponse(Comment c, String type) {
        UserResponse ownerInformation = userService.getPublicUserById(c.getOwnerId()).get();
        if (type.compareTo("public") == 0) {
            return new CommentResponse(c.getId(), c.getOwnerId(),
                    ownerInformation,
                    c.getPostId(),
                    c.getContent(), likeRepository.getTotalCommentLike(c.getId()),
                    DateFormat.toDateString(c.getCreated(), DateTime.YYYY_MM_DD),
                    DateFormat.toDateString(c.getModified(), DateTime.YYYY_MM_DD),
                    0);
        } else {
            return new CommentResponse(c.getId(), c.getOwnerId(),
                    ownerInformation,
                    c.getPostId(),
                    c.getContent(), likeRepository.getTotalCommentLike(c.getId()),
                    DateFormat.toDateString(c.getCreated(), DateTime.YYYY_MM_DD),
                    DateFormat.toDateString(c.getModified(), DateTime.YYYY_MM_DD),
                    0);
        }
    }
}