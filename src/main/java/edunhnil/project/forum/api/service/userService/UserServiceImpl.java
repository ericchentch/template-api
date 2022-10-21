package edunhnil.project.forum.api.service.userService;

import static java.util.Map.entry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.dao.userRepository.User;
import edunhnil.project.forum.api.dao.userRepository.UserRepository;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.userDTO.ChangePasswordReq;
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
                        String sortField, String loginId, boolean skipAccessability) {
                List<User> users = repository
                                .getUsers(allParams, keySort, page, pageSize, sortField)
                                .get();
                return Optional.of(new ListWrapperResponse<UserResponse>(
                                users.stream()
                                                .map(user -> userUtils.generateUserResponse(user,
                                                                isPublic(user.get_id().toString(), loginId,
                                                                                skipAccessability)))
                                                .collect(Collectors.toList()),
                                page, pageSize, repository.getTotalPage(allParams)));
        }

        @Override
        public Optional<UserResponse> getUserById(String id, String loginId, boolean skipAccessability) {
                List<User> users = repository.getUsers(Map.ofEntries(entry("_id", id)), "", 0, 0, "").get();
                if (users.size() == 0) {
                        throw new ResourceNotFoundException("Not found user!");
                }
                User user = users.get(0);
                return Optional.of(userUtils.generateUserResponse(user,
                                isPublic(user.get_id().toString(), loginId, skipAccessability)));
        }

        @Override
        public void addNewUser(UserRequest userRequest) {
                List<User> users = repository
                                .getUsers(Map.ofEntries(entry("username", userRequest.getUsername())), "", 0, 0, "")
                                .get();
                if (users.size() != 0) {
                        throw new InvalidRequestException("username existed");
                }
                validate(userRequest);
                PasswordValidator.validateNewPassword(userRequest.getPassword());
                String passwordEncode = bCryptPasswordEncoder().encode(userRequest.getPassword());
                userRequest.setPassword(passwordEncode);
                User user = objectMapper.convertValue(userRequest, User.class);
                user.setTokens(null);
                user.setCreated(DateFormat.getCurrentTime());
                user.setModified(null);
                repository.insertAndUpdate(user);
        }

        @Override
        public void updateUserById(UserRequest userRequest, String loginId, String id, boolean skipAccessability) {
                List<User> users = repository.getUsers(Map.ofEntries(entry("_id", id)), "", 0, 0, "").get();
                if (users.size() == 0) {
                        throw new ResourceNotFoundException("Not found user!");
                }
                User oldUser = users.get(0);
                validate(userRequest);
                checkAccessability(loginId, id, skipAccessability);
                PasswordValidator.validateNewPassword(userRequest.getPassword());
                User user = objectMapper.convertValue(userRequest, User.class);
                user.setTokens(oldUser.getTokens());
                user.setCreated(oldUser.getCreated());
                user.setModified(DateFormat.getCurrentTime());
                repository.insertAndUpdate(user);
        }

        @Override
        public void deleteUserById(String id, String loginId, boolean skipAccessability) {
                List<User> users = repository.getUsers(Map.ofEntries(entry("_id", id)), "", 0, 0, "").get();
                if (users.size() == 0) {
                        throw new ResourceNotFoundException("Not found user!");
                }
                User oldUser = users.get(0);
                checkAccessability(loginId, id, skipAccessability);
                User user = objectMapper.convertValue(oldUser, User.class);
                user.setDeleted(1);
                user.setModified(DateFormat.getCurrentTime());
                repository.insertAndUpdate(user);
        }

        @Override
        public void changePasswordById(ChangePasswordReq changePassword, String id) {
                validate(changePassword);
                PasswordValidator.validatePassword(changePassword.getOldPassword());
                PasswordValidator.validateNewPassword(changePassword.getNewPassword());
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                List<User> users = repository.getUsers(Map.ofEntries(entry("_id", id)), "", 0, 0, "").get();
                if (users.size() == 0) {
                        throw new ResourceNotFoundException("Not found user!");
                }
                User user = users.get(0);
                if (!bCryptPasswordEncoder.matches(changePassword.getOldPassword(),
                                user.getPassword())) {
                        throw new InvalidRequestException("Old password does not match!");
                }
                user.setPassword(bCryptPasswordEncoder.encode(changePassword.getNewPassword()));
                user.setModified(DateFormat.getCurrentTime());
                repository.insertAndUpdate(user);
        }

}
