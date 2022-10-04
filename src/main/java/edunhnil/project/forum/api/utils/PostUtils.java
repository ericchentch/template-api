package edunhnil.project.forum.api.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edunhnil.project.forum.api.constant.DateTime;
import edunhnil.project.forum.api.dao.likeRepository.LikeRepository;
import edunhnil.project.forum.api.dao.postRepository.Post;
import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;
import edunhnil.project.forum.api.dto.postDTO.PostResponse;
import edunhnil.project.forum.api.dto.userDTO.UserResponse;
import edunhnil.project.forum.api.service.categoryService.CategoryService;
import edunhnil.project.forum.api.service.userService.UserService;

@Component
public class PostUtils {

        @Autowired
        private UserService userService;

        @Autowired
        private CategoryService categoryService;

        @Autowired
        private LikeRepository likeRepository;

        public PostResponse generatePostResponse(Post p, String type, String loginId) {

                UserResponse author = userService.getPublicUserById(p.getAuthorId()).get();
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
                                        author,
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
                                        author,
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
}
