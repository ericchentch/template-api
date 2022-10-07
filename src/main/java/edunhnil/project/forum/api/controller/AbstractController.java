package edunhnil.project.forum.api.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edunhnil.project.forum.api.dao.featureRepository.Feature;
import edunhnil.project.forum.api.dao.featureRepository.FeatureRepository;
import edunhnil.project.forum.api.dao.permissionRepository.Permission;
import edunhnil.project.forum.api.dao.permissionRepository.PermissionRepository;
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
    protected FeatureRepository featureRepository;

    @Autowired
    protected PermissionRepository permissionRepository;

    @Value("${spring.key.jwt}")
    protected String JWT_SECRET;

    protected void validateToken(HttpServletRequest request) {
        if (tokenProvider.validateToken(request)) {
            String token = JwtUtils.getJwtFromRequest(request);
            User user = tokenProvider.getUserInfoFromToken(token)
                    .orElseThrow(() -> new UnauthorizedException("User are deactivated or deleted!"));
            if (user.getToken().compareTo(token) != 0) {
                throw new UnauthorizedException("Unauthorized");
            }
            Feature feature = featureRepository.getFeatureByPath(request.getRequestURI())
                    .orElseThrow(() -> new ResourceNotFoundException("This feature is not enabled!"));
            List<Permission> permissions = permissionRepository
                    .getPermissionByUser(user.get_id().toString(), feature.get_id().toString())
                    .orElseThrow(() -> new UnauthorizedException("You are not approved any permissions!"));
            if (permissions.size() == 0) {
                throw new ForbiddenException("Access denied!");
            }
        } else {
            throw new UnauthorizedException("Unauthorized");
        }
    }

    protected <T> ResponseEntity<CommonResponse<T>> response(Optional<T> response, String successMessage) {
        return new ResponseEntity<>(new CommonResponse<>(true, response.orElseThrow(() -> {
            throw new ResourceNotFoundException("Resource not found");
        }), successMessage, HttpStatus.OK.value()), HttpStatus.OK);
    }
}
