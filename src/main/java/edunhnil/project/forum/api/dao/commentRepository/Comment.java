package edunhnil.project.forum.api.dao.commentRepository;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private String id;
    private String ownerId;
    private String postId;
    private String content;
    private Date created;
    private Date modified;
    private int deleted;
}
