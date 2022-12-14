package edunhnil.project.forum.api.dto.userDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUsernameReq {
    @Schema(type = "string", example = " ")
    @NotEmpty(message = "old username is required")
    @NotBlank(message = "old username is required")
    @NotNull(message = "old username is required")
    private String oldUsername;

    @Schema(type = "string", example = " ")
    @NotEmpty(message = "new username is required")
    @NotBlank(message = "new username is required")
    @NotNull(message = "new username is required")
    private String newUsername;
}
