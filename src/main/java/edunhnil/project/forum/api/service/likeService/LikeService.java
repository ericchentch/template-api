package edunhnil.project.forum.api.service.likeService;

import java.util.Optional;

public interface LikeService {
    Optional<Integer> getTotalPostLike(int postId);

    Optional<Integer> getTotalCommentLike(int commentId);

    Optional<Boolean> checkLikeComment(int commentId, String ownerId);

    Optional<Boolean> checkLikePost(int postId, String ownerId);

    void addNewCommentLike(String ownerId, int commentId);

    void addNewPostLike(String ownerId, int postId);

    void hideLikePost(String ownerId, int postId);

    void hideCommentLike(String ownerId, int commentId);
}
