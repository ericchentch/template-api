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
    public void saveComment(Comment comment) {
        String[] ignores = { "modified" };
        save(comment, ignores);
    }

}
