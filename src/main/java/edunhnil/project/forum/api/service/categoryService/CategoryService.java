package edunhnil.project.forum.api.service.categoryService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import edunhnil.project.forum.api.dto.categoryDTO.CategoryRequest;
import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;

public interface CategoryService {
    Optional<List<CategoryResponse>> getCategories(Map<String, String> allParams);

    Optional<CategoryResponse> getCategoryById(String id);

    Optional<CategoryResponse> getCategoryDetailById(String id);

    void saveCategory(CategoryRequest categoryRequest);

    void updateCategory(CategoryRequest categoryRequest, String id);

    void deleteCategory(String id);
}
