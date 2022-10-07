package edunhnil.project.forum.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.dto.loginDTO.LoginRequest;
import edunhnil.project.forum.api.dto.loginDTO.LoginResponse;
import edunhnil.project.forum.api.dto.loginDTO.RegisterRequest;
import edunhnil.project.forum.api.jwt.JwtUtils;
import edunhnil.project.forum.api.service.loginService.LoginService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "auth")
// @CrossOrigin(origins = "http://localhost:3000/", methods = {
// RequestMethod.OPTIONS, RequestMethod.GET,
// RequestMethod.POST, RequestMethod.PUT,
// RequestMethod.DELETE }, allowedHeaders = "*", allowCredentials = "true")
public class LoginController extends AbstractController<LoginService> {

        @PostMapping(value = "login")
        public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest,
                        HttpServletRequest request) {
                return response(service.login(loginRequest, false), "Login successfully!");
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PostMapping(value = "logout")
        public ResponseEntity<CommonResponse<String>> logout(HttpServletRequest request) {
                validateToken(request);
                String id = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                                JWT_SECRET);
                service.logout(id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Logout successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @PostMapping(value = "register")
        public ResponseEntity<CommonResponse<String>> register(@RequestBody RegisterRequest registerRequest) {
                service.register(registerRequest);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Verify email sended successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @PostMapping(value = "verify-email/{email}/{code}")
        public ResponseEntity<CommonResponse<String>> verifyEmail(@PathVariable String email,
                        @PathVariable String code) {
                service.verifyRegister(code, email);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Verified account successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @PostMapping(value = "verify-email/resend/{email}")
        public ResponseEntity<CommonResponse<String>> resendVerifyEmail(@PathVariable String email) {
                service.resendVerifyRegister(email);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Resend verify email successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @PostMapping(value = "forgot-password/{email}")
        public ResponseEntity<CommonResponse<String>> forgotPassword(@PathVariable String email) {
                service.forgotPassword(email);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Forgot password email successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @PostMapping(value = "verify-2fa/{email}/{code}")
        public ResponseEntity<CommonResponse<LoginResponse>> verify2FA(@PathVariable String email,
                        @PathVariable String code) {
                return response(service.verify2FA(email, code), "Verify 2FA successfully!");
        }

        @PostMapping(value = "verify-2fa/resend/{email}")
        public ResponseEntity<CommonResponse<String>> resendVerify2FA(@PathVariable String email) {
                service.resend2FACode(email);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Resend verify 2FA email successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

}
