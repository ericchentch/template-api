package edunhnil.project.forum.api.dto.fileDTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
    private String id;
    private String userId;
    private String typeFile;
    private String linkFile;
    private Date createdAt;
}
