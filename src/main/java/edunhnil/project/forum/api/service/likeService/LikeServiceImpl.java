package edunhnil.project.forum.api.service.likeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.dao.commentRepository.Comment;
import edunhnil.project.forum.api.dao.commentRepository.CommentRepository;
import edunhnil.project.forum.api.dao.likeRepository.Like;
import edunhnil.project.forum.api.dao.likeRepository.LikeRepository;
import edunhnil.project.forum.api.dao.postRepository.Post;
import edunhnil.project.forum.api.dao.postRepository.PostRepository;
import edunhnil.project.forum.api.dao.userRepository.UserRepository;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.service.AbstractService;
import edunhnil.project.forum.api.utils.DateFormat;

@Service
public class LikeServiceImpl extends AbstractService<LikeRepository>
        implements LikeService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void saveCommentLike(String ownerId, String commentId) {
        checkOwnerId(ownerId);
        checkCommentId(commentId);
        Map<String, String> allParams = new HashMap<>();
        allParams.put("ownerId", ownerId);
        allParams.put("targetId", commentId);
        allParams.put("type", "comment");
        List<Like> likes = repository.getLikes(allParams).get();
        if (likes.size() > 0) {
            Like like = likes.get(0);
            like.setDeleted(1);
            repository.updateLike(like);
        } else {
            Like like = new Like(UUID.randomUUID().toString(), ownerId, commentId, "comment",
                    DateFormat.getCurrentTime(), 0);
            repository.updateLike(like);
        }
    }

    @Override
    public void savePostLike(String ownerId, String postId) {
        checkOwnerId(ownerId);
        checkPostId(postId);
        Map<String, String> allParams = new HashMap<>();
        allParams.put("ownerId", ownerId);
        allParams.put("targetId", postId);
        allParams.put("type", "post");
        List<Like> likes = repository.getLikes(allParams).get();
        if (likes.size() > 0) {
            Like like = likes.get(0);
            like.setDeleted(1);
            repository.updateLike(like);
        } else {
            Like like = new Like(UUID.randomUUID().toString(), ownerId, postId, "post", DateFormat.getCurrentTime(), 0);
            repository.updateLike(like);
        }
    }

    void checkPostId(String postId) {
        Map<String, String> postIds = new HashMap<>();
        postIds.put("id", postId);
        List<Post> posts = postRepository.getPostsByAuthorId(postIds, "", 0, 0, "").get();
        if (posts.size() == 0) {
            throw new ResourceNotFoundException("Not found post with id: " +
                    postId);
        }
    }

    void checkCommentId(String commentId) {
        Map<String, String> allParams = new HashMap<>();
        allParams.put("id", commentId);
        List<Comment> comments = commentRepository.getAllComment(allParams, "", 0, 0, "")
                .get();
        if (comments.size() == 0) {
            throw new ResourceNotFoundException("Not found comment with id:" + commentId);
        }
    }

    void checkOwnerId(String ownerId) {
        userRepository.getUserById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user with id: " +
                        ownerId));
    }

}
