package edunhnil.project.forum.api.dao.postRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostRepository {
        Optional<List<Post>> getPostsByAuthorId(Map<String, String> allParams, String keySort, int page, int pageSize,
                        String sortField);

        void savePost(Post post);

}
