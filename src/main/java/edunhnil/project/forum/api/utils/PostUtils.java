package edunhnil.project.forum.api.utils;

import static java.util.Map.entry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edunhnil.project.forum.api.constant.DateTime;
import edunhnil.project.forum.api.dao.likeRepository.LikeRepository;
import edunhnil.project.forum.api.dao.postRepository.Post;
import edunhnil.project.forum.api.dao.userRepository.User;
import edunhnil.project.forum.api.dao.userRepository.UserRepository;
import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;
import edunhnil.project.forum.api.dto.commonDTO.UserInformation;
import edunhnil.project.forum.api.dto.postDTO.PostResponse;
import edunhnil.project.forum.api.service.categoryService.CategoryService;

@Component
public class PostUtils {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private CategoryService categoryService;

        @Autowired
        private LikeRepository likeRepository;

        public PostResponse generatePostResponse(Post p, String type, String loginId) {

                Optional<CategoryResponse> categorySerRes = categoryService.getCategoryById(p.getCategoryId());
                CategoryResponse category = categorySerRes.get();

                Map<String, String> allParams = new HashMap<>();
                allParams.put("targetId", p.getId());
                allParams.put("type", "post");

                Map<String, String> paramsLiked = new HashMap<>();
                paramsLiked.put("targetId", p.getId());
                paramsLiked.put("type", "post");
                paramsLiked.put("ownerId", loginId);

                if (type.compareTo("public") == 0) {
                        allParams.put("deleted", "0");
                        paramsLiked.put("deleted", "0");
                }

                if (type.compareTo("public") == 0) {
                        return new PostResponse(p.getId(),
                                        returnUser(p.getAuthorId()),
                                        p.getTitle(),
                                        p.getContent(),
                                        p.getView(),
                                        likeRepository.getTotalLike(allParams),
                                        category,
                                        DateFormat.toDateString(p.getCreated(),
                                                        DateTime.YYYY_MM_DD),
                                        DateFormat.toDateString(p.getModified(),
                                                        DateTime.YYYY_MM_DD),
                                        false,
                                        0);
                } else {
                        return new PostResponse(p.getId(),
                                        returnUser(p.getAuthorId()),
                                        p.getTitle(),
                                        p.getContent(),
                                        p.getView(),
                                        likeRepository.getTotalLike(allParams),
                                        category,
                                        DateFormat.toDateString(p.getCreated(),
                                                        DateTime.YYYY_MM_DD),
                                        DateFormat.toDateString(p.getModified(),
                                                        DateTime.YYYY_MM_DD),
                                        likeRepository.getTotalLike(paramsLiked) != 0,
                                        p.getDeleted());
                }
        }

        private UserInformation returnUser(String userId) {
                List<User> users = userRepository.getUsers(Map.ofEntries(entry("_id", userId)), "", 0, 0, "").get();
                if (users.size() == 0) {
                        return new UserInformation("", "", "Deleted user");
                } else {
                        return new UserInformation(userId, users.get(0).getLastName(), users.get(0).getFirstName());
                }
        }
}
