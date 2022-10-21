package edunhnil.project.forum.api.controller;

import static java.util.Map.entry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import edunhnil.project.forum.api.dto.commonDTO.ValidationResponse;
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

    protected ValidationResponse validateToken(HttpServletRequest request, boolean hasPublic) {
        if (tokenProvider.validateToken(request)) {
            String token = JwtUtils.getJwtFromRequest(request);
            User user = tokenProvider.getUserInfoFromToken(token)
                    .orElseThrow(() -> new UnauthorizedException("User are deactivated or deleted!"));
            if (user.getTokens().stream().filter(saveToken -> saveToken.compareTo(token) == 0)
                    .collect(Collectors.toList()).size() != 0) {
                throw new UnauthorizedException("Unauthorized");
            }
            List<Feature> feature = featureRepository
                    .getFeatures(Map.ofEntries(entry("path", request.getRequestURI())), "", 0, 0, "").get();
            if (feature.size() == 0) {
                throw new ResourceNotFoundException("This feature is not enabled!");
            }
            List<Permission> permissions = permissionRepository
                    .getPermissionByUser(user.get_id().toString(), feature.get(0).get_id().toString())
                    .orElseThrow(() -> new UnauthorizedException("You are not approved any permissions!"));
            if (permissions.size() == 0) {
                throw new ForbiddenException("Access denied!");
            }
            boolean skipAccessability = false;
            for (Permission permission : permissions) {
                if (permission.getSkipAccessability() == 0
                        && permission.getFeatureId().contains(feature.get(0).get_id())) {
                    skipAccessability = true;
                }
            }
            return new ValidationResponse(skipAccessability, user.get_id().toString());
        } else {
            if (!hasPublic)
                throw new UnauthorizedException("Unauthorized");
            return new ValidationResponse(false, "public");
        }
    }

    protected <T> ResponseEntity<CommonResponse<T>> response(Optional<T> response, String successMessage) {
        return new ResponseEntity<>(new CommonResponse<>(true, response.orElseThrow(() -> {
            throw new ResourceNotFoundException("Resource not found");
        }), successMessage, HttpStatus.OK.value()), HttpStatus.OK);
    }
}
