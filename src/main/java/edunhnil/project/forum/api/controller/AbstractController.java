package edunhnil.project.forum.api.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edunhnil.project.forum.api.config.Guard;
import edunhnil.project.forum.api.dao.userRepository.User;
import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.exception.ForbiddenException;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.exception.UnauthorizedException;
import edunhnil.project.forum.api.jwt.JwtTokenProvider;
import edunhnil.project.forum.api.jwt.JwtUtils;

public abstract class AbstractController<s> {
    @Autowired
    protected s service;

    @Autowired
    protected JwtTokenProvider tokenProvider;

    @Autowired
    protected Guard guard;

    @Value("${spring.key.jwt}")
    protected String JWT_SECRET;

    protected void validateToken(HttpServletRequest request) {
        if (tokenProvider.validateToken(request)) {
            String token = JwtUtils.getJwtFromRequest(request);
            User user = tokenProvider.getUserInfoFromToken(token)
                    .orElseThrow(() -> new ResourceNotFoundException("User are deactivated or deleted!"));
            if (user.getToken().compareTo(token) != 0) {
                throw new UnauthorizedException("Unauthorized");
            }
        } else {
            throw new UnauthorizedException("Unauthorized");
        }
    }

    protected void validateRole(String type, String token, String id, String[] roles) {
        if (type.compareTo("role") == 0) {
            if (!guard.checkRoleById(token, roles))
                throw new ForbiddenException("Access denied!");
        }
        if (type.compareTo("post") == 0) {
            if (!guard.checkAuthorId(token, Integer.parseInt(id)))
                throw new ForbiddenException("Access denied!");
        }
        if (type.compareTo("comment") == 0) {
            if (!guard.checkCommentId(token, Integer.parseInt(id)))
                throw new ForbiddenException("Access denied!");
        }
    }

    protected <T> ResponseEntity<CommonResponse<T>> response(Optional<T> response, String successMessage) {
        return new ResponseEntity<>(new CommonResponse<>(true, response.orElseThrow(() -> {
            throw new ResourceNotFoundException("Resource not found");
        }), successMessage, HttpStatus.OK.value()), HttpStatus.OK);
    }
}
