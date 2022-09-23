package edunhnil.project.forum.api.dto.loginDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String userId;
    private boolean verify2FA;

    public LoginResponse(String accessToken, String userId, boolean verify2FA) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.verify2FA = verify2FA;
    }
}
