package edunhnil.project.forum.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.userDTO.ChangePasswordReq;
import edunhnil.project.forum.api.dto.userDTO.ChangeUsernameReq;
import edunhnil.project.forum.api.dto.userDTO.UserRequest;
import edunhnil.project.forum.api.dto.userDTO.UserResponse;
import edunhnil.project.forum.api.jwt.JwtUtils;
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
        @GetMapping(value = "admin/getUsers")
        public ResponseEntity<CommonResponse<ListWrapperResponse<UserResponse>>> getUsers(
                        @RequestParam(required = false, defaultValue = "1") int page,
                        @RequestParam(required = false, defaultValue = "10") int pageSize,
                        @RequestParam Map<String, String> allParams,
                        @RequestParam(defaultValue = "asc") String keySort,
                        @RequestParam(defaultValue = "modified") String sortField, HttpServletRequest request) {
                validateToken(request);
                String[] roles = { "ROLE_ADMIN" };
                validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
                return response(service.getUsers(allParams, keySort, page, pageSize, sortField),
                                "Get list of users successfully!");
        }

        @GetMapping(value = "public/getUsers")
        public ResponseEntity<CommonResponse<ListWrapperResponse<UserResponse>>> getPublicUsers(
                        @RequestParam(required = false, defaultValue = "1") int page,
                        @RequestParam(required = false, defaultValue = "10") int pageSize,
                        @RequestParam Map<String, String> allParams,
                        @RequestParam(defaultValue = "asc") String keySort,
                        @RequestParam(defaultValue = "modified") String sortField, HttpServletRequest request) {
                allParams.put("deleted", "0");
                return response(service.getPublicUsers(allParams, keySort, page, pageSize, sortField),
                                "Get list of users successfully!");
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @GetMapping(value = "admin/getUserDetail/{id}")
        public ResponseEntity<CommonResponse<UserResponse>> getUserById(HttpServletRequest request,
                        @PathVariable String id) {
                validateToken(request);
                String[] roles = { "ROLE_ADMIN" };
                validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
                return response(service.getUserById(id), "Get user successfully!");
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @GetMapping(value = "user/getProfile")
        public ResponseEntity<CommonResponse<UserResponse>> getProfile(HttpServletRequest request) {
                validateToken(request);
                String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
                validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
                String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                                JWT_SECRET);
                return response(service.getUserById(id), "Get user successfully!");
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PostMapping(value = "admin/addNewUser")
        public ResponseEntity<CommonResponse<String>> addNewUser(@RequestBody UserRequest userRequest,
                        HttpServletRequest request) {
                validateToken(request);
                String[] roles = { "ROLE_ADMIN" };
                validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
                service.addNewUser(userRequest);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Add user successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PutMapping(value = "admin/updateUser/{userId}")
        public ResponseEntity<CommonResponse<String>> updateUserAdmin(@RequestBody UserRequest userRequest,
                        @PathVariable(required = true) String userId,
                        HttpServletRequest request) {
                validateToken(request);
                String[] roles = { "ROLE_ADMIN" };
                validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
                service.updateUserById(userRequest, userId);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Update user successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @DeleteMapping(value = "admin/deleteUser/{userId}")
        public ResponseEntity<CommonResponse<String>> deleteUserUser(HttpServletRequest request,
                        @PathVariable(required = true) String userId) {
                validateToken(request);
                String[] roles = { "ROLE_ADMIN" };
                validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
                service.deleteUserById(userId);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Delete user successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PutMapping(value = "user/updateUser")
        public ResponseEntity<CommonResponse<String>> updateUserUser(HttpServletRequest request,
                        @RequestBody UserRequest userRequest) {
                validateToken(request);
                String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
                validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
                String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                                JWT_SECRET);
                service.updateUserById(userRequest, id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Update successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @DeleteMapping(value = "user/deleteUser")
        public ResponseEntity<CommonResponse<String>> deleteUserAdmin(HttpServletRequest request) {
                validateToken(request);
                String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
                validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
                String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                                JWT_SECRET);
                service.deleteUserById(id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Delete successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PutMapping(value = "user/changePassword")
        public ResponseEntity<CommonResponse<String>> changePassword(@RequestBody ChangePasswordReq changePassword,
                        HttpServletRequest request) {
                validateToken(request);
                String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
                validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
                String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                                JWT_SECRET);
                service.changePasswordById(changePassword, id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Change password successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PutMapping(value = "user/changeUsername")
        public ResponseEntity<CommonResponse<String>> changeUsername(@RequestBody ChangeUsernameReq changeUsername,
                        HttpServletRequest request) {
                validateToken(request);
                String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
                validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
                String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                                JWT_SECRET);
                service.changeUsernameById(changeUsername, id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Change username successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PutMapping(value = "user/change2FAStatus")
        public ResponseEntity<CommonResponse<String>> change2FAStatus(HttpServletRequest request) {
                validateToken(request);
                String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
                validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
                String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                                JWT_SECRET);
                service.change2FAStatus(id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Change 2FA status successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }
}
