package edunhnil.project.forum.api.service.commentService;

import java.util.Map;
import java.util.Optional;

import edunhnil.project.forum.api.dto.commentDTO.CommentRequest;
import edunhnil.project.forum.api.dto.commentDTO.CommentResponse;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;

public interface CommentService {

        Optional<ListWrapperResponse<CommentResponse>> getComments(Map<String, String> allParams,
                        String keySort, int page, int pageSize, String sortField, String loginId,
                        boolean skipAccessability);

        void addNewComment(CommentRequest commentRequest, String postId, String ownerId);

        void editCommentById(CommentRequest commentRequest, String id, String loginId, boolean skipAccessability);

        void deleteComment(String id, String loginId, boolean skipAccessability);

}
