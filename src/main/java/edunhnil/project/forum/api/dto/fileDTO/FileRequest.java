package edunhnil.project.forum.api.dto.fileDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class FileRequest {
    @Schema(type = "string", defaultValue = " ")
    @NotEmpty(message = "User Id is required")
    @NotBlank(message = "User Id is required")
    @NotNull(message = "User Id is required")
    private String userId;

    @Schema(type = "string", defaultValue = "VIDEO")
    @NotEmpty(message = "Type of file is required")
    @NotBlank(message = "Type of file is required")
    @NotNull(message = "Type of file is required")
    private String typeFile;
    
    @Schema(type = "string", defaultValue = " ")
    @NotEmpty(message = "Link is required")
    @NotBlank(message = "Link is required")
    @NotNull(message = "Link is required")
    private String linkFile;
}
