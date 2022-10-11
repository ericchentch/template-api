package edunhnil.project.forum.api.service.commentService;

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
import edunhnil.project.forum.api.utils.DateFormat;

@Service
public class CommentServiceImpl extends AbstractService<CommentRepository>
                implements CommentService {

        @Autowired
        private PostRepository postRepository;

        @Autowired
        private CommentUtils commentUtils;

        @Autowired
        private AccessabilityRepository accessabilityRepository;

        @Override
        public Optional<ListWrapperResponse<CommentResponse>> getComments(Map<String, String> allParams,
                        String keySort, int page,
                        int pageSize,
                        String sortField, String loginId, boolean skipAccessability) {
                List<Comment> comments = repository.getAllComment(allParams, keySort, page, pageSize, sortField).get();
                return Optional
                                .of(new ListWrapperResponse<CommentResponse>(
                                                comments.stream()
                                                                .map(c -> commentUtils.generateCommentResponse(c,
                                                                                isPublic(c.getOwnerId(), loginId,
                                                                                                skipAccessability),
                                                                                loginId))
                                                                .collect(Collectors.toList()),
                                                page, pageSize, repository.getTotal(allParams)));
        }

        @Override
        public void addNewComment(CommentRequest commentRequest, String postId, String ownerId) {
                validate(commentRequest);
                List<Post> posts = postRepository.getPostsByAuthorId(Map.ofEntries(entry("id", postId)), "", 0, 0, "")
                                .get();
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
                accessabilityRepository
                                .addNewAccessability(new Accessability(null, new ObjectId(ownerId), comment.getId()));
                repository.saveComment(comment);
        }

        @Override
        public void editCommentById(CommentRequest commentRequest, String id, String loginId,
                        boolean skipAccessability) {
                validate(commentRequest);
                List<Comment> comments = repository.getAllComment(Map.ofEntries(entry("id", id)), "", 0, 0, "")
                                .get();
                if (comments.size() == 0) {
                        throw new ResourceNotFoundException("Not found comment with id:" + id);
                }
                checkAccessability(loginId, id, skipAccessability);
                Comment comment = objectMapper.convertValue(comments.get(0), Comment.class);
                comment.setContent(commentRequest.getContent());
                comment.setModified(DateFormat.getCurrentTime());
                repository.saveComment(comment);
        }

        @Override
        public void deleteComment(String id, String loginId, boolean skipAccessability) {
                List<Comment> comments = repository.getAllComment(Map.ofEntries(entry("id", id)), "", 0, 0, "")
                                .get();
                if (comments.size() == 0) {
                        throw new ResourceNotFoundException("Not found comment with id:" + id);
                }
                checkAccessability(loginId, id, skipAccessability);
                Comment comment = comments.get(0);
                comment.setDeleted(0);
                comment.setModified(DateFormat.getCurrentTime());
                repository.saveComment(comment);
        }

}
