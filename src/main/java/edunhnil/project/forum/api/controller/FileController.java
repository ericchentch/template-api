package edunhnil.project.forum.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.fileDTO.FileRequest;
import edunhnil.project.forum.api.dto.fileDTO.FileResponse;
import edunhnil.project.forum.api.service.fileService.FileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "file-controller")
public class FileController extends AbstractController<FileService> {
    @PostMapping(value = "create-file")
    @SecurityRequirement(name = "Bearer Authentication")
    void createFile(@RequestBody FileRequest fileRequest) {
        service.createFile(fileRequest);
    }

    @GetMapping(value = "get-files-by-user-id")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<CommonResponse<ListWrapperResponse<FileResponse>>> getFilesByUserId(String userId) {
        return response(service.getFilesByUserId(userId), "Success");
    }

    @DeleteMapping(value = "delete-file/{_id}")
    @SecurityRequirement(name = "Bear Authentication")
    void deleteFile(@PathVariable String _id) {
        service.deleteFile(_id);
    }
}
