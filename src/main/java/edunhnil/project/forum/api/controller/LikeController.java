package edunhnil.project.forum.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.service.likeService.LikeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "like")
// @CrossOrigin(origins = "http://localhost:3000/", methods = {
// RequestMethod.OPTIONS, RequestMethod.GET,
// RequestMethod.POST, RequestMethod.PUT,
// RequestMethod.DELETE }, allowedHeaders = "*", allowCredentials = "true")
public class LikeController extends AbstractController<LikeService> {

        @SecurityRequirement(name = "Bearer Authentication")
        @PostMapping(value = "user/update-like-post")
        public ResponseEntity<CommonResponse<String>> likePost(HttpServletRequest request,
                        @RequestParam(required = true) String postId) {
                String id = validateToken(request, false).getLoginId();
                service.savePostLike(id, postId);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Update like successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PostMapping(value = "user/update-like-comment")
        public ResponseEntity<CommonResponse<String>> likeComment(HttpServletRequest request,
                        @RequestParam(required = true) String commentId) {
                String id = validateToken(request, false).getLoginId();
                service.saveCommentLike(id, commentId);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Update like successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }
}
