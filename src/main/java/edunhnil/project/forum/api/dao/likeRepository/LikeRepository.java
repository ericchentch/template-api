package edunhnil.project.forum.api.dao.likeRepository;

public interface LikeRepository {
    int getTotalPostLike(int postId);

    int getTotalCommentLike(int commentId);

    boolean checkLikeComment(int commentId, String ownerId);

    boolean checkLikePost(int postId, String ownerId);

    void addNewCommentLike(String ownerId, int commentId);

    void addNewPostId(String ownerId, int postId);

    void hideLikePost(String ownerId, int postId);

    void hideCommentLike(String ownerId, int commentId);

    void resetIdComment();

    void resetIdPost();
}
