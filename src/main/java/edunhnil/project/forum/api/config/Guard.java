package edunhnil.project.forum.api.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import edunhnil.project.forum.api.dao.commentRepository.Comment;
import edunhnil.project.forum.api.dao.commentRepository.CommentRepository;
import edunhnil.project.forum.api.dao.postRepository.Post;
import edunhnil.project.forum.api.dao.postRepository.PostRepository;
import edunhnil.project.forum.api.dao.userRepository.User;
import edunhnil.project.forum.api.dao.userRepository.UserRepository;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.jwt.JwtTokenProvider;
import edunhnil.project.forum.api.jwt.JwtUtils;

@Component
public class Guard {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Value("${spring.key.jwt}")
    private String JWT_SECRET;

    public boolean checkAuthorId(HttpServletRequest request, int postId) {
        String token = JwtUtils.getJwtFromRequest(request);
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(request)) {
            String id = JwtUtils.getUserIdFromJwt(token, JWT_SECRET);
            Optional<User> user = userRepository.getUserById(id);
            if (user.isEmpty())
                return false;
            Map<String, String> postIds = new HashMap<>();
            postIds.put("id", Integer.toString(postId));
            List<Post> posts = postRepository.getPostsByAuthorId(postIds, "", 0, 0, "")
                    .get();
            if (posts.size() == 0) {
                return false;
            }
            return user.get().get_id().toString().compareTo(posts.get(0).getAuthorId()) == 0;
        }
        return true;
    }

    public boolean checkCommentId(HttpServletRequest request, int commentId) {
        String token = JwtUtils.getJwtFromRequest(request);
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(request)) {
            String id = JwtUtils.getUserIdFromJwt(token, JWT_SECRET);
            Optional<User> user = userRepository.getUserById(id);
            if (user.isEmpty())
                return false;
            Map<String, String> allParams = new HashMap<>();
            allParams.put("id", Integer.toString(commentId));
            List<Comment> comments = commentRepository.getAllComment(allParams, "", 0, 0, "")
                    .get();
            if (comments.size() == 0) {
                return false;
            }
            return user.get().get_id().toString().compareTo(comments.get(0).getOwnerId()) == 0;
        }
        return true;
    }

    public boolean checkRoleById(HttpServletRequest request, String[] roles) {
        String token = JwtUtils.getJwtFromRequest(request);
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(request)) {
            String id = JwtUtils.getUserIdFromJwt(token, JWT_SECRET);
            User user = userRepository.getUserById(id).orElseThrow(
                    () -> new ResourceNotFoundException("User is deactivated or unauthorized!"));
            for (int i = 0; i < roles.length; i++) {
                if (roles[i].compareTo(user.getRole()) == 0)
                    return true;
            }
            return false;
        }
        return true;
    }

}
