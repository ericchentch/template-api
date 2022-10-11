package edunhnil.project.forum.api.dto.commonDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInformation {
    private String id;
    private String lastName;
    private String firstName;
}
