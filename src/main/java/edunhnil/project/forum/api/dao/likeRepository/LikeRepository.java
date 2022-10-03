package edunhnil.project.forum.api.dao.likeRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LikeRepository {
    int getTotalLike(Map<String, String> allParams);

    Optional<List<Like>> getLikes(Map<String, String> allParams);

    void updateLike(Like like);
}
