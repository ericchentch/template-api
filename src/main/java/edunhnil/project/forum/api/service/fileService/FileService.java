package edunhnil.project.forum.api.service.fileService;

import java.util.Map;
import java.util.Optional;

import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.fileDTO.FileRequest;
import edunhnil.project.forum.api.dto.fileDTO.FileResponse;

public interface FileService {
    void createFile(FileRequest fileRequest);
    void deleteFileAdmins(String _id);
    void deleteFileUsers(String _id, String loginId);

    Optional<ListWrapperResponse<FileResponse>> getFilesByUserId(
        String userId,
        int page,
        int pageSize,
        Map<String, String> allParams,
        String keySort,
        String sortField
    );
    Optional<FileResponse> getFileById(String fileId);
}
