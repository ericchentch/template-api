package edunhnil.project.forum.api.utils;

import org.springframework.stereotype.Component;

import edunhnil.project.forum.api.dao.fileRepository.File;
import edunhnil.project.forum.api.dto.fileDTO.FileResponse;

@Component
public class FileUtils {
    public FileResponse generateFileResponse(File file, String type) {
        if (type.compareTo("public") == 0) {
            return new FileResponse(
                    file.get_id().toString(),
                    "",
                    file.getTypeFile(),
                    file.getLinkFile(),
                    file.getCreatedAt());
        } else {
            return new FileResponse(
                    file.get_id().toString(),
                    file.getUserId(),
                    file.getTypeFile(),
                    file.getLinkFile(),
                    file.getCreatedAt());
        }
    }
}
