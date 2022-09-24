package edunhnil.project.forum.api.dao.fileRepository;

import java.util.Date;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
    private ObjectId _id;
    private String userId;
    private String typeFile;
    private String linkFile;
    private Date createdAt;
    private String deleted;
}
