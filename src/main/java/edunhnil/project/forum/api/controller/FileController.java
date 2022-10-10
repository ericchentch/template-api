package edunhnil.project.forum.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.commonDTO.ValidationResponse;
import edunhnil.project.forum.api.dto.fileDTO.FileRequest;
import edunhnil.project.forum.api.dto.fileDTO.FileResponse;
import edunhnil.project.forum.api.service.fileService.FileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "files")
public class FileController extends AbstractController<FileService> {
        @PostMapping(value = "create-file")
        @SecurityRequirement(name = "Bearer Authentication")
        public ResponseEntity<CommonResponse<String>> createFile(@RequestBody FileRequest fileRequest,
                        HttpServletRequest request) {
                ValidationResponse result = validateToken(request, false);
                service.createFile(fileRequest, result.getLoginId());
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Create file successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @GetMapping(value = "get-files")
        @SecurityRequirement(name = "Bearer Authentication")
        public ResponseEntity<CommonResponse<ListWrapperResponse<FileResponse>>> getFilesByUserId(
                        @RequestParam(required = false, defaultValue = "1") int page,
                        @RequestParam(required = false, defaultValue = "10") int pageSize,
                        @RequestParam Map<String, String> allParams,
                        @RequestParam(defaultValue = "asc") String keySort,
                        @RequestParam(defaultValue = "modified") String sortField, HttpServletRequest request) {
                ValidationResponse result = validateToken(request, true);
                return response(service.getFilesByUserId(
                                page,
                                pageSize,
                                allParams,
                                keySort,
                                sortField, result.getLoginId(), result.isSkipAccessability()), "Success");
        }

        @GetMapping(value = "get-file")
        @SecurityRequirement(name = "Bearer Authentication")
        public ResponseEntity<CommonResponse<FileResponse>> getFilesByFileId(
                        @RequestParam(required = true) String fileId,
                        HttpServletRequest request) {
                ValidationResponse result = validateToken(request, true);
                return response(service.getFileById(fileId, result.getLoginId(), result.isSkipAccessability()),
                                "Success");
        }

        @DeleteMapping(value = "delete-file")
        @SecurityRequirement(name = "Bear Authentication")
        public ResponseEntity<CommonResponse<String>> deleteFile(@RequestParam(required = true) String _id,
                        HttpServletRequest request) {
                ValidationResponse result = validateToken(request, false);
                service.deleteFile(_id, result.getLoginId(), result.isSkipAccessability());
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Delete file successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }
}
