package edunhnil.project.forum.api.dto.permissionDTO;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRequest {
    @NotBlank(message = "Name is required!")
    @NotNull(message = "Name is required!")
    @NotEmpty(message = "Name is required!")
    private String name;

    @NotBlank(message = "Feature id is required!")
    @NotNull(message = "Feature id is required!")
    @NotEmpty(message = "Feature id is required!")
    private List<String> featureId;

    @NotBlank(message = "User id is required!")
    @NotNull(message = "User id is required!")
    @NotEmpty(message = "User id is required!")
    private List<String> userId;
}
