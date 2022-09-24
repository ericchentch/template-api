package edunhnil.project.forum.api.dao.categoryRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CategoryRepository {
    Optional<List<Category>> getCategories(Map<String, String> allParams);
}
