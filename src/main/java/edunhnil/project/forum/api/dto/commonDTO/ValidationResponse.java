package edunhnil.project.forum.api.dto.commonDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponse {
    private boolean skipAccessability;
    private String loginId;
}
