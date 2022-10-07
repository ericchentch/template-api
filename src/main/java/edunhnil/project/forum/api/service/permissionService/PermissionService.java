package edunhnil.project.forum.api.service.permissionService;

import java.util.Map;
import java.util.Optional;

import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.permissionDTO.PermissionRequest;
import edunhnil.project.forum.api.dto.permissionDTO.PermissionResponse;

public interface PermissionService {
    Optional<ListWrapperResponse<PermissionResponse>> getPermissions(Map<String, String> allParams, String keySort,
            int page,
            int pageSize, String sortField);

    void addNewPermissions(PermissionRequest permissionRequest);

    void editPermission(PermissionRequest permissionRequest, String id);

    void deletePermission(String id);
}
