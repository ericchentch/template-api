package edunhnil.project.forum.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping(value = "user/update-post/{postId}")
    public void likePost(HttpServletRequest request, @PathVariable int postId) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
        validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
        String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                JWT_SECRET);
        service.savePostLike(id, postId);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "user/update-comment/{commentId}")
    public void likeComment(HttpServletRequest request, @PathVariable int commentId) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
        validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
        String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                JWT_SECRET);
        service.savePostLike(id, commentId);
    }
}
