package edunhnil.project.forum.api.service.fileService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.dao.accessabilityRepository.Accessability;
import edunhnil.project.forum.api.dao.accessabilityRepository.AccessabilityRepository;
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
public class FileServiceImpl extends AbstractService<FileRepository> implements FileService {

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private AccessabilityRepository accessabilityRepository;

    @Override
    public void createFile(FileRequest fileRequest, String loginId) {
        File file = objectMapper.convertValue(fileRequest, File.class);
        ObjectId newId = new ObjectId();
        file.set_id(newId);
        file.setUserId(loginId);
        file.setDeleted(0);
        file.setCreatedAt(DateFormat.getCurrentTime());
        accessabilityRepository.addNewAccessability(new Accessability(null, new ObjectId(loginId), newId.toString()));
        repository.saveFile(file);
    }

    @Override
    public Optional<ListWrapperResponse<FileResponse>> getFilesByUserId(
            int page,
            int pageSize,
            Map<String, String> allParams,
            String keySort,
            String sortField,
            String loginId,
            boolean skipAccessability) {
        List<File> files = repository.getFiles(allParams, page, pageSize, keySort, sortField).get();
        return Optional.of(new ListWrapperResponse<FileResponse>(
                files
                        .stream()
                        .map(file -> fileUtils.generateFileResponse(file,
                                isPublic(file.getUserId(), loginId, skipAccessability)))
                        .collect(Collectors.toList()),
                page,
                pageSize,
                files.size()));
    }

    @Override
    public Optional<FileResponse> getFileById(String fileId, String loginId, boolean skipAccessability) {
        Map<String, String> allParams = new HashMap<>();
        allParams.put("_id", fileId);
        List<File> files = repository.getFiles(allParams, 0, 0, "", "").get();
        if (files.size() == 0) {
            throw new ResourceNotFoundException("This file is deleted or not added!");
        }
        FileResponse result = fileUtils.generateFileResponse(files.get(0),
                isPublic(files.get(0).getUserId(), loginId, skipAccessability));
        return Optional.of(result);
    }

    @Override
    public void deleteFile(String _id, String loginId, boolean skipAccessability) {
        checkAccessability(loginId, _id, skipAccessability);
        File file = repository.getFileById(_id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found file with id: " + _id));
        file.setDeleted(1);
        repository.saveFile(file);
    }
}
