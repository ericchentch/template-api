package edunhnil.project.forum.api.service.commentService;

import java.util.Map;
import java.util.Optional;

import edunhnil.project.forum.api.dto.commentDTO.CommentRequest;
import edunhnil.project.forum.api.dto.commentDTO.CommentResponse;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;

public interface CommentService {
        Optional<ListWrapperResponse<CommentResponse>> getPublicComment(int postId, int page, String keySort,
                        String sortField, String loginId);

        Optional<ListWrapperResponse<CommentResponse>> getAdminComment(Map<String, String> allParams,
                        String keySort, int page, int pageSize, String sortField, String loginId);

        void addNewComment(CommentRequest commentRequest, int postId, String ownerId);

        void editCommentById(CommentRequest commentRequest, int id, String loginId);

        void deleteUserComment(int id, String loginId);

        void deleteAdminComment(int id);
}
