package edunhnil.project.forum.api.dao.categoryRepository;

import edunhnil.project.forum.api.dao.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CategoryRepositoryImpl extends AbstractRepository implements CategoryRepository {

    @Override
    public Optional<List<Category>> getCategories(Map<String, String> allParams) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM category");
        sql.append(convertParamsFilterSelectQuery(allParams, Category.class));
        return replaceQuery(sql.toString(), Category.class);
    }

    @Override
    public Optional<Category> getCategoryById(int id) {
        String sql = "SELECT * FROM category WHERE id = ?";
        return replaceQueryForObjectWithId(sql.toString(), Category.class, id);
    }

}
