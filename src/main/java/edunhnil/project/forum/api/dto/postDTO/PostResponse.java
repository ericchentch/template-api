package edunhnil.project.forum.api.dto.postDTO;

import java.util.List;

import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;
import edunhnil.project.forum.api.dto.commonDTO.UserInformation;
import edunhnil.project.forum.api.dto.fileDTO.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private String id;
    private UserInformation author;
    private String title;
    private String content;
    private int view;
    private int like;
    private CategoryResponse category;
    private List<FileResponse> files;
    private String created;
    private String modified;
    private boolean isLiked;
    private int deleted;
}
