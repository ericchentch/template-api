package edunhnil.project.forum.api.dao.likeRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edunhnil.project.forum.api.dao.AbstractRepository;

@Repository
public class LikeRepositoryImpl extends AbstractRepository implements LikeRepository {

    @Override
    public int getTotalLike(Map<String, String> allParams) {
        return getTotal(allParams, Like.class);
    }

    @Override
    public void updateLike(Like like) {
        String[] ignores = { "id" };
        save(like, ignores);
    }

    @Override
    public Optional<List<Like>> getLikes(Map<String, String> allParams) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT * FROM forum.like");
        sql.append(convertParamsFilterSelectQuery(allParams, Like.class, 0, 0, "", ""));
        return replaceQuery(sql.toString(), Like.class);
    }

}
