package edunhnil.project.forum.api.service.postService;

import java.util.Map;
import java.util.Optional;

import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.postDTO.PostRequest;
import edunhnil.project.forum.api.dto.postDTO.PostResponse;

public interface PostService {
        Optional<ListWrapperResponse<PostResponse>> getPostsByAuthorId(Map<String, String> allParams,
                        String keySort, int page,
                        int pageSize, String sortField, String loginId);

        Optional<ListWrapperResponse<PostResponse>> getPublicPost(Map<String, String> allParams,
                        String keySort, int page,
                        int pageSize, String sortField);

        Optional<PostResponse> getPrivatePost(String id, String loginId);

        Optional<PostResponse> getPostById(String id);

        void updatePostById(PostRequest req, String id, String loginId);

        void addNewPost(PostRequest postRequest, String authorId);

        void deleteUserPostById(String id, String loginId);

        void deleteAdminPostById(String id);

        void changeUserEnabled(int input, String id, String loginId);

        void changeAdminEnabled(int input, String id);

}
