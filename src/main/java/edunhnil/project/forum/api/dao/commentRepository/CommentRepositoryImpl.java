package edunhnil.project.forum.api.dao.commentRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edunhnil.project.forum.api.dao.AbstractRepository;

@Repository
public class CommentRepositoryImpl extends AbstractRepository implements CommentRepository {

    @Override
    public Optional<List<Comment>> getAllComment(Map<String, String> allParams, String keySort, int page, int pageSize,
            String sortField) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT * FROM forum.comment c ");
        sql.append(convertParamsFilterSelectQuery(allParams, Comment.class, page, pageSize, keySort, sortField));
        return replaceQuery(sql.toString(), Comment.class);
    }

    @Override
    public void addNewComment(Comment comment) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO forum.comment (owner_id, post_id, content)");
        sql.append(" VALUES (?, ? ,?)");
        jdbcTemplate.update(sql.toString(), comment.getOwnerId(), comment.getPostId(), comment.getContent());
    }

    @Override
    public void editCommentById(Comment comment, int id) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE forum.comment SET content=?, modified = now()");
        sql.append(" WHERE id = ?");
        jdbcTemplate.update(sql.toString(), comment.getContent(), id);
    }

    @Override
    public void deleteCommentById(int id) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE forum.comment SET deleted = 1, modified = now()");
        sql.append(" WHERE id = ?");
        jdbcTemplate.update(sql.toString(), id);

    }

    @Override
    public void resetId() {
        int max = getMax("SELECT MAX(id) FROM forum.comment") + 1;
        String sql = "ALTER SEQUENCE forum.comment_id_seq RESTART WITH "
                + max;
        jdbcTemplate.execute(sql);

    }

    @Override
    public int getTotalCommentPost(int postId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM forum.comment c");
        sql.append(" WHERE c.deleted = 0 AND c.post_id = " + postId);
        return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
    }

    @Override
    public int getTotalCommentAdmin(Map<String, String> allParams) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM forum.comment c ");
        sql.append(convertParamsFilterSelectQuery(allParams, Comment.class, 0, 0, "", ""));
        return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
    }

}
