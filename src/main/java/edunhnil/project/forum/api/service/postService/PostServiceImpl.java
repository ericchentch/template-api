package edunhnil.project.forum.api.service.postService;

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
                if (loginId.compareTo("public") == 0) {
                        allParams.put("deleted", "0");
                }
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
                                page, pageSize, repository.getTotal(allParams)));
        }

        @Override
        public Optional<PostResponse> getPostById(String id, String loginId, boolean skipAccessability) {
                List<Post> posts = repository.getPostsByAuthorId(Map.ofEntries(entry("id", id)), "", 0, 0, "")
                                .get();
                if (posts.size() == 0) {
                        throw new ResourceNotFoundException("Not found post with id: " +
                                        id);
                }
                Post post = posts.get(0);
                post.setView(post.getView() + 1);
                return Optional.of(postUtils.generatePostResponse(post,
                                isPublic(posts.get(0).getAuthorId(), loginId, skipAccessability), loginId));
        }

        @Override
        public void updatePostById(PostRequest req, String id, String loginId, boolean skipAccessability) {
                List<Post> posts = repository.getPostsByAuthorId(Map.ofEntries(entry("id", id)), "", 0, 0, "")
                                .get();
                if (posts.size() == 0) {
                        throw new ResourceNotFoundException("Not found post with id: " +
                                        id);
                }
                validate(req);
                checkAccessability(loginId, id, skipAccessability);
                List<Category> categories = categoryRepository
                                .getCategories(Map.ofEntries(entry("id", req.getCategoryId()))).get();
                if (categories.size() == 0) {
                        throw new ResourceNotFoundException(
                                        "Not found category with id:" + req.getCategoryId());
                }
                Post post = new Post(posts.get(0).getId(), posts.get(0).getAuthorId(), req.getTitle(), req.getContent(),
                                posts.get(0).getView(), req.getCategoryId(), posts.get(0).getCreated(),
                                DateFormat.getCurrentTime(), 0);
                repository.savePost(post);

        }

        @Override
        public void addNewPost(PostRequest postRequest, String authorId) {
                validate(postRequest);
                List<Category> categories = categoryRepository
                                .getCategories(Map.ofEntries(entry("id", postRequest.getCategoryId()))).get();
                if (categories.size() == 0) {
                        throw new ResourceNotFoundException(
                                        "Not found category with id:" + postRequest.getCategoryId());
                }
                Post post = new Post(UUID.randomUUID().toString(), authorId, postRequest.getTitle(),
                                postRequest.getContent(), 0, postRequest.getCategoryId(), DateFormat.getCurrentTime(),
                                null, 0);
                accessabilityRepository
                                .addNewAccessability(new Accessability(null, new ObjectId(authorId), post.getId()));
                repository.savePost(post);
        }

        @Override
        public void deletePostById(String id, String loginId, boolean skipAccessability) {
                List<Post> posts = repository.getPostsByAuthorId(Map.ofEntries(entry("id", id)), "", 0, 0, "")
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
