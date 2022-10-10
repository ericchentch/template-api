package edunhnil.project.forum.api.service.postService;

import java.util.Map;
import java.util.Optional;

import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.postDTO.PostRequest;
import edunhnil.project.forum.api.dto.postDTO.PostResponse;

public interface PostService {
        Optional<ListWrapperResponse<PostResponse>> getPosts(Map<String, String> allParams,
                        String keySort, int page,
                        int pageSize, String sortField, String loginId, boolean skipAccessability);

        Optional<PostResponse> getPostById(String id, String loginId, boolean skipAccessability);

        void updatePostById(PostRequest req, String id, String loginId, boolean skipAccessability);

        void addNewPost(PostRequest postRequest, String authorId);

        void deletePostById(String id, String loginId, boolean skipAccessability);

}
