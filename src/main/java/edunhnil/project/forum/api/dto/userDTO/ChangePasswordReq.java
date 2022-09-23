package edunhnil.project.forum.api.dto.userDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordReq {

    @Schema(type = "string", example = " ")
    @NotEmpty(message = "old password is required")
    @NotNull(message = "old password is required")
    @NotBlank(message = "old password is required")
    private String oldPassword;

    @Schema(type = "string", example = " ")
    @NotEmpty(message = "new password is required")
    @NotNull(message = "new password is required")
    @NotBlank(message = "new password is required")
    private String newPassword;
}
