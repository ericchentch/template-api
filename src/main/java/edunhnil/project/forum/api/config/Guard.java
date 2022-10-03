package edunhnil.project.forum.api.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import edunhnil.project.forum.api.dao.commentRepository.Comment;
import edunhnil.project.forum.api.dao.commentRepository.CommentRepository;
import edunhnil.project.forum.api.dao.postRepository.Post;
import edunhnil.project.forum.api.dao.postRepository.PostRepository;
import edunhnil.project.forum.api.dao.userRepository.User;
import edunhnil.project.forum.api.dao.userRepository.UserRepository;
import edunhnil.project.forum.api.jwt.JwtUtils;

@Component
public class Guard {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Value("${spring.key.jwt}")
    private String JWT_SECRET;

    public boolean checkAuthorId(String token, int postId) {
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

    public boolean checkCommentId(String token, int commentId) {
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

    public boolean checkRoleById(String token, String[] roles) {
        String id = JwtUtils.getUserIdFromJwt(token, JWT_SECRET);
        Optional<User> user = userRepository.getUserById(id);
        if (user.isEmpty())
            return false;
        for (int i = 0; i < roles.length; i++) {
            if (roles[i].compareTo(user.get().getRole()) == 0)
                return true;
        }
        return false;

    }

}
