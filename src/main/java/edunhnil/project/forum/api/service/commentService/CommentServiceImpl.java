package edunhnil.project.forum.api.service.commentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
import edunhnil.project.forum.api.exception.ForbiddenException;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.service.AbstractService;
import edunhnil.project.forum.api.utils.CommentUtils;
import edunhnil.project.forum.api.utils.DateFormat;

@Service
public class CommentServiceImpl extends AbstractService<CommentRepository>
                implements CommentService {

        @Autowired
        private PostRepository postRepository;

        @Autowired
        private CommentUtils commentUtils;

        @Override
        public Optional<ListWrapperResponse<CommentResponse>> getPublicComment(String postId, int page,
                        String keySort, String sortField, String loginId) {
                Map<String, String> allParams = new HashMap<String, String>();
                allParams.put("postId", postId);
                List<Comment> comments = repository
                                .getAllComment(allParams, keySort, page, 5, sortField)
                                .get();
                return Optional.of(new ListWrapperResponse<CommentResponse>(comments.stream()
                                .map(c -> commentUtils.generateCommentResponse(c, "public", loginId))
                                .collect(Collectors.toList()), page, 5,
                                comments.size()));
        }

        @Override
        public Optional<ListWrapperResponse<CommentResponse>> getAdminComment(Map<String, String> allParams,
                        String keySort, int page,
                        int pageSize,
                        String sortField, String loginId) {
                List<Comment> comments = repository
                                .getAllComment(allParams, keySort, page, pageSize, sortField)
                                .orElseThrow(() -> new ResourceNotFoundException("No comment"));
                return Optional
                                .of(new ListWrapperResponse<CommentResponse>(
                                                comments.stream()
                                                                .map(c -> commentUtils.generateCommentResponse(c,
                                                                                "", loginId))
                                                                .collect(Collectors.toList()),
                                                page, pageSize, comments.size()));
        }

        @Override
        public void addNewComment(CommentRequest commentRequest, String postId, String ownerId) {
                validate(commentRequest);
                Map<String, String> postIds = new HashMap<>();
                postIds.put("id", postId);
                List<Post> posts = postRepository.getPostsByAuthorId(postIds, "", 0, 0, "").get();
                if (posts.size() == 0) {
                        throw new ResourceNotFoundException("Not found post with id: " +
                                        postId);
                }
                Comment comment = objectMapper.convertValue(commentRequest, Comment.class);
                comment.setId(UUID.randomUUID().toString());
                comment.setPostId(postId);
                comment.setOwnerId(ownerId);
                comment.setCreated(DateFormat.getCurrentTime());
                comment.setDeleted(0);
                repository.saveComment(comment);
        }

        @Override
        public void editCommentById(CommentRequest commentRequest, String id, String loginId) {
                validate(commentRequest);
                Map<String, String> allParams = new HashMap<>();
                allParams.put("id", id);
                List<Comment> comments = repository.getAllComment(allParams, "", 0, 0, "")
                                .get();
                if (comments.size() == 0) {
                        throw new ResourceNotFoundException("Not found comment with id:" + id);
                }
                Comment comment = objectMapper.convertValue(comments.get(0), Comment.class);
                if (comment.getOwnerId().compareTo(loginId) != 0) {
                        throw new ForbiddenException("Access denied!");
                }
                comment.setContent(commentRequest.getContent());
                comment.setModified(DateFormat.getCurrentTime());
                repository.saveComment(comment);
        }

        @Override
        public void deleteUserComment(String id, String loginId) {
                Map<String, String> allParams = new HashMap<>();
                allParams.put("id", id);
                List<Comment> comments = repository.getAllComment(allParams, "", 0, 0, "")
                                .get();
                if (comments.size() == 0) {
                        throw new ResourceNotFoundException("Not found comment with id:" + id);
                }
                Comment comment = comments.get(0);
                if (comment.getOwnerId().compareTo(loginId) != 0) {
                        throw new ForbiddenException("Access denied!");
                }
                comment.setDeleted(0);
                comment.setModified(DateFormat.getCurrentTime());
                repository.saveComment(comment);
        }

        @Override
        public void deleteAdminComment(String id) {
                Map<String, String> allParams = new HashMap<>();
                allParams.put("id", id);
                List<Comment> comments = repository.getAllComment(allParams, "", 0, 0, "")
                                .get();
                if (comments.size() == 0) {
                        throw new ResourceNotFoundException("Not found comment with id:" + id);
                }
                Comment comment = comments.get(0);
                comment.setDeleted(0);
                comment.setModified(DateFormat.getCurrentTime());
                repository.saveComment(comment);
        }

}
