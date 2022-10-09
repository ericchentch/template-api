package edunhnil.project.forum.api.dto.commentDTO;

import edunhnil.project.forum.api.dto.userDTO.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private String id;
    private String ownerId;
    private UserResponse informationOwner;
    private String postId;
    private String content;
    private int like;
    private String created;
    private String modified;
    private boolean liked;
    private int deleted;
}
