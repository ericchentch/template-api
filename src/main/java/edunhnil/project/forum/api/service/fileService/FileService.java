package edunhnil.project.forum.api.service.fileService;

import java.util.List;
import java.util.Optional;

import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.fileDTO.FileRequest;
import edunhnil.project.forum.api.dto.fileDTO.FileResponse;

public interface FileService {
    void createFile(FileRequest fileRequest);
    void deleteFile(String _id);
    Optional<ListWrapperResponse<FileResponse>> getFilesByUserId(String userId);
    Optional<FileResponse> getFileById(String fileId);
}
