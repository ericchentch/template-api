package edunhnil.project.forum.api.service.loginService;

import java.util.Optional;

import edunhnil.project.forum.api.dto.loginDTO.LoginRequest;
import edunhnil.project.forum.api.dto.loginDTO.LoginResponse;
import edunhnil.project.forum.api.dto.loginDTO.RegisterRequest;

public interface LoginService {
    Optional<LoginResponse> login(LoginRequest loginRequest, boolean isRegister);

    void logout(String id, String token);

    void register(RegisterRequest RegisterRequest);

    void verifyRegister(String code, String email);

    void resendVerifyRegister(String email);

    void forgotPassword(String email);

    Optional<LoginResponse> verify2FA(String email, String code);

    void resend2FACode(String email);
}
