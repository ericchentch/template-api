package edunhnil.project.forum.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edunhnil.project.forum.api.dto.fileDTO.FileRequest;
import edunhnil.project.forum.api.service.fileService.FileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "file-controller")
public class FileController extends AbstractController<FileService> {
    @PostMapping(value = "create-file")
    @SecurityRequirement(name = "Bearer Authentication")
    void createFile(FileRequest fileRequest) {
        service.createFile(fileRequest);
    }
}
