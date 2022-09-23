package edunhnil.project.forum.api.service.userService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.constant.DateTime;
import edunhnil.project.forum.api.dao.userRepository.User;
import edunhnil.project.forum.api.dao.userRepository.UserRepository;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.userDTO.ChangePasswordReq;
import edunhnil.project.forum.api.dto.userDTO.ChangeUsernameReq;
import edunhnil.project.forum.api.dto.userDTO.UserRequest;
import edunhnil.project.forum.api.dto.userDTO.UserResponse;
import edunhnil.project.forum.api.exception.InvalidRequestException;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.service.AbstractService;
import edunhnil.project.forum.api.utils.DateFormat;
import edunhnil.project.forum.api.utils.PasswordValidator;
import edunhnil.project.forum.api.utils.UserUtils;

@Service
public class UserServiceImpl extends AbstractService<UserRepository>
                implements UserService {

        @Autowired
        private UserUtils userUtils;

        @Override
        public Optional<ListWrapperResponse<UserResponse>> getUsers(Map<String, String> allParams,
                        String keySort, int page,
                        int pageSize,
                        String sortField) {
                List<User> users = repository
                                .getUsers(allParams, keySort, page, pageSize, sortField)
                                .get();
                return Optional.of(new ListWrapperResponse<UserResponse>(
                                users.stream()
                                                .map(user -> userUtils.generateUserResponse(user, ""))
                                                .collect(Collectors.toList()),
                                page, pageSize, 0));
        }

        @Override
        public Optional<ListWrapperResponse<UserResponse>> getPublicUsers(Map<String, String> allParams, String keySort,
                        int page,
                        int pageSize, String sortField) {
                List<User> users = repository
                                .getUsers(allParams, keySort, page, pageSize, sortField)
                                .get();
                return Optional.of(new ListWrapperResponse<UserResponse>(
                                users.stream()
                                                .map(user -> userUtils.generateUserResponse(user, "public"))
                                                .collect(Collectors.toList()),
                                page, pageSize, repository.getTotalPage(allParams)));
        }

        @Override
        public Optional<UserResponse> getUserById(String id) {
                User user = repository.getUserById(id).orElseThrow(
                                () -> new ResourceNotFoundException("Not found user with id: " + id + "!"));
                return Optional.of(userUtils.generateUserResponse(user, ""));
        }

        @Override
        public Optional<UserResponse> getPublicUserById(String id) {
                User user = repository.getUserById(id).orElseThrow(
                                () -> new ResourceNotFoundException("Not found user with id: " + id + "!"));
                return Optional.of(userUtils.generateUserResponse(user, "public"));
        }

        @Override
        public void addNewUser(UserRequest userRequest) {
                if (repository.checkUsername(userRequest.getUsername()).isPresent()) {
                        throw new InvalidRequestException("username existed");
                }
                validate(userRequest);
                PasswordValidator.validateNewPassword(userRequest.getPassword());
                String passwordEncode = bCryptPasswordEncoder().encode(userRequest.getPassword());
                userRequest.setPassword(passwordEncode);
                User user = objectMapper.convertValue(userRequest, User.class);
                user.setToken("");
                user.setCreated(DateFormat.toDateString(DateFormat.getCurrentTime(), DateTime.YYYY_MM_DD));
                user.setModified("");
                repository.insertAndUpdate(user);
        }

        @Override
        public void updateUserById(UserRequest userRequest, String id) {
                User oldUser = repository.getUserById(id).orElseThrow(
                                () -> new ResourceNotFoundException("Not found user with id: " + id + "!"));
                validate(userRequest);
                PasswordValidator.validateNewPassword(userRequest.getPassword());
                User user = objectMapper.convertValue(userRequest, User.class);
                user.setToken(oldUser.getToken());
                user.setCreated(oldUser.getCreated());
                user.setModified(DateFormat.toDateString(DateFormat.getCurrentTime(), DateTime.YYYY_MM_DD));
                repository.insertAndUpdate(user);
        }

        @Override
        public void deleteUserById(String id) {
                User oldUser = repository.getUserById(id).orElseThrow(
                                () -> new ResourceNotFoundException("Not found user with id: " + id + "!"));
                User user = objectMapper.convertValue(oldUser, User.class);
                user.setDeleted(1);
                user.setModified(DateFormat.toDateString(DateFormat.getCurrentTime(), DateTime.YYYY_MM_DD));
                repository.insertAndUpdate(user);
        }

        @Override
        public void changePasswordById(ChangePasswordReq changePassword, String id) {
                validate(changePassword);
                PasswordValidator.validatePassword(changePassword.getOldPassword());
                PasswordValidator.validateNewPassword(changePassword.getNewPassword());
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                User user = repository.getUserById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Not found id: " + id));
                if (!bCryptPasswordEncoder.matches(changePassword.getOldPassword(),
                                user.getPassword())) {
                        throw new InvalidRequestException("Old password does not match!");
                }
                user.setPassword(bCryptPasswordEncoder.encode(changePassword.getNewPassword()));
                user.setModified(DateFormat.toDateString(DateFormat.getCurrentTime(), DateTime.YYYY_MM_DD));
                repository.insertAndUpdate(user);
        }

        @Override
        public void changeUsernameById(ChangeUsernameReq changeUsername, String id) {
                validate(changeUsername);
                User user = repository.getUserById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Not found id: " + id));
                if (user.getUsername().compareTo(changeUsername.getOldUsername()) != 0) {
                        throw new InvalidRequestException("Old username is not match!");
                }
                user.setUsername(changeUsername.getNewUsername());
                user.setModified(DateFormat.toDateString(DateFormat.getCurrentTime(), DateTime.YYYY_MM_DD));
                repository.insertAndUpdate(user);
        }

        @Override
        public void change2FAStatus(String id) {
                User user = repository.getUserById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Not found id: " + id));
                if (user.isVerify2FA()) {
                        user.setVerify2FA(false);
                        repository.insertAndUpdate(user);
                } else {
                        user.setVerify2FA(true);
                        repository.insertAndUpdate(user);
                }

        }

}
