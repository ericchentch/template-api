package edunhnil.project.forum.api.dao.permissionRepository;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private ObjectId _id;
    private String name;
    private List<ObjectId> featureId;
    private List<ObjectId> userId;
    private Date created;
    private Date modified;
}
