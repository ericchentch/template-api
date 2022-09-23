package edunhnil.project.forum.api.service.categoryService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;

public interface CategoryService {
    Optional<List<CategoryResponse>> getCategories(Map<String, String> allParams);

    Optional<CategoryResponse> getCategoryById(int id);

    Optional<CategoryResponse> getCategoryDetailById(int id);
}
