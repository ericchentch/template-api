package edunhnil.project.forum.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edunhnil.project.forum.api.dto.commentDTO.CommentRequest;
import edunhnil.project.forum.api.dto.commentDTO.CommentResponse;
import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.commonDTO.ValidationResponse;
import edunhnil.project.forum.api.service.commentService.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "comment")
// @CrossOrigin(origins = "http://localhost:3000/", methods = {
// RequestMethod.OPTIONS, RequestMethod.GET,
// RequestMethod.POST, RequestMethod.PUT,
// RequestMethod.DELETE }, allowedHeaders = "*", allowCredentials = "true")

public class CommentController extends AbstractController<CommentService> {

        @SecurityRequirement(name = "Bearer Authentication")
        @GetMapping(value = "get-comment")
        public ResponseEntity<CommonResponse<ListWrapperResponse<CommentResponse>>> getCommentInPost(
                        @RequestParam(required = false, defaultValue = "1") int page,
                        @RequestParam(required = false, defaultValue = "5") int pageSize,
                        @RequestParam(defaultValue = "modified") String sortField,
                        @RequestParam(defaultValue = "asc") String keySort,
                        @RequestParam Map<String, String> allParams, HttpServletRequest request) {
                ValidationResponse result = validateToken(request, true);
                return response(service.getComments(allParams, keySort, page, pageSize, sortField, result.getLoginId(),
                                result.isSkipAccessability()),
                                "Get list of comments successfully!");
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PostMapping(value = "add-new-comment")
        public ResponseEntity<CommonResponse<String>> addNewComment(@RequestBody CommentRequest commentRequest,
                        HttpServletRequest request,
                        @RequestParam(required = true) String postId) {
                ValidationResponse result = validateToken(request, false);
                service.addNewComment(commentRequest, postId, result.getLoginId());
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Add comment successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PutMapping(value = "user/edit-comment")
        public ResponseEntity<CommonResponse<String>> editComment(@RequestBody CommentRequest commentRequest,
                        HttpServletRequest request,
                        @RequestParam(required = true) String commentId) {
                ValidationResponse result = validateToken(request, false);
                service.editCommentById(commentRequest, commentId, result.getLoginId(), result.isSkipAccessability());
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Edit comment successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @DeleteMapping(value = "delete-comment")
        public ResponseEntity<CommonResponse<String>> deleteComment(@RequestParam(required = true) String commentId,
                        HttpServletRequest request) {
                ValidationResponse result = validateToken(request, false);
                service.deleteComment(commentId, result.getLoginId(), result.isSkipAccessability());
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Delete comment successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }
}
