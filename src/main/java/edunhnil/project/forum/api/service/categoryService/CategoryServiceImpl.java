package edunhnil.project.forum.api.service.categoryService;

import static java.util.Map.entry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.dao.accessabilityRepository.Accessability;
import edunhnil.project.forum.api.dao.accessabilityRepository.AccessabilityRepository;
import edunhnil.project.forum.api.dao.categoryRepository.Category;
import edunhnil.project.forum.api.dao.categoryRepository.CategoryRepository;
import edunhnil.project.forum.api.dto.categoryDTO.CategoryRequest;
import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;
import edunhnil.project.forum.api.exception.InvalidRequestException;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.service.AbstractService;

@Service
public class CategoryServiceImpl extends AbstractService<CategoryRepository>
                implements CategoryService {

        @Autowired
        private AccessabilityRepository accessabilityRepository;

        @Override
        public Optional<List<CategoryResponse>> getCategories(Map<String, String> allParams, String loginId,
                        boolean skipAccessability) {
                List<Category> categories = repository.getCategories(allParams).get();
                return Optional.of(categories.stream()
                                .map(cate -> new CategoryResponse(cate.getId(), cate.getCategoryName()))
                                .collect(Collectors.toList()));
        }

        @Override
        public Optional<CategoryResponse> getCategoryById(String id, String loginId,
                        boolean skipAccessability) {
                List<Category> categories = repository.getCategories(Map.ofEntries(entry("id", id))).get();
                if (categories.size() == 0)
                        return Optional.of(new CategoryResponse("", "Deleted category"));
                Category category = categories.get(0);
                return Optional.of(new CategoryResponse(id, category.getCategoryName()));
        }

        @Override
        public void saveCategory(CategoryRequest categoryRequest, String loginId) {
                validate(categoryRequest);
                List<Category> categories = repository
                                .getCategories(Map.ofEntries(entry("categoryName", categoryRequest.getName()))).get();
                if (categories.size() != 0) {
                        throw new InvalidRequestException("This name is not available!");
                }
                Category category = new Category(UUID.randomUUID().toString(), categoryRequest.getName());
                accessabilityRepository
                                .addNewAccessability(new Accessability(null, new ObjectId(loginId), category.getId()));
                repository.saveCategory(category);
        }

        @Override
        public void deleteCategory(String id, String loginId, boolean skipAccessability) {
                List<Category> categories = repository.getCategories(Map.ofEntries(entry("id", id))).get();
                if (categories.size() == 0) {
                        throw new ResourceNotFoundException("This category is not available!");
                }
                checkAccessability(loginId, id, skipAccessability);
                repository.deleteCategory(id);
        }

        @Override
        public void updateCategory(CategoryRequest categoryRequest, String id, String loginId,
                        boolean skipAccessability) {
                validate(categoryRequest);
                List<Category> categories = repository.getCategories(Map.ofEntries(entry("id", id))).get();
                if (categories.size() == 0) {
                        throw new ResourceNotFoundException("This category is not available!");
                }
                checkAccessability(loginId, id, skipAccessability);
                Category category = categories.get(0);
                category.setCategoryName(categoryRequest.getName());
                repository.saveCategory(category);
        }

}
