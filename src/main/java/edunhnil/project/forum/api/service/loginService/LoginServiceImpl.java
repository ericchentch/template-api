package edunhnil.project.forum.api.service.loginService;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.constant.FormInput;
import edunhnil.project.forum.api.dao.userRepository.User;
import edunhnil.project.forum.api.dao.userRepository.UserRepository;
import edunhnil.project.forum.api.dto.loginDTO.LoginRequest;
import edunhnil.project.forum.api.dto.loginDTO.LoginResponse;
import edunhnil.project.forum.api.dto.loginDTO.RegisterRequest;
import edunhnil.project.forum.api.email.EmailDetail;
import edunhnil.project.forum.api.email.EmailService;
import edunhnil.project.forum.api.exception.InvalidRequestException;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.jwt.JwtTokenProvider;
import edunhnil.project.forum.api.service.AbstractService;
import edunhnil.project.forum.api.utils.CodeGenerator;
import edunhnil.project.forum.api.utils.PasswordValidator;

@Service
public class LoginServiceImpl extends AbstractService<UserRepository>
        implements LoginService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService;

    @Override
    public Optional<LoginResponse> login(LoginRequest loginRequest, boolean isRegister) {
        validate(loginRequest);
        if (!isRegister) {
            PasswordValidator.validatePassword(loginRequest.getPassword());
        }
        User user = new User();
        if (loginRequest.getUsername().matches(FormInput.EMAIL)) {
            user = repository.getUserByEmail(loginRequest.getUsername()).orElseThrow(
                    () -> new ResourceNotFoundException("Not found user with email: " + loginRequest.getUsername()));
        } else {
            user = repository.checkUsername(loginRequest.getUsername()).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Not found user with username: " + loginRequest.getUsername()));
        }
        if (!user.isVerified())
            throw new InvalidRequestException("This account is not verified!");
        if (!bCryptPasswordEncoder().matches(loginRequest.getPassword(),
                user.getPassword())) {
            throw new InvalidRequestException("password does not match");
        }

        repository.insertAndUpdate(user);
        if (user.isVerify2FA()) {
            String verify2FACode = CodeGenerator.userCodeGenerator();
            emailService
                    .sendSimpleMail(new EmailDetail(user.getEmail(), "Your 2FA code is: " + verify2FACode,
                            "2FA code from forum-api"));
            Date now = new Date();
            Date expiredDate = new Date(now.getTime() + 5 * 60 * 1000L);
            user.setVerifyTime(expiredDate);
            user.setCode(verify2FACode);
            repository.insertAndUpdate(user);
            return Optional.of(new LoginResponse("", "", true));
        } else {
            String jwt = jwtTokenProvider.generateToken(user.get_id().toString());
            user.setToken(jwt);
            repository.insertAndUpdate(user);
            return Optional.of(new LoginResponse(jwt, user.get_id().toString(), false));
        }
    }

    @Override
    public void logout(String id) {
        User user = repository.getUserById(id).orElseThrow(
                () -> new ResourceNotFoundException("User is deactivated or deleted!"));
        user.setToken("");
        repository.insertAndUpdate(user);
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        if (repository.checkUsername(registerRequest.getUsername()).isPresent()) {
            throw new InvalidRequestException("username existed");
        }
        Optional<User> userCheckMail = repository.getUserByEmail(registerRequest.getEmail());
        if (userCheckMail.isPresent()) {
            if (userCheckMail.get().isVerified()) {
                throw new InvalidRequestException("This email is taken!");
            } else {
                throw new InvalidRequestException("Please verify your email!");
            }
        } else {
            validate(registerRequest);
            PasswordValidator.validateNewPassword(registerRequest.getPassword());
            String passwordEncode = bCryptPasswordEncoder().encode(registerRequest.getPassword());
            User user = objectMapper.convertValue(registerRequest, User.class);
            user.setPassword(passwordEncode);
            user.setRole("ROLE_USER");
            user.setToken("");
            user.setCode(CodeGenerator.userCodeGenerator());
            Date now = new Date();
            Date expiredDate = new Date(now.getTime() + 5 * 60 * 1000L);
            user.setVerifyTime(expiredDate);
            repository.insertAndUpdate(user);
            emailService
                    .sendSimpleMail(new EmailDetail(user.getEmail(), user.getCode(), "Sign up code from forum-api"));
        }
    }

    @Override
    public void verifyRegister(String code, String email) {
        User user = new User();
        if (email.matches(FormInput.EMAIL)) {
            user = repository.getUserByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("This email is invalid!"));
        } else {
            user = repository.checkUsername(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Username is not exist, please register!"));
        }
        Date now = new Date();
        if (user.getCode().compareTo(code) != 0)
            throw new InvalidRequestException("This code is invalid");
        else if (user.getVerifyTime().compareTo(now) < 0) {
            throw new InvalidRequestException("Code is expired");
        }
        user.setCode("");
        user.setVerified(true);
        repository.insertAndUpdate(user);
    }

    @Override
    public void resendVerifyRegister(String email) {
        User userCheckMail = repository.getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email does not exist, please sign up!"));
        userCheckMail.setCode(CodeGenerator.userCodeGenerator());
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + 5 * 60 * 1000L);
        userCheckMail.setVerifyTime(expiredDate);
        repository.insertAndUpdate(userCheckMail);
        emailService
                .sendSimpleMail(new EmailDetail(userCheckMail.getEmail(), userCheckMail.getCode(),
                        "Sign up code from forum-api"));

    }

    @Override
    public void forgotPassword(String email) {
        User userCheckMail = repository.getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email does not exist, please sign up!"));
        String newPassword = CodeGenerator.passwordGenerator();
        userCheckMail
                .setPassword(
                        bCryptPasswordEncoder().encode(Base64.getEncoder().encodeToString(newPassword.getBytes())));
        repository.insertAndUpdate(userCheckMail);
        emailService.sendSimpleMail(new EmailDetail(userCheckMail.getEmail(),
                "Your new username: " + userCheckMail.getUsername() + " \n" + "Your new password: " + newPassword,
                "Forgot password detail from forum-api"));
    }

    @Override
    public Optional<LoginResponse> verify2FA(String email, String code) {
        User user = new User();
        if (email.matches(FormInput.EMAIL)) {
            user = repository.getUserByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("This email is invalid!"));
        } else {
            user = repository.checkUsername(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Username is not exist, please register!"));
        }
        Date now = new Date();
        if (user.getCode().compareTo(code) != 0)
            throw new InvalidRequestException("This code is invalid");
        else if (user.getVerifyTime().compareTo(now) < 0) {
            throw new InvalidRequestException("Code is expired");
        }
        String jwt = jwtTokenProvider.generateToken(user.get_id().toString());
        user.setToken(jwt);
        repository.insertAndUpdate(user);
        return Optional.of(new LoginResponse(jwt, user.get_id().toString(), true));
    }

    @Override
    public void resend2FACode(String email) {
        User userCheckMail = repository.getUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email does not exist, please sign up!"));
        userCheckMail.setCode(CodeGenerator.userCodeGenerator());
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + 5 * 60 * 1000L);
        userCheckMail.setVerifyTime(expiredDate);
        repository.insertAndUpdate(userCheckMail);
        emailService
                .sendSimpleMail(new EmailDetail(userCheckMail.getEmail(), userCheckMail.getCode(),
                        "Verify 2FA code from forum-api"));
    }

}
