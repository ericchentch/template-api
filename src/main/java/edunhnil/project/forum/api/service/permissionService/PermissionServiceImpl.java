package edunhnil.project.forum.api.service.permissionService;

import static java.util.Map.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.constant.DateTime;
import edunhnil.project.forum.api.dao.permissionRepository.Permission;
import edunhnil.project.forum.api.dao.permissionRepository.PermissionRepository;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.featureDTO.FeatureResponse;
import edunhnil.project.forum.api.dto.permissionDTO.PermissionRequest;
import edunhnil.project.forum.api.dto.permissionDTO.PermissionResponse;
import edunhnil.project.forum.api.dto.userDTO.UserResponse;
import edunhnil.project.forum.api.exception.InvalidRequestException;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.service.AbstractService;
import edunhnil.project.forum.api.service.featureService.FeatureService;
import edunhnil.project.forum.api.service.userService.UserService;
import edunhnil.project.forum.api.utils.DateFormat;

@Service
public class PermissionServiceImpl extends AbstractService<PermissionRepository> implements PermissionService {

    @Autowired
    private FeatureService featureService;

    @Autowired
    private UserService userService;

    @Override
    public Optional<ListWrapperResponse<PermissionResponse>> getPermissions(Map<String, String> allParams,
            String keySort, int page, int pageSize, String sortField) {
        List<Permission> permissions = repository.getPermissions(allParams, keySort, page, pageSize, sortField).get();
        return Optional.of(new ListWrapperResponse<PermissionResponse>(
                permissions.stream()
                        .map(permission -> new PermissionResponse(permission.get_id().toString(), permission.getName(),
                                generateFeatureList(permission.getFeatureId().stream()
                                        .map(feature -> feature.toString()).collect(Collectors.toList())),
                                generateUserList(permission.getUserId().stream().map(userId -> userId.toString())
                                        .collect(Collectors.toList())),
                                DateFormat.toDateString(permission.getCreated(), DateTime.YYYY_MM_DD),
                                DateFormat.toDateString(permission.getModified(), DateTime.YYYY_MM_DD),
                                permission.getSkipAccessability()))
                        .collect(Collectors.toList()),
                page, pageSize, permissions.size()));
    }

    @Override
    public void addNewPermissions(PermissionRequest permissionRequest) {
        validate(permissionRequest);
        List<Permission> permissions = repository
                .getPermissions(Map.ofEntries(entry("name", permissionRequest.getName())), "", 0, 0, "").get();
        if (permissions.size() != 0) {
            throw new InvalidRequestException("This name is unavailable!");
        }
        Permission permission = new Permission();
        permission.setName(permissionRequest.getName());
        permission.setCreated(DateFormat.getCurrentTime());
        permission.setSkipAccessability(1);
        if (permissionRequest.getFeatureId().size() != 0) {
            List<FeatureResponse> featureResponse = generateFeatureList(permissionRequest.getFeatureId());
            permission.setFeatureId(
                    featureResponse.stream().map(feature -> new ObjectId(feature.get_id()))
                            .collect(Collectors.toList()));
        } else {
            permission.setFeatureId(new ArrayList<>());
        }
        if (permissionRequest.getUserId().size() != 0) {
            List<UserResponse> userResponse = generateUserList(permissionRequest.getUserId());
            permission
                    .setUserId(
                            userResponse.stream().map(user -> new ObjectId(user.getId())).collect(Collectors.toList()));
        } else {
            permission.setUserId(new ArrayList<>());
        }
        repository.insertAndUpdate(permission);
    }

    @Override
    public void editPermission(PermissionRequest permissionRequest, String id) {
        Permission permission = repository.getPermissionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found permission!"));
        validate(permissionRequest);
        permission.setName(permissionRequest.getName());
        if (permissionRequest.getFeatureId().size() != 0) {
            List<FeatureResponse> featureResponse = generateFeatureList(permissionRequest.getFeatureId());
            permission.setFeatureId(
                    featureResponse.stream().map(feature -> new ObjectId(feature.get_id()))
                            .collect(Collectors.toList()));
        } else {
            permission.setFeatureId(new ArrayList<>());
        }
        if (permissionRequest.getUserId().size() != 0) {
            List<UserResponse> userResponse = generateUserList(permissionRequest.getUserId());
            permission
                    .setUserId(
                            userResponse.stream().map(user -> new ObjectId(user.getId())).collect(Collectors.toList()));
        } else {
            permission.setUserId(new ArrayList<>());
        }
        permission.setSkipAccessability(permissionRequest.getSkipAccessability());
        permission.setModified(DateFormat.getCurrentTime());
        repository.insertAndUpdate(permission);
    }

    @Override
    public void deletePermission(String id) {
        repository.getPermissionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found permission!"));
        repository.deletePermission(id);
    }

    private String generateParamsValue(List<String> features) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < features.size(); i++) {
            result.append(features.get(i));
            if (i != features.size() - 1) {
                result.append(",");
            }
        }
        return result.toString();
    }

    private List<FeatureResponse> generateFeatureList(List<String> features) {
        String result = generateParamsValue(features);
        return featureService.getFeatures(Map.ofEntries(entry("_id", result.toString())), "", 0, 0, "").get().getData();
    }

    private List<UserResponse> generateUserList(List<String> users) {
        String result = generateParamsValue(users);
        return userService.getUsers(Map.ofEntries(entry("_id", result.toString())), "", 0, 0, "", "public", false).get()
                .getData();
    }

}
