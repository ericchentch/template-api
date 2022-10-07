package edunhnil.project.forum.api.service.fileService;

import java.util.Optional;

import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.fileDTO.FileRequest;
import edunhnil.project.forum.api.dto.fileDTO.FileResponse;

public interface FileService {
    void createFile(FileRequest fileRequest);
    void deleteFileAdmins(String _id);
    void deleteFileUsers(String _id, String loginId);

    Optional<ListWrapperResponse<FileResponse>> getFilesByUserId(String userId);
    Optional<FileResponse> getFileById(String fileId);
}
