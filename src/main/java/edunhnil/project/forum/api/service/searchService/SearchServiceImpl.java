package edunhnil.project.forum.api.service.searchService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;
import edunhnil.project.forum.api.dto.postDTO.PostResponse;
import edunhnil.project.forum.api.dto.searchDTO.SearchResponse;
import edunhnil.project.forum.api.service.AbstractService;
import edunhnil.project.forum.api.service.categoryService.CategoryService;
import edunhnil.project.forum.api.service.postService.PostService;

@Service
public class SearchServiceImpl extends AbstractService<PostService>
        implements SearchService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public Optional<SearchResponse> totalSearch(String keySearch) {
        Map<String, String> postParams = new HashMap<String, String>();
        Map<String, String> categoryParams = new HashMap<String, String>();
        postParams.put("title", keySearch);
        postParams.put("content", keySearch);
        categoryParams.put("categoryName", keySearch);
        List<PostResponse> popularPosts = repository.getPublicPost(postParams,
                "desc", 1, 5, "view").get().getData();
        List<PostResponse> newestPosts = repository.getPublicPost(postParams, "desc",
                1, 5, "created").get().getData();
        List<CategoryResponse> categories = categoryService.getCategories(categoryParams).get();
        return Optional.of(new SearchResponse(popularPosts, newestPosts,
                categories));
    }

}
