package edunhnil.project.forum.api.dao.fileRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
    private int id;
    private String userId;
    private String typeFile;
    private String linkFile;
    private int createdAt;
}
