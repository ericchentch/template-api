package edunhnil.project.forum.api.dto.userDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import edunhnil.project.forum.api.constant.FormInput;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class UserRequest {

    @Schema(type = "string", example = " ")
    @NotEmpty(message = "Username is required!")
    @NotBlank(message = "Username is required!")
    @NotNull(message = "Username is required!")
    @Pattern(regexp = FormInput.USERNAME, message = "Username is invalid!")
    private String username;

    @Schema(type = "string", example = " ")
    @NotEmpty(message = "Password is required!")
    @NotBlank(message = "Password is required!")
    @NotNull(message = "Password is required!")
    private String password;

    @Min(value = 0)
    @Max(value = 1)
    @NotNull(message = "Gender is required!")
    @Schema(type = "int", example = "0")
    private int gender;

    @Schema(type = "string", example = "yyyy-mm-dd")
    @NotNull(message = "Date of birth is required!")
    @NotBlank(message = "Date of birth is required!")
    @NotEmpty(message = "Date of birth is required!")
    @Pattern(regexp = FormInput.DATE, message = "Date of birth is invalid!")
    private String dob;

    @Schema(type = "string", example = " ")
    @NotNull(message = "Address is required!")
    @NotBlank(message = "Address is required!")
    @NotEmpty(message = "Address is required!")
    private String address;

    @Schema(type = "string", example = " ")
    @NotNull(message = "First name is required!")
    @NotBlank(message = "First name is required!")
    @NotEmpty(message = "First name is required!")
    private String firstName;

    @Schema(type = "string", example = " ")
    @NotNull(message = "Last name is required!")
    @NotBlank(message = "Last name is required!")
    @NotEmpty(message = "Last name is required!")
    private String lastName;

    @Schema(type = "string", example = " ")
    @NotNull(message = "Phone number is required!")
    @NotBlank(message = "Phone number is required!")
    @NotEmpty(message = "Phone number is required!")
    @Pattern(regexp = FormInput.PHONE, message = "Phone is invalid!")
    private String phone;

    @Schema(type = "string", example = " ")
    @NotNull(message = "Email is required!")
    @NotBlank(message = "Email is required!")
    @NotEmpty(message = "Email is required!")
    @Pattern(regexp = FormInput.EMAIL, message = "Email is invalid")
    private String email;

    @Schema(type = "string", example = "ROLE_USER")
    @NotEmpty(message = "Role is required!")
    @NotBlank(message = "Role is required!")
    @NotNull(message = "Role is required!")
    private String role;

    @Min(value = 0)
    @Max(value = 1)
    @NotNull(message = "Deleted is required!")
    @Schema(type = "int", example = "0")
    private int deleted;
}
