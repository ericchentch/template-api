package edunhnil.project.forum.api.dto.postDTO;

import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;
import edunhnil.project.forum.api.dto.userDTO.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private String id;
    private String authorId;
    private UserResponse author;
    private String title;
    private String content;
    private int view;
    private int like;
    private String categoryId;
    private CategoryResponse category;
    private String created;
    private String modified;
    private boolean isLiked;
    private int deleted;
}
