package edunhnil.project.forum.api.dto.categoryDTO;

import edunhnil.project.forum.api.dto.postDTO.PostResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private String id;
    private String categoryName;
    private int postsNumber;
    private PostResponse newestPost;

    public CategoryResponse(String id,
            String categoryName,
            int postsNumber) {
        this.categoryName = categoryName;
        this.id = id;
        this.postsNumber = postsNumber;
    }
}
