package edunhnil.project.forum.api.service.userService;

import java.util.Map;
import java.util.Optional;

import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.userDTO.ChangePasswordReq;
import edunhnil.project.forum.api.dto.userDTO.UserRequest;
import edunhnil.project.forum.api.dto.userDTO.UserResponse;

public interface UserService {
        Optional<ListWrapperResponse<UserResponse>> getUsers(Map<String, String> allParams, String keySort, int page,
                        int pageSize,
                        String sortField, String loginId, boolean skipAccessability);

        Optional<UserResponse> getUserById(String id, String loginId, boolean skipAccessability);

        void addNewUser(UserRequest userRequest);

        void updateUserById(UserRequest userRequest, String loginId, String id, boolean skipAccessability);

        void deleteUserById(String id, String loginId, boolean skipAccessability);

        void changePasswordById(ChangePasswordReq changePassword, String id);

}
