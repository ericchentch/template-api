package edunhnil.project.forum.api.dto.categoryDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "category name is required!")
    @NotEmpty(message = "category name is required!")
    @NotNull(message = "category name is required!")
    private String name;
}
