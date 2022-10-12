package edunhnil.project.forum.api.service.categoryService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import edunhnil.project.forum.api.dto.categoryDTO.CategoryRequest;
import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;

public interface CategoryService {
        Optional<List<CategoryResponse>> getCategories(Map<String, String> allParams, String loginId,
                        boolean skipAccessability);

        Optional<CategoryResponse> getCategoryById(String id, String loginId,
                        boolean skipAccessability);

        void saveCategory(CategoryRequest categoryRequest, String loginId);

        void updateCategory(CategoryRequest categoryRequest, String id, String loginId, boolean skipAccessability);

        void deleteCategory(String id, String loginId, boolean skipAccessability);
}
