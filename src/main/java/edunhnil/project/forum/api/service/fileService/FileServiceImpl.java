package edunhnil.project.forum.api.service.fileService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.dao.fileRepository.File;
import edunhnil.project.forum.api.dao.fileRepository.FileRepository;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.fileDTO.FileRequest;
import edunhnil.project.forum.api.dto.fileDTO.FileResponse;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.service.AbstractService;
import edunhnil.project.forum.api.utils.DateFormat;
import edunhnil.project.forum.api.utils.FileUtils;

@Service
public class FileServiceImpl extends AbstractService<FileRepository> implements FileService{

    @Autowired
    private FileUtils fileUtils;

    @Override
    public void createFile(FileRequest fileRequest) {
        File file = objectMapper.convertValue(fileRequest, File.class);
        file.setDeleted("false");
        file.setCreatedAt(DateFormat.getCurrentTime());
        repository.saveFile(file);
    }

    @Override
    public void deleteFile(String _id) {
        File file = repository.getFileById(_id).orElseThrow(() -> new ResourceNotFoundException("Not found file with id: " + _id));
        file.setDeleted("true");
        repository.saveFile(file);
    }

    @Override
    public Optional<ListWrapperResponse<FileResponse>> getFilesByUserId(String userId) {
        Map<String, String> allParams = Map.ofEntries(
                    Map.entry("userId", userId),
                    Map.entry("deleted", "false")
                );
        List<File> files = repository.getFiles(allParams, 0, 0).get();
        return Optional.of(new ListWrapperResponse<FileResponse> (
            files
                .stream()
                .map(file -> fileUtils.generateFileResponse(file))
                .collect(Collectors.toList()),
            0,
            0,
            0
        ));
    }

    @Override
    public Optional<FileResponse> getFileById(String fileId) {
        File file = repository.getFileById(fileId).orElseThrow(
            () -> new ResourceNotFoundException("Not found file with id: " + fileId));

        FileResponse result = fileUtils.generateFileResponse(file);

        return Optional.of(result);
    }
}
