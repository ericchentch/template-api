package edunhnil.project.forum.api.dto.permissionDTO;

import java.util.List;

import edunhnil.project.forum.api.dto.featureDTO.FeatureResponse;
import edunhnil.project.forum.api.dto.userDTO.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionResponse {
    private String _id;
    private String name;
    private List<FeatureResponse> features;
    private List<UserResponse> users;
    private String created;
    private String modified;
    private int skipAccessability;
}
