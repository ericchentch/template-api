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
    public void addNewPost(Post post, String authorId) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO forum.post (author_id, title, content, category_id)");
        sql.append(" VALUES(?,?,?,?)");
        jdbcTemplate.update(sql.toString(), authorId, post.getTitle(), post.getContent(),
                post.getCategoryId());
    }

    @Override
    public void updatePostById(Post post, int id) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE forum.post SET title=?, content=?, category_id=?, modified=now()");
        sql.append(" WHERE id=?");
        jdbcTemplate.update(sql.toString(), post.getTitle(), post.getContent(), post.getCategoryId(), id);
    }

    @Override
    public void deletePostById(int id) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE forum.post SET deleted=1, modified=now()");
        sql.append(" WHERE id=?");
        jdbcTemplate.update(sql.toString(), id);
    }

    @Override
    public void oneMoreView(int id) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE forum.post SET view=view+1, modified=now()");
        sql.append(" WHERE id=?");
        jdbcTemplate.update(sql.toString(), id);
    }

    @Override
    public int getTotalPage(Map<String, String> allParams) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM forum.post p");
        sql.append(convertParamsFilterSelectQuery(allParams, Post.class, 0, 0, "", ""));
        return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
    }

    @Override
    public void resetId() {
        int max = getMax("SELECT MAX(id) FROM forum.post") + 1;
        String sql = "ALTER SEQUENCE forum.post_id_seq RESTART WITH " + max;
        jdbcTemplate.execute(sql);
    }

    @Override
    public void changeEnabled(int input, int id) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE forum.post SET enabled=?, modified=now()");
        sql.append(" WHERE id=?");
        jdbcTemplate.update(sql.toString(), input, id);
    }

}
