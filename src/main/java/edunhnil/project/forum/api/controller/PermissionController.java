package edunhnil.project.forum.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.permissionDTO.PermissionRequest;
import edunhnil.project.forum.api.dto.permissionDTO.PermissionResponse;
import edunhnil.project.forum.api.service.permissionService.PermissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "permissions")
public class PermissionController extends AbstractController<PermissionService> {

        @SecurityRequirement(name = "Bearer Authentication")
        @GetMapping(value = "get-list-permissions")
        public ResponseEntity<CommonResponse<ListWrapperResponse<PermissionResponse>>> getFeatures(
                        @RequestParam(required = false, defaultValue = "1") int page,
                        @RequestParam(required = false, defaultValue = "10") int pageSize,
                        @RequestParam Map<String, String> allParams,
                        @RequestParam(defaultValue = "asc") String keySort,
                        @RequestParam(defaultValue = "modified") String sortField, HttpServletRequest request) {
                validateToken(request);
                return response(service.getPermissions(allParams, keySort, page, pageSize, sortField),
                                "Get list of permissions successfully!");
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PostMapping(value = "add-new-permission")
        public ResponseEntity<CommonResponse<String>> addNewFeature(
                        @RequestBody PermissionRequest permissionRequest, HttpServletRequest request) {
                validateToken(request);
                service.addNewPermissions(permissionRequest);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Add permission successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PutMapping(value = "update-permission/{id}")
        public ResponseEntity<CommonResponse<String>> updateFeature(
                        @RequestBody PermissionRequest permissionRequest, @PathVariable String id,
                        HttpServletRequest request) {
                validateToken(request);
                service.editPermission(permissionRequest, id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Update permission successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @DeleteMapping(value = "delete-permission/{id}")
        public ResponseEntity<CommonResponse<String>> updateFeature(
                        @PathVariable String id, HttpServletRequest request) {
                validateToken(request);
                service.deletePermission(id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Delete permission successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }
}
