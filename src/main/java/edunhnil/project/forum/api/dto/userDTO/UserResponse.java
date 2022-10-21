package edunhnil.project.forum.api.dto.userDTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private String password;
    private int gender;
    private String dob;
    private String address;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<String> tokens;
    private boolean isVerify;
    private boolean is2FAVerify;
    private String created;
    private String modified;
    private int deleted;
}
