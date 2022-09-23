package edunhnil.project.forum.api.dto.searchDTO;

import java.util.List;

import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;
import edunhnil.project.forum.api.dto.postDTO.PostResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    private List<PostResponse> popularPost;
    private List<PostResponse> newestPost;
    private List<CategoryResponse> categories;
}
