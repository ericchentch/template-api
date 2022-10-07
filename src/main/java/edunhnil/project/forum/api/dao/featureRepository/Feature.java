package edunhnil.project.forum.api.dao.featureRepository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "features")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feature {
    private ObjectId _id;
    private String name;
    private String path;
    private String created;
    private int deleted;
}
