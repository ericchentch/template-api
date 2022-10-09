package edunhnil.project.forum.api.service.likeService;

public interface LikeService {

    void saveCommentLike(String ownerId, String commentId);

    void savePostLike(String ownerId, String postId);
}
