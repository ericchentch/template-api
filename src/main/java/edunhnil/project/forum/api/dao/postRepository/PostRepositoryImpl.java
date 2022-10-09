package edunhnil.project.forum.api.dao.postRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edunhnil.project.forum.api.dao.AbstractRepository;

@Repository
public class PostRepositoryImpl extends AbstractRepository implements PostRepository {

    @Override
    public Optional<List<Post>> getPostsByAuthorId(Map<String, String> allParams, String keySort, int page,
            int pageSize, String sortField) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT * FROM forum.post");
        sql.append(convertParamsFilterSelectQuery(allParams, Post.class, page, pageSize, keySort, sortField));
        return replaceQuery(sql.toString(), Post.class);
    }

    @Override
    public void savePost(Post post) {
        String[] ignores = { "modified" };
        save(post, ignores);
    }

}
