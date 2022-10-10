package edunhnil.project.forum.api.service.postService;

import java.util.HashMap;
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
import edunhnil.project.forum.api.dao.postRepository.Post;
import edunhnil.project.forum.api.dao.postRepository.PostRepository;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.postDTO.PostRequest;
import edunhnil.project.forum.api.dto.postDTO.PostResponse;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.service.AbstractService;
import edunhnil.project.forum.api.utils.DateFormat;
import edunhnil.project.forum.api.utils.PostUtils;

@Service
public class PostServiceImpl extends AbstractService<PostRepository>
                implements PostService {

        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private PostUtils postUtils;

        @Autowired
        private AccessabilityRepository accessabilityRepository;

        @Override
        public Optional<ListWrapperResponse<PostResponse>> getPosts(Map<String, String> allParams,
                        String keySort, int page,
                        int pageSize, String sortField, String loginId, boolean skipAccessability) {
                List<Post> posts = repository
                                .getPostsByAuthorId(allParams, keySort, page, pageSize,
                                                sortField)
                                .orElseThrow(() -> new ResourceNotFoundException("No post"));
                return Optional.of(new ListWrapperResponse<PostResponse>(
                                posts.stream()
                                                .map(p -> postUtils.generatePostResponse(p,
                                                                isPublic(p.getAuthorId(), loginId, skipAccessability),
                                                                loginId))
                                                .collect(Collectors.toList()),
                                page, pageSize, posts.size()));
        }

        @Override
        public Optional<PostResponse> getPostById(String id, String loginId, boolean skipAccessability) {
                Map<String, String> postIds = new HashMap<>();
                postIds.put("id", id);
                List<Post> posts = repository.getPostsByAuthorId(postIds, "", 0, 0, "")
                                .get();
                if (posts.size() == 0) {
                        throw new ResourceNotFoundException("Not found post with id: " +
                                        id);
                }
                Post post = posts.get(0);
                post.setView(post.getView() + 1);
                return Optional.of(postUtils.generatePostResponse(post,
                                isPublic(posts.get(0).getAuthorId(), loginId, skipAccessability), "a"));
        }

        @Override
        public void updatePostById(PostRequest req, String id, String loginId, boolean skipAccessability) {
                Map<String, String> postIds = new HashMap<>();
                postIds.put("id", id);
                List<Post> posts = repository.getPostsByAuthorId(postIds, "", 0, 0, "")
                                .get();
                if (posts.size() == 0) {
                        throw new ResourceNotFoundException("Not found post with id: " +
                                        id);
                }
                validate(req);
                checkAccessability(loginId, id, skipAccessability);
                Post post = posts.get(0);
                Map<String, String> allParams = new HashMap<>();
                allParams.put("id", id);
                List<Category> categories = categoryRepository.getCategories(allParams).get();
                if (categories.size() == 0) {
                        throw new ResourceNotFoundException(
                                        "Not found category with id:" + req.getCategoryId());
                }
                post.setTitle(req.getTitle());
                post.setModified(DateFormat.getCurrentTime());
                post.setContent(req.getContent());
                post.setCategoryId(req.getCategoryId());
                repository.savePost(post);

        }

        @Override
        public void addNewPost(PostRequest postRequest, String authorId) {
                validate(postRequest);
                Map<String, String> allParams = new HashMap<>();
                allParams.put("id", postRequest.getCategoryId());
                List<Category> categories = categoryRepository.getCategories(allParams).get();
                if (categories.size() == 0) {
                        throw new ResourceNotFoundException(
                                        "Not found category with id:" + postRequest.getCategoryId());
                }
                Post post = objectMapper.convertValue(postRequest, Post.class);
                post.setId(UUID.randomUUID().toString());
                post.setAuthorId(authorId);
                post.setCreated(DateFormat.getCurrentTime());
                post.setDeleted(0);
                post.setView(0);
                accessabilityRepository
                                .addNewAccessability(new Accessability(null, new ObjectId(authorId), post.getId()));
                repository.savePost(post);
        }

        @Override
        public void deletePostById(String id, String loginId, boolean skipAccessability) {
                Map<String, String> postIds = new HashMap<>();
                postIds.put("id", id);
                List<Post> posts = repository.getPostsByAuthorId(postIds, "", 0, 0, "")
                                .get();
                if (posts.size() == 0) {
                        throw new ResourceNotFoundException("Not found post with id: " +
                                        id);
                }
                checkAccessability(loginId, id, skipAccessability);
                Post post = posts.get(0);
                post.setModified(DateFormat.getCurrentTime());
                post.setDeleted(1);
                repository.savePost(post);
        }
}
