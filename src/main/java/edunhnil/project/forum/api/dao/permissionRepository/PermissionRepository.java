package edunhnil.project.forum.api.dao.permissionRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PermissionRepository {
    Optional<List<Permission>> getPermissions(Map<String, String> allParams, String keySort, int page, int pageSize,
            String sortField);

    Optional<Permission> getPermissionById(String id);

    Optional<List<Permission>> getPermissionByUser(String userId, String featureId);

    void insertAndUpdate(Permission permission);

    void deletePermission(String id);
}
