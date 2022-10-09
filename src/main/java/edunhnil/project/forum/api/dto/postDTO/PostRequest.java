package edunhnil.project.forum.api.dto.postDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    @Schema(type = "string", example = " ")
    @NotEmpty(message = "title is required")
    @NotBlank(message = "title is required")
    @NotNull(message = "title is required")
    private String title;

    @Schema(type = "string", example = " ")
    @NotEmpty(message = "content is required")
    @NotBlank(message = "content is required")
    @NotNull(message = "content is required")
    private String content;

    @Schema(type = "string", example = " ")
    @NotEmpty(message = "category id is required")
    @NotBlank(message = "category id is required")
    @NotNull(message = "category id is required")
    private String categoryId;

}
