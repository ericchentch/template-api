package edunhnil.project.forum.api.dto.featureDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureResponse {
    private String _id;
    private String name;
    private String path;
    private int deleted;
}
