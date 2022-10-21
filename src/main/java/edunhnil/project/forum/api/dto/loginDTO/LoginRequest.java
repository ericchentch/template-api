package edunhnil.project.forum.api.dto.loginDTO;

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
public class LoginRequest {

    @Schema(type = "string", example = " ")
    @NotEmpty(message = "username is required")
    @NotBlank(message = "username is required")
    @NotNull(message = "username is required")
    private String username;

    @Schema(type = "string", example = " ")
    @NotNull(message = "password is required")
    @NotEmpty(message = "password is required")
    @NotBlank(message = "password is required")
    private String password;

    @Schema(type = "string", example = " ")
    @NotEmpty(message = "device id is required")
    @NotBlank(message = "device id is required")
    @NotNull(message = "device id is required")
    private String deviceId;
}