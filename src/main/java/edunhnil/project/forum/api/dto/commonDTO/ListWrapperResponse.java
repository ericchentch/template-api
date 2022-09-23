package edunhnil.project.forum.api.dto.commonDTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListWrapperResponse<T> {
    private List<T> data;
    private int page;
    private int pageSize;
    private Object totalRows;
}
