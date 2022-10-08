package edunhnil.project.forum.api.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edunhnil.project.forum.api.constant.DateTime;
import edunhnil.project.forum.api.dao.likeRepository.LikeRepository;
import edunhnil.project.forum.api.dao.postRepository.Post;
import edunhnil.project.forum.api.dao.userRepository.User;
import edunhnil.project.forum.api.dao.userRepository.UserRepository;
import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;
import edunhnil.project.forum.api.dto.postDTO.PostResponse;
import edunhnil.project.forum.api.dto.userDTO.UserResponse;
import edunhnil.project.forum.api.service.categoryService.CategoryService;

@Component
public class PostUtils {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private CategoryService categoryService;

        @Autowired
        private LikeRepository likeRepository;

        @Autowired
        private UserUtils userUtils;

        public PostResponse generatePostResponse(Post p, String type, String loginId) {

                Optional<CategoryResponse> categorySerRes = categoryService.getCategoryDetailById(p.getCategoryId());
                CategoryResponse category = categorySerRes.get();

                Map<String, String> allParams = new HashMap<>();
                allParams.put("targetId", Integer.toString(p.getId()));
                allParams.put("type", "post");

                Map<String, String> paramsLiked = new HashMap<>();
                paramsLiked.put("targetId", Integer.toString(p.getId()));
                paramsLiked.put("type", "post");
                paramsLiked.put("ownerId", loginId);
                paramsLiked.put("deleted", "0");

                if (type.compareTo("public") == 0) {
                        return new PostResponse(p.getId(), p.getAuthorId(),
                                        returnUser(p.getAuthorId(), type),
                                        p.getTitle(),
                                        p.getContent(),
                                        p.getView(),
                                        likeRepository.getTotalLike(allParams),
                                        p.getCategoryId(),
                                        category,
                                        DateFormat.toDateString(p.getCreated(),
                                                        DateTime.YYYY_MM_DD),
                                        DateFormat.toDateString(p.getModified(),
                                                        DateTime.YYYY_MM_DD),
                                        likeRepository.getTotalLike(paramsLiked) != 0,
                                        0,
                                        0);
                } else {
                        return new PostResponse(p.getId(), p.getAuthorId(),
                                        returnUser(p.getAuthorId(), type),
                                        p.getTitle(),
                                        p.getContent(),
                                        p.getView(),
                                        likeRepository.getTotalLike(allParams),
                                        p.getCategoryId(),
                                        category,
                                        DateFormat.toDateString(p.getCreated(),
                                                        DateTime.YYYY_MM_DD),
                                        DateFormat.toDateString(p.getModified(),
                                                        DateTime.YYYY_MM_DD),
                                        likeRepository.getTotalLike(paramsLiked) != 0,
                                        p.getEnabled(),
                                        p.getDeleted());
                }
        }

        private UserResponse returnUser(String userId, String type) {
                Optional<User> user = userRepository.getUserById(userId);
                User deletedUser = new User(new ObjectId(userId), "", "", 0, "", "", "Deleted user", "", "", "", "",
                                null,
                                null, "",
                                false,
                                false, null, 0);
                if (user.isEmpty()) {
                        return userUtils.generateUserResponse(deletedUser, type);
                } else {
                        return userUtils.generateUserResponse(user.get(), type);
                }
        }
}
