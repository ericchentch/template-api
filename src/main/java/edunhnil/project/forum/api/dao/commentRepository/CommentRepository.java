package edunhnil.project.forum.api.dao.commentRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentRepository {
    Optional<List<Comment>> getAllComment(Map<String, String> allParams, String keySort, int page, int pageSize,
            String sortField);

    void saveComment(Comment comment);

}
