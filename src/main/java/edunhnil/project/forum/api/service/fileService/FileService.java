package edunhnil.project.forum.api.service.fileService;

import java.util.Map;
import java.util.Optional;

import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.fileDTO.FileRequest;
import edunhnil.project.forum.api.dto.fileDTO.FileResponse;

public interface FileService {
    void createFile(FileRequest fileRequest, String loginId);

    void deleteFile(String _id, String loginId, boolean skipAccessability);

    Optional<ListWrapperResponse<FileResponse>> getFilesByUserId(
            int page,
            int pageSize,
            Map<String, String> allParams,
            String keySort,
            String sortField,
            String loginId, boolean skipAccessability);

    Optional<FileResponse> getFileById(String fileId, String loginId, boolean skipAccessability);
}
