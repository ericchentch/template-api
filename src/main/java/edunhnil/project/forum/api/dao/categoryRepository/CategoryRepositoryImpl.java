package edunhnil.project.forum.api.dao.categoryRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edunhnil.project.forum.api.dao.AbstractRepository;

@Repository
public class CategoryRepositoryImpl extends AbstractRepository implements CategoryRepository {

    @Override
    public Optional<List<Category>> getCategories(Map<String, String> allParams) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM category");
        sql.append(convertParamsFilterSelectQuery(allParams, Category.class, 0, 0, "", ""));
        return replaceQuery(sql.toString(), Category.class);
    }

    @Override
    public void saveCategory(Category category) {
        String[] ignores = { "" };
        save(category, ignores);
    }

    @Override
    public void deleteCategory(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM category WHERE id=");
        sql.append("'").append(id).append("'");
        jdbcTemplate.execute(sql.toString());
    }

}
