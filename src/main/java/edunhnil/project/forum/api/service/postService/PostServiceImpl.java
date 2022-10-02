package edunhnil.project.forum.api.service.postService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        @Override
        public Optional<ListWrapperResponse<PostResponse>> getPublicPost(Map<String, String> allParams, String keySort,
                        int page,
                        int pageSize, String sortField) {
                if (allParams.containsKey("deleted")) {
                        allParams.replace("deleted", "0");
                } else {
                        allParams.put("deleted", "0");
                }
                if (allParams.containsKey("enabled")) {
                        allParams.replace("enabled", "0");
                } else {
                        allParams.put("enabled", "0");
                }
                if (allParams.containsKey("authorId")) {
                        allParams.remove("authorId");
                }
                List<Post> posts = repository
                                .getPostsByAuthorId(allParams, keySort, page, pageSize,
                                                sortField)
                                .get();
                return Optional.of(new ListWrapperResponse<PostResponse>(
                                posts.stream()
                                                .map(p -> postUtils.generatePostResponse(p, "public"))
                                                .collect(Collectors.toList()),
                                page, pageSize,
                                repository.getTotalPage(allParams)));
        }

        @Override
        public Optional<ListWrapperResponse<PostResponse>> getPostsByAuthorId(Map<String, String> allParams,
                        String keySort, int page,
                        int pageSize, String sortField, String authorId) {
                if (allParams.containsKey("authorId")) {
                        allParams.replace("authorId", authorId);

                } else {
                        allParams.put("authorId", authorId);
                }
                List<Post> posts = repository
                                .getPostsByAuthorId(allParams, keySort, page, pageSize,
                                                sortField)
                                .orElseThrow(() -> new ResourceNotFoundException("No post"));

                return Optional.of(new ListWrapperResponse<PostResponse>(
                                posts.stream()
                                                .map(p -> postUtils.generatePostResponse(p, "public"))
                                                .collect(Collectors.toList()),
                                page, pageSize, repository.getTotalPage(allParams)));
        }

        @Override
        public Optional<PostResponse> getPrivatePost(int id) {
                Map<String, String> postIds = new HashMap<>();
                postIds.put("id", Integer.toString(id));
                List<Post> posts = repository.getPostsByAuthorId(postIds, "", 0, 0, "")
                                .get();
                if (posts.size() == 0) {
                        throw new ResourceNotFoundException("Not found post with id: " +
                                        id);
                }
                return Optional.of(postUtils.generatePostResponse(posts.get(0), ""));
        }

        @Override
        public Optional<PostResponse> getPostById(int id) {
                Map<String, String> postIds = new HashMap<>();
                postIds.put("id", Integer.toString(id));
                List<Post> posts = repository.getPostsByAuthorId(postIds, "", 0, 0, "")
                                .get();
                if (posts.size() == 0) {
                        throw new ResourceNotFoundException("Not found post with id: " +
                                        id);
                }
                Post post = posts.get(0);
                post.setView(post.getView() + 1);
                return Optional.of(postUtils.generatePostResponse(post, "public"));
        }

        @Override
        public void updatePostById(PostRequest req, int id) {
                Map<String, String> postIds = new HashMap<>();
                postIds.put("id", Integer.toString(id));
                List<Post> posts = repository.getPostsByAuthorId(postIds, "", 0, 0, "")
                                .get();
                if (posts.size() == 0) {
                        throw new ResourceNotFoundException("Not found post with id: " +
                                        id);
                }
                Post post = posts.get(0);
                validate(req);
                Map<String, String> allParams = new HashMap<>();
                allParams.put("id", Integer.toString(id));
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
                allParams.put("id", Integer.toString(postRequest.getCategoryId()));
                List<Category> categories = categoryRepository.getCategories(allParams).get();
                if (categories.size() == 0) {
                        throw new ResourceNotFoundException(
                                        "Not found category with id:" + postRequest.getCategoryId());
                }
                Post post = objectMapper.convertValue(postRequest, Post.class);
                post.setAuthorId(authorId);
                post.setCreated(DateFormat.getCurrentTime());
                post.setDeleted(0);
                post.setView(0);
                repository.savePost(post);
        }

        @Override
        public void deletePostById(int id) {
                Map<String, String> postIds = new HashMap<>();
                postIds.put("id", Integer.toString(id));
                List<Post> posts = repository.getPostsByAuthorId(postIds, "", 0, 0, "")
                                .get();
                if (posts.size() == 0) {
                        throw new ResourceNotFoundException("Not found post with id: " +
                                        id);
                }
                Post post = posts.get(0);
                post.setModified(DateFormat.getCurrentTime());
                post.setDeleted(1);
                repository.savePost(post);
        }

        @Override
        public void changeEnabled(int input, int id) {
                Map<String, String> postIds = new HashMap<>();
                postIds.put("id", Integer.toString(id));
                List<Post> posts = repository.getPostsByAuthorId(postIds, "", 0, 0, "")
                                .get();
                if (posts.size() == 0) {
                        throw new ResourceNotFoundException("Not found post with id: " +
                                        id);
                }
                Post post = posts.get(0);
                post.setEnabled(input);
                post.setModified(DateFormat.getCurrentTime());
                repository.savePost(post);
        }

}
