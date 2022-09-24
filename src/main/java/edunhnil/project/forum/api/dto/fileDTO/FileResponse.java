package edunhnil.project.forum.api.dto.fileDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
    private String id;
    private String userId;
    private FileEnum typeFile;
    private String linkFile;
    private int createdAt;
}
