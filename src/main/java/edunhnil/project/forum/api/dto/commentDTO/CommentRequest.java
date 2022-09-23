package edunhnil.project.forum.api.dto.commentDTO;

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
public class CommentRequest {

    @Schema(type = "string", defaultValue = " ")
    @NotEmpty(message = "content is required")
    @NotBlank(message = "content is required")
    @NotNull(message = "content is required")
    private String content;
}
