package edunhnil.project.forum.api.service.fileService;

import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.dao.fileRepository.File;
import edunhnil.project.forum.api.dao.fileRepository.FileRepository;
import edunhnil.project.forum.api.dto.fileDTO.FileRequest;
import edunhnil.project.forum.api.service.AbstractService;

@Service
public class FileServiceImpl extends AbstractService<FileRepository> implements FileService{

    @Override
    public void createFile(FileRequest fileRequest) {
        // TODO Auto-generated method stub
        File file = objectMapper.convertValue(fileRequest, File.class);
        repository.createFile(file);
    }

    
}
