package edunhnil.project.forum.api.service.categoryService;

import static java.util.Map.entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.dao.categoryRepository.Category;
import edunhnil.project.forum.api.dao.categoryRepository.CategoryRepository;
import edunhnil.project.forum.api.dao.postRepository.Post;
import edunhnil.project.forum.api.dao.postRepository.PostRepository;
import edunhnil.project.forum.api.dto.categoryDTO.CategoryRequest;
import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;
import edunhnil.project.forum.api.dto.postDTO.PostResponse;
import edunhnil.project.forum.api.exception.InvalidRequestException;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.service.AbstractService;
import edunhnil.project.forum.api.utils.PostUtils;

@Service
public class CategoryServiceImpl extends AbstractService<CategoryRepository>
                implements CategoryService {

        @Autowired
        private PostRepository postRepository;

        @Autowired
        PostUtils postUtils;

        @Override
        public Optional<List<CategoryResponse>> getCategories(Map<String, String> allParamsC) {
                List<Category> categories = repository.getCategories(allParamsC)
                                .orElseThrow(() -> new ResourceNotFoundException("not found"));
                List<CategoryResponse> result = new ArrayList<>();
                for (Category c : categories) {
                        List<Post> posts = postRepository
                                        .getPostsByAuthorId(
                                                        Map.ofEntries(entry("categoryId", c.getId()),
                                                                        entry("deleted", "0")),
                                                        "DESC", 1, 10, "created")
                                        .orElseThrow(() -> new ResourceNotFoundException("Not found any post"));
                        if (posts.size() != 0) {
                                PostResponse newestPost = postUtils.generatePostResponse(posts.get(0), "public", "");
                                result.add(new CategoryResponse(c.getId(), c.getCategoryName(),
                                                posts.size(), newestPost));
                        } else {
                                result.add(new CategoryResponse(c.getId(), c.getCategoryName(),
                                                posts.size()));
                        }
                }
                return Optional.of(result);
        }

        @Override
        public Optional<CategoryResponse> getCategoryById(String id) {
                List<Category> categories = repository.getCategories(Map.ofEntries(entry("id", id))).get();
                if (categories.size() == 0)
                        return Optional.of(new CategoryResponse("",
                                        "Deleted category",
                                        0, null));
                Category category = categories.get(0);
                List<String> searchField = new ArrayList<>();
                searchField.add("category_id");
                Map<String, String> allParams = new HashMap<String, String>();
                allParams.put("category_id", category.getId());
                allParams.put("deleted", "0");
                allParams.put("enabled", "0");
                List<Post> posts = postRepository.getPostsByAuthorId(allParams, "DESC", 1,
                                10, "created")
                                .get();
                if (posts.size() != 0) {
                        PostResponse newestPost = postUtils.generatePostResponse(posts.get(0), "public", "");
                        return Optional.of(new CategoryResponse(category.getId(),
                                        category.getCategoryName(),
                                        posts.size(),
                                        newestPost));
                } else {
                        return Optional.of(new CategoryResponse(category.getId(),
                                        category.getCategoryName(),
                                        posts.size()));
                }
        }

        @Override
        public Optional<CategoryResponse> getCategoryDetailById(String id) {
                List<Category> categories = repository.getCategories(Map.ofEntries(entry("id", id))).get();
                if (categories.size() == 0)
                        return Optional.of(new CategoryResponse("",
                                        "Deleted category",
                                        0));
                Category category = categories.get(0);
                return Optional.of(new CategoryResponse(category.getId(),
                                category.getCategoryName(), 0));
        }

        @Override
        public void saveCategory(CategoryRequest categoryRequest) {
                validate(categoryRequest);
                List<Category> categories = repository
                                .getCategories(Map.ofEntries(entry("categoryName", categoryRequest.getName()))).get();
                if (categories.size() != 0) {
                        throw new InvalidRequestException("This name is not available!");
                }
                Category category = new Category(UUID.randomUUID().toString(), categoryRequest.getName());
                repository.saveCategory(category);
        }

        @Override
        public void deleteCategory(String id) {
                List<Category> categories = repository.getCategories(Map.ofEntries(entry("id", id))).get();
                if (categories.size() != 0) {
                        throw new ResourceNotFoundException("This category is not available!");
                }
                repository.deleteCategory(id);
        }

        @Override
        public void updateCategory(CategoryRequest categoryRequest, String id) {
                validate(categoryRequest);
                List<Category> categories = repository.getCategories(Map.ofEntries(entry("id", id))).get();
                if (categories.size() != 0) {
                        throw new ResourceNotFoundException("This category is not available!");
                }
                Category category = categories.get(0);
                category.setCategoryName(categoryRequest.getName());
                repository.saveCategory(category);
        }

}
