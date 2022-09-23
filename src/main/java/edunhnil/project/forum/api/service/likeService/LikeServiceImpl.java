package edunhnil.project.forum.api.service.likeService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.dao.commentRepository.CommentRepository;
import edunhnil.project.forum.api.dao.likeRepository.LikeRepository;
import edunhnil.project.forum.api.dao.postRepository.PostRepository;
import edunhnil.project.forum.api.dao.userRepository.UserRepository;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.service.AbstractService;

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
    public Optional<Integer> getTotalPostLike(int postId) {
        checkPostId(postId);
        return Optional.of(repository.getTotalPostLike(postId));
    }

    @Override
    public Optional<Integer> getTotalCommentLike(int commentId) {
        checkCommentId(commentId);
        return Optional.of(repository.getTotalCommentLike(commentId));
    }

    @Override
    public Optional<Boolean> checkLikeComment(int commentId, String ownerId) {
        checkCommentId(commentId);
        checkOwnerId(ownerId);
        return Optional.of(repository.checkLikeComment(commentId, ownerId));
    }

    @Override
    public Optional<Boolean> checkLikePost(int postId, String ownerId) {
        checkPostId(postId);
        checkOwnerId(ownerId);
        return Optional.of(repository.checkLikePost(postId, ownerId));
    }

    @Override
    public void addNewCommentLike(String ownerId, int commentId) {
        checkOwnerId(ownerId);
        checkCommentId(commentId);
        repository.resetIdComment();
        if (!repository.checkLikeComment(commentId, ownerId)) {
            repository.addNewCommentLike(ownerId, commentId);
        }
    }

    @Override
    public void addNewPostLike(String ownerId, int postId) {
        checkOwnerId(ownerId);
        checkPostId(postId);
        repository.resetIdPost();
        if (!repository.checkLikePost(postId, ownerId)) {
            repository.addNewPostId(ownerId, postId);
        }
    }

    @Override
    public void hideLikePost(String ownerId, int postId) {
        checkOwnerId(ownerId);
        checkPostId(postId);
        if (repository.checkLikePost(postId, ownerId)) {
            repository.hideLikePost(ownerId, postId);
        }
    }

    @Override
    public void hideCommentLike(String ownerId, int commentId) {
        checkOwnerId(ownerId);
        checkCommentId(commentId);
        if (repository.checkLikeComment(commentId, ownerId)) {
            repository.hideCommentLike(ownerId, commentId);
        }
    }

    void checkPostId(int postId) {
        postRepository.getPostById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found post with id: " +
                        postId));
    }

    void checkCommentId(int commentId) {
        commentRepository.getCommentById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found comment with id:" + commentId));
    }

    void checkOwnerId(String ownerId) {
        userRepository.getUserById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user with id: " +
                        ownerId));
    }

}
