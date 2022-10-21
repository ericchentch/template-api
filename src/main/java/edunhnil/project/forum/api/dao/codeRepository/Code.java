package edunhnil.project.forum.api.dao.codeRepository;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "codes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Code {
    private ObjectId _id;
    private ObjectId userId;
    private String type;
    private String code;
    private Date expiredDate;
}
