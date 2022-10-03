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

import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.postDTO.PostRequest;
import edunhnil.project.forum.api.dto.postDTO.PostResponse;
import edunhnil.project.forum.api.jwt.JwtUtils;
import edunhnil.project.forum.api.service.postService.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "post")
// @CrossOrigin(origins = "http://localhost:3000/", methods = {
// RequestMethod.OPTIONS, RequestMethod.GET,
// RequestMethod.POST, RequestMethod.PUT,
// RequestMethod.DELETE }, allowedHeaders = "*", allowCredentials = "true")
public class PostController extends AbstractController<PostService> {

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "user/getListPost")
    public ResponseEntity<CommonResponse<ListWrapperResponse<PostResponse>>> getUserPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "asc") String keySort,
            @RequestParam(defaultValue = "modified") String sortField,
            @RequestParam Map<String, String> allParams, HttpServletRequest request) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
        validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
        String loginId = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                JWT_SECRET);
        return response(service.getPostsByAuthorId(allParams, keySort, page,
                pageSize, sortField, loginId),
                "Get list of posts successfully!");
    }

    @GetMapping(value = "public/getListPost")
    public ResponseEntity<CommonResponse<ListWrapperResponse<PostResponse>>> getPublicPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "asc") String keySort,
            @RequestParam(defaultValue = "modified") String sortField,
            @RequestParam Map<String, String> allParams) {
        return response(service.getPublicPost(allParams, keySort, page, pageSize,
                sortField),
                "Get list of posts successfully!");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "admin/getPost/{postId}")
    public ResponseEntity<CommonResponse<PostResponse>> getPostAdmin(@PathVariable int postId,
            HttpServletRequest request) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN" };
        validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
        String loginId = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                JWT_SECRET);
        return response(service.getPrivatePost(postId, loginId), "Get post successfully!");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "user/getPost/{postId}")
    public ResponseEntity<CommonResponse<PostResponse>> getPostUser(@PathVariable int postId,
            HttpServletRequest request) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
        validateRole("post", JwtUtils.getJwtFromRequest(request), Integer.toString(postId), roles);
        String loginId = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                JWT_SECRET);
        return response(service.getPrivatePost(postId, loginId), "Get post successfully!");
    }

    @GetMapping(value = "public/getPost/{postId}")
    public ResponseEntity<CommonResponse<PostResponse>> getPostPublic(@PathVariable int postId) {
        return response(service.getPostById(postId), "Get post successfully!");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "user/addNewPost")
    public void addNewPost(@RequestBody PostRequest postRequest,
            HttpServletRequest request) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
        validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
        String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                JWT_SECRET);
        service.addNewPost(postRequest, id);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "user/updatePost/{postId}")
    public void updatePost(@PathVariable int postId, @RequestBody PostRequest postUpdateReq,
            HttpServletRequest request) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
        validateRole("post", JwtUtils.getJwtFromRequest(request), Integer.toString(postId), roles);
        service.updatePostById(postUpdateReq, postId);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "admin/deletePost/{postId}")
    public void deletePostAdmin(@PathVariable int postId, HttpServletRequest request) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN" };
        validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
        service.deletePostById(postId);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "user/deletePost/{postId}")
    public void deletePostUser(@PathVariable int postId, HttpServletRequest request) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
        validateRole("post", JwtUtils.getJwtFromRequest(request), Integer.toString(postId), roles);
        service.deletePostById(postId);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "admin/changeEnabled/{postId}")
    public void changeEnabledAdmin(@PathVariable int postId,
            @RequestParam(required = true, defaultValue = "0") int input,
            HttpServletRequest request) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN" };
        validateRole("role", JwtUtils.getJwtFromRequest(request), "", roles);
        service.changeEnabled(input, postId);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "user/changeEnabled/{postId}")
    public void changeEnabledUser(@PathVariable int postId,
            @RequestParam(required = true, defaultValue = "0") int input,
            HttpServletRequest request) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
        validateRole("post", JwtUtils.getJwtFromRequest(request), Integer.toString(postId), roles);
        service.changeEnabled(input, postId);
    }

}
