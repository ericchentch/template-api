package edunhnil.project.forum.api.dao.likeRepository;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    private String id;
    private String ownerId;
    private String targetId;
    private String type;
    private Date created;
    private int deleted;
}
