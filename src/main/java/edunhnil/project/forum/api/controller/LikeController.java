package edunhnil.project.forum.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(value = "public/getNumberPostLike/{postId}")
    public ResponseEntity<CommonResponse<Integer>> getNumberPostLike(@PathVariable int postId) {
        return response(service.getTotalPostLike(postId), "Get likes of post successfully!");
    }

    @GetMapping(value = "public/getNumberCommentLike/{commentId}")
    public ResponseEntity<CommonResponse<Integer>> getNumberCommentLike(@PathVariable int commentId) {
        return response(service.getTotalCommentLike(commentId), "Get likes of comment successfully");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "user/commentStatusLikeBtn/{commentId}")
    public ResponseEntity<CommonResponse<Boolean>> commentStatusLikeBtn(HttpServletRequest request,
            @PathVariable int commentId) {
        validateToken(request);
        String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                JWT_SECRET);
        return response(service.checkLikeComment(commentId, id), "Get like status successfully!");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "user/postStatusLikeBtn/{postId}")
    public ResponseEntity<CommonResponse<Boolean>> postStatusLikeBtn(HttpServletRequest request,
            @PathVariable int postId) {
        validateToken(request);
        String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                JWT_SECRET);
        return response(service.checkLikePost(postId, id), "Get like status successfully!");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "user/likePost/{postId}")
    public void likePost(HttpServletRequest request, @PathVariable int postId) {
        validateToken(request);
        String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                JWT_SECRET);
        service.addNewPostLike(id, postId);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "user/likeComment/{commentId}")
    public void likeComment(HttpServletRequest request, @PathVariable int commentId) {
        validateToken(request);
        String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                JWT_SECRET);
        service.addNewCommentLike(id, commentId);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "user/discardLikeComment/{commentId}")
    public void discardLikeComment(HttpServletRequest request, @PathVariable int commentId) {
        validateToken(request);
        String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                JWT_SECRET);
        service.hideCommentLike(id, commentId);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "user/discardLikePost/{postId}")
    public void discardLikePost(HttpServletRequest request, @PathVariable int postId) {
        validateToken(request);
        String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                JWT_SECRET);
        service.hideLikePost(id, postId);
    }
}
