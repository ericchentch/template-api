package edunhnil.project.forum.api.dao.fileRepository;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "file")
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
