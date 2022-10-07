package edunhnil.project.forum.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.jwt.JwtUtils;
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
        @PostMapping(value = "user/update-like-post/{postId}")
        public ResponseEntity<CommonResponse<String>> likePost(HttpServletRequest request, @PathVariable int postId) {
                validateToken(request);
                String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                                JWT_SECRET);
                service.savePostLike(id, postId);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Update like successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PostMapping(value = "user/update-like-comment/{commentId}")
        public ResponseEntity<CommonResponse<String>> likeComment(HttpServletRequest request,
                        @PathVariable int commentId) {
                validateToken(request);
                String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                                JWT_SECRET);
                service.saveCommentLike(id, commentId);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Update like successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }
}
