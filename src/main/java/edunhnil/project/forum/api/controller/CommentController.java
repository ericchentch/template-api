package edunhnil.project.forum.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping(value = "/public/getCommentInPost")
    public ResponseEntity<CommonResponse<ListWrapperResponse<CommentResponse>>> getCommentInPost(
            @RequestParam(required = true, defaultValue = "1") int postId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(defaultValue = "modified") String sortField,
            @RequestParam(defaultValue = "asc") String keySort) {
        return response(service.getPublicComment(postId, page, keySort, sortField),
                "Get list of comments successfully!");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "admin/getAllComment")
    public ResponseEntity<CommonResponse<ListWrapperResponse<CommentResponse>>> getCommentAdmin(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "asc") String keySort,
            @RequestParam(defaultValue = "modified") String sortField,
            @RequestParam Map<String, String> allParams,
            HttpServletRequest request) {
        validateToken(request);
        return response(service.getAdminComment(allParams, keySort, page, pageSize,
                sortField),
                "Get list of comments successfully!");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "user/addNewComment/{postId}")
    public void addNewComment(@RequestBody CommentRequest commentRequest,
            HttpServletRequest request,
            @PathVariable int postId) {
        validateToken(request);
        String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                JWT_SECRET);
        service.addNewComment(commentRequest, postId, id);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "user/editComment/{commentId}")
    public void editComment(@RequestBody CommentRequest commentRequest,
            HttpServletRequest request,
            @PathVariable int commentId) {
        validateToken(request);
        service.editCommentById(commentRequest, commentId);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "user/deleteComment/{commentId}")
    public void userDeleteComment(@PathVariable int commentId, HttpServletRequest request) {
        validateToken(request);
        service.deleteComment(commentId);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "admin/deleteComment/{commentId}")
    public void adminDeleteComment(@PathVariable int commentId,
            HttpServletRequest request) {
        validateToken(request);
        service.deleteComment(commentId);
    }
}
