package edunhnil.project.forum.api.service.likeService;

public interface LikeService {

    void saveCommentLike(String ownerId, int commentId);

    void savePostLike(String ownerId, int postId);
}
