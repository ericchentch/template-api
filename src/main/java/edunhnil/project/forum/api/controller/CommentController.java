package edunhnil.project.forum.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import edunhnil.project.forum.api.jwt.JwtUtils;
import edunhnil.project.forum.api.service.commentService.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "comment")
// @CrossOrigin(origins = "http://localhost:3000/", methods = {
// RequestMethod.OPTIONS, RequestMethod.GET,
// RequestMethod.POST, RequestMethod.PUT,
// RequestMethod.DELETE }, allowedHeaders = "*", allowCredentials = "true")

public class CommentController extends AbstractController<CommentService> {

        @GetMapping(value = "/public/get-comment-in-post")
        public ResponseEntity<CommonResponse<ListWrapperResponse<CommentResponse>>> getCommentInPost(
                        @RequestParam(required = true, defaultValue = "1") int postId,
                        @RequestParam(required = false, defaultValue = "1") int page,
                        @RequestParam(defaultValue = "modified") String sortField,
                        @RequestParam(defaultValue = "asc") String keySort) {
                return response(service.getPublicComment(postId, page, keySort, sortField, "a"),
                                "Get list of comments successfully!");
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @GetMapping(value = "/user/get-comment-in-post")
        public ResponseEntity<CommonResponse<ListWrapperResponse<CommentResponse>>> getCommentInPostUser(
                        @RequestParam(required = true, defaultValue = "1") int postId,
                        @RequestParam(required = false, defaultValue = "1") int page,
                        @RequestParam(defaultValue = "modified") String sortField,
                        @RequestParam(defaultValue = "asc") String keySort, HttpServletRequest request) {
                validateToken(request);
                String loginId = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                                JWT_SECRET);
                return response(
                                service.getPublicComment(postId, page, keySort, sortField, loginId),
                                "Get list of comments successfully!");
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @GetMapping(value = "admin/get-all-comment")
        public ResponseEntity<CommonResponse<ListWrapperResponse<CommentResponse>>> getCommentAdmin(
                        @RequestParam(required = false, defaultValue = "1") int page,
                        @RequestParam(required = false, defaultValue = "10") int pageSize,
                        @RequestParam(defaultValue = "asc") String keySort,
                        @RequestParam(defaultValue = "modified") String sortField,
                        @RequestParam Map<String, String> allParams, HttpServletRequest request) {
                validateToken(request);
                String loginId = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                                JWT_SECRET);
                return response(
                                service.getAdminComment(allParams, keySort, page, pageSize, sortField, loginId),
                                "Get list of comments successfully!");
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PostMapping(value = "user/add-new-comment/{postId}")
        public ResponseEntity<CommonResponse<String>> addNewComment(@RequestBody CommentRequest commentRequest,
                        HttpServletRequest request,
                        @PathVariable int postId) {
                validateToken(request);
                String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request), JWT_SECRET);
                service.addNewComment(commentRequest, postId, id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Add comment successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PutMapping(value = "user/edit-comment/{commentId}")
        public ResponseEntity<CommonResponse<String>> editComment(@RequestBody CommentRequest commentRequest,
                        HttpServletRequest request,
                        @PathVariable int commentId) {
                validateToken(request);
                String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request), JWT_SECRET);
                service.editCommentById(commentRequest, commentId, id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Edit comment successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @DeleteMapping(value = "user/delete-comment/{commentId}")
        public ResponseEntity<CommonResponse<String>> userDeleteComment(@PathVariable int commentId,
                        HttpServletRequest request) {
                validateToken(request);
                String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request), JWT_SECRET);
                service.deleteUserComment(commentId, id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Delete comment successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @DeleteMapping(value = "admin/delete-comment/{commentId}")
        public ResponseEntity<CommonResponse<String>> adminDeleteComment(@PathVariable int commentId,
                        HttpServletRequest request) {
                validateToken(request);
                service.deleteAdminComment(commentId);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Delete comment successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }
}
