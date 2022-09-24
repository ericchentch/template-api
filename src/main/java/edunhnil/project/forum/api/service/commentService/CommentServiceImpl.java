package edunhnil.project.forum.api.service.commentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.dao.commentRepository.Comment;
import edunhnil.project.forum.api.dao.commentRepository.CommentRepository;
import edunhnil.project.forum.api.dao.postRepository.Post;
import edunhnil.project.forum.api.dao.postRepository.PostRepository;
import edunhnil.project.forum.api.dto.commentDTO.CommentRequest;
import edunhnil.project.forum.api.dto.commentDTO.CommentResponse;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.service.AbstractService;
import edunhnil.project.forum.api.utils.CommentUtils;

@Service
public class CommentServiceImpl extends AbstractService<CommentRepository>
                implements CommentService {

        @Autowired
        private PostRepository postRepository;

        @Autowired
        private CommentUtils commentUtils;

        @Override
        public Optional<ListWrapperResponse<CommentResponse>> getPublicComment(int postId, int page,
                        String keySort, String sortField) {
                Map<String, String> allParams = new HashMap<String, String>();
                allParams.put("postId", Integer.toString(postId));
                List<Comment> comments = repository
                                .getAllComment(allParams, keySort, page, 5, sortField)
                                .get();
                return Optional.of(new ListWrapperResponse<CommentResponse>(comments.stream()
                                .map(c -> commentUtils.generateCommentResponse(c, "public"))
                                .collect(Collectors.toList()), page, 5,
                                repository.getTotalCommentPost(postId)));
        }

        @Override
        public Optional<ListWrapperResponse<CommentResponse>> getAdminComment(Map<String, String> allParams,
                        String keySort, int page,
                        int pageSize,
                        String sortField) {
                List<Comment> comments = repository
                                .getAllComment(allParams, keySort, page, pageSize, sortField)
                                .orElseThrow(() -> new ResourceNotFoundException("No comment"));
                return Optional
                                .of(new ListWrapperResponse<CommentResponse>(
                                                comments.stream()
                                                                .map(c -> commentUtils.generateCommentResponse(c,
                                                                                ""))
                                                                .collect(Collectors.toList()),
                                                page, pageSize,
                                                repository.getTotalCommentAdmin(allParams)));
        }

        @Override
        public void addNewComment(CommentRequest commentRequest, int postId, String ownerId) {
                validate(commentRequest);
                repository.resetId();
                Map<String, String> postIds = new HashMap<>();
                postIds.put("id", Integer.toString(postId));
                List<Post> posts = postRepository.getPostsByAuthorId(postIds, "", 0, 0, "").get();
                if (posts.size() == 0) {
                        throw new ResourceNotFoundException("Not found post with id: " +
                                        postId);
                }
                Comment comment = objectMapper.convertValue(commentRequest, Comment.class);
                comment.setPostId(postId);
                comment.setOwnerId(ownerId);
                repository.addNewComment(comment);
        }

        @Override
        public void editCommentById(CommentRequest commentRequest, int id) {
                validate(commentRequest);
                Map<String, String> allParams = new HashMap<>();
                allParams.put("id", Integer.toString(id));
                List<Comment> comments = repository.getAllComment(allParams, "", 0, 0, "")
                                .get();
                if (comments.size() == 0) {
                        throw new ResourceNotFoundException("Not found comment with id:" + id);
                }
                repository.editCommentById(objectMapper.convertValue(commentRequest,
                                Comment.class), id);
        }

        @Override
        public void deleteComment(int id) {
                Map<String, String> allParams = new HashMap<>();
                allParams.put("id", Integer.toString(id));
                List<Comment> comments = repository.getAllComment(allParams, "", 0, 0, "")
                                .get();
                if (comments.size() == 0) {
                        throw new ResourceNotFoundException("Not found comment with id:" + id);
                }
                repository.deleteCommentById(id);
        }

}
