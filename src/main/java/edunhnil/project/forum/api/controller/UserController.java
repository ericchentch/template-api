package edunhnil.project.forum.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.commonDTO.ValidationResponse;
import edunhnil.project.forum.api.dto.userDTO.ChangePasswordReq;
import edunhnil.project.forum.api.dto.userDTO.UserRequest;
import edunhnil.project.forum.api.dto.userDTO.UserResponse;
import edunhnil.project.forum.api.service.userService.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "account")
// @CrossOrigin(origins = "http://localhost:3000/", methods = {
// RequestMethod.OPTIONS, RequestMethod.GET,
// RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }, allowedHeaders
// = "*", allowCredentials = "true")
public class UserController extends AbstractController<UserService> {

        @SecurityRequirement(name = "Bearer Authentication")
        @GetMapping(value = "get-list-user")
        public ResponseEntity<CommonResponse<ListWrapperResponse<UserResponse>>> getUsers(
                        @RequestParam(required = false, defaultValue = "1") int page,
                        @RequestParam(required = false, defaultValue = "10") int pageSize,
                        @RequestParam Map<String, String> allParams,
                        @RequestParam(defaultValue = "asc") String keySort,
                        @RequestParam(defaultValue = "modified") String sortField, HttpServletRequest request) {
                ValidationResponse result = validateToken(request, true);
                return response(service.getUsers(allParams, keySort, page, pageSize, sortField, result.getLoginId(),
                                result.isSkipAccessability()),
                                "Get list of users successfully!");
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @GetMapping(value = "get-detail-user")
        public ResponseEntity<CommonResponse<UserResponse>> getUserById(HttpServletRequest request,
                        @RequestParam(required = true) String id) {
                ValidationResponse result = validateToken(request, true);
                return response(service.getUserById(id, result.getLoginId(), result.isSkipAccessability()),
                                "Get user successfully!");
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PostMapping(value = "admin/add-new-user")
        public ResponseEntity<CommonResponse<String>> addNewUser(@RequestBody UserRequest userRequest,
                        HttpServletRequest request) {
                validateToken(request, false);
                service.addNewUser(userRequest);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Add user successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PutMapping(value = "update-user")
        public ResponseEntity<CommonResponse<String>> updateUser(@RequestBody UserRequest userRequest,
                        @RequestParam(required = true) String userId,
                        HttpServletRequest request) {
                ValidationResponse result = validateToken(request, false);
                service.updateUserById(userRequest, result.getLoginId(), userId, result.isSkipAccessability());
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Update user successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @DeleteMapping(value = "delete-user")
        public ResponseEntity<CommonResponse<String>> deleteUser(@RequestParam(required = true) String userId,
                        HttpServletRequest request) {
                ValidationResponse result = validateToken(request, false);
                service.deleteUserById(userId, result.getLoginId(), result.isSkipAccessability());
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Delete successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PutMapping(value = "change-password")
        public ResponseEntity<CommonResponse<String>> changePassword(@RequestBody ChangePasswordReq changePassword,
                        HttpServletRequest request) {
                String id = validateToken(request, false).getLoginId();
                service.changePasswordById(changePassword, id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Change password successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }
}
