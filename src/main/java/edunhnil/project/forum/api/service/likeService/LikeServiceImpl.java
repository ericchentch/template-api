package edunhnil.project.forum.api.service.likeService;

import static java.util.Map.entry;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.dao.commentRepository.CommentRepository;
import edunhnil.project.forum.api.dao.likeRepository.Like;
import edunhnil.project.forum.api.dao.likeRepository.LikeRepository;
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
        List<Like> likes = repository.getLikes(
                Map.ofEntries(entry("ownerId", ownerId), entry("targetId", commentId), entry("type", "comment"))).get();
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
        List<Like> likes = repository
                .getLikes(Map.ofEntries(entry("ownerId", ownerId), entry("targetId", postId), entry("type", "post")))
                .get();
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
        if (postRepository.getPostsByAuthorId(Map.ofEntries(entry("id", postId)), "", 0, 0, "").get().size() == 0) {
            throw new ResourceNotFoundException("Not found post with id: " +
                    postId);
        }
    }

    void checkCommentId(String commentId) {
        if (commentRepository.getAllComment(Map.ofEntries(entry("id", commentId)), "", 0, 0, "")
                .get().size() == 0) {
            throw new ResourceNotFoundException("Not found comment with id:" + commentId);
        }
    }

    void checkOwnerId(String ownerId) {
        if (userRepository.getUsers(Map.ofEntries(entry("_id", ownerId)), "", 0, 0, "").get().size() == 0) {
            throw new ResourceNotFoundException("Not found user!");
        }

    }

}
