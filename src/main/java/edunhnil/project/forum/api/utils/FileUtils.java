package edunhnil.project.forum.api.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import edunhnil.project.forum.api.dao.fileRepository.File;
import edunhnil.project.forum.api.dto.fileDTO.FileResponse;

@Component
public class FileUtils {
    public FileResponse generateFileResponse(File file) {
        
        return new FileResponse(
            file.get_id().toString(),
            file.getUserId(),
            file.getTypeFile(),
            file.getLinkFile(),
            file.getCreatedAt()
        );
    }
}
