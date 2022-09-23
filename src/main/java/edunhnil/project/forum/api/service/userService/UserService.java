package edunhnil.project.forum.api.service.userService;

import java.util.Map;
import java.util.Optional;

import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.userDTO.ChangePasswordReq;
import edunhnil.project.forum.api.dto.userDTO.ChangeUsernameReq;
import edunhnil.project.forum.api.dto.userDTO.UserRequest;
import edunhnil.project.forum.api.dto.userDTO.UserResponse;

public interface UserService {
        Optional<ListWrapperResponse<UserResponse>> getUsers(Map<String, String> allParams, String keySort, int page,
                        int pageSize,
                        String sortField);

        Optional<ListWrapperResponse<UserResponse>> getPublicUsers(Map<String, String> allParams, String keySort,
                        int page, int pageSize,
                        String sortField);

        Optional<UserResponse> getUserById(String id);

        Optional<UserResponse> getPublicUserById(String id);

        void addNewUser(UserRequest userRequest);

        void updateUserById(UserRequest userRequest, String id);

        void deleteUserById(String id);

        void changePasswordById(ChangePasswordReq changePassword, String id);

        void changeUsernameById(ChangeUsernameReq changeUsername, String id);

        void change2FAStatus(String id);

}
