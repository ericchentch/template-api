package edunhnil.project.forum.api.dto.featureDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureRequest {
    @NotBlank(message = "Name is required!")
    @NotNull(message = "Name is required!")
    @NotEmpty(message = "Name is required!")
    private String name;

    @NotBlank(message = "Path is required!")
    @NotNull(message = "Path is required!")
    @NotEmpty(message = "Path is required!")
    private String path;
}
