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

import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.commonDTO.ValidationResponse;
import edunhnil.project.forum.api.dto.postDTO.PostRequest;
import edunhnil.project.forum.api.dto.postDTO.PostResponse;
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
        @GetMapping(value = "get-list-post")
        public ResponseEntity<CommonResponse<ListWrapperResponse<PostResponse>>> getUserPosts(
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int pageSize,
                        @RequestParam(defaultValue = "asc") String keySort,
                        @RequestParam(defaultValue = "modified") String sortField,
                        @RequestParam Map<String, String> allParams, HttpServletRequest request) {
                ValidationResponse result = validateToken(request, true);
                return response(service.getPosts(allParams, keySort, page,
                                pageSize, sortField, result.getLoginId(), result.isSkipAccessability()),
                                "Get list of posts successfully!");
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @GetMapping(value = "get-post-detail")
        public ResponseEntity<CommonResponse<PostResponse>> getPost(@RequestParam(required = true) String postId,
                        HttpServletRequest request) {
                ValidationResponse result = validateToken(request, true);
                return response(service.getPostById(postId, result.getLoginId(), result.isSkipAccessability()),
                                "Get post successfully!");
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PostMapping(value = "add-new-post")
        public ResponseEntity<CommonResponse<String>> addNewPost(@RequestBody PostRequest postRequest,
                        HttpServletRequest request) {
                String id = validateToken(request, false).getLoginId();
                service.addNewPost(postRequest, id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Add new post successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PutMapping(value = "update-post")
        public ResponseEntity<CommonResponse<String>> updatePost(@RequestParam(required = true) String postId,
                        @RequestBody PostRequest postUpdateReq,
                        HttpServletRequest request) {
                ValidationResponse result = validateToken(request, false);
                service.updatePostById(postUpdateReq, postId, result.getLoginId(), result.isSkipAccessability());
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Update post successfully!",
                                                HttpStatus.OK.value()),
                                null, HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @DeleteMapping(value = "delete-post")
        public ResponseEntity<CommonResponse<String>> deletePost(@RequestParam(required = true) String postId,
                        HttpServletRequest request) {
                ValidationResponse result = validateToken(request, false);
                service.deletePostById(postId, result.getLoginId(), result.isSkipAccessability());
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Delete post successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

}
