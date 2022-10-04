package edunhnil.project.forum.api.controller;

import javax.servlet.http.HttpServletRequest;

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
import edunhnil.project.forum.api.jwt.JwtUtils;
import edunhnil.project.forum.api.service.fileService.FileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "files")
public class FileController extends AbstractController<FileService> {
    @PostMapping(value = "user/create-file")
    @SecurityRequirement(name = "Bearer Authentication")
    void createFile(@RequestBody FileRequest fileRequest, HttpServletRequest request) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
        validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);

        service.createFile(fileRequest);
    }

    @GetMapping(value = "user/get-files/{userId}")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<CommonResponse<ListWrapperResponse<FileResponse>>> getFilesByUserId(HttpServletRequest request) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
        validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
        String userId = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                        JWT_SECRET);
        
        return response(service.getFilesByUserId(userId), "Success");
    }
    
    @GetMapping(value = "user/get-file/{fileId}")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<CommonResponse<FileResponse>> getFilesByFileId(@PathVariable String fileId, HttpServletRequest request) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
        validateRole("role", JwtUtils.getJwtFromRequest(request), "0", roles);
        
        return response(service.getFileById(fileId), "Success");
    }

    @DeleteMapping(value = "user/delete-file/{_id}")
    @SecurityRequirement(name = "Bear Authentication")
    String deleteFile(@PathVariable String _id, HttpServletRequest request) {
        validateToken(request);
        String[] roles = { "ROLE_ADMIN", "ROLE_USER" };
        validateRole("file", JwtUtils.getJwtFromRequest(request), _id, roles);
        String userId = JwtUtils.getUserIdFromJwt(JwtUtils.getJwtFromRequest(request),
                        JWT_SECRET);
        
        service.deleteFile(_id);
        return "Success";
    }
}
