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
import edunhnil.project.forum.api.dto.featureDTO.FeatureRequest;
import edunhnil.project.forum.api.dto.featureDTO.FeatureResponse;
import edunhnil.project.forum.api.service.featureService.FeatureService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "features")
public class FeatureController extends AbstractController<FeatureService> {

        @SecurityRequirement(name = "Bearer Authentication")
        @GetMapping(value = "get-list-feature")
        public ResponseEntity<CommonResponse<ListWrapperResponse<FeatureResponse>>> getFeatures(
                        @RequestParam(required = false, defaultValue = "1") int page,
                        @RequestParam(required = false, defaultValue = "10") int pageSize,
                        @RequestParam Map<String, String> allParams,
                        @RequestParam(defaultValue = "asc") String keySort,
                        @RequestParam(defaultValue = "modified") String sortField, HttpServletRequest request) {
                validateToken(request);
                return response(service.getFeatures(allParams, keySort, page, pageSize, sortField),
                                "Get list of features successfully!");
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PostMapping(value = "add-new-feature")
        public ResponseEntity<CommonResponse<String>> addNewFeature(
                        @RequestBody FeatureRequest featureRequest, HttpServletRequest request) {
                validateToken(request);
                service.addNewFeature(featureRequest);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Add feature successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @PutMapping(value = "update-feature/{id}")
        public ResponseEntity<CommonResponse<String>> updateFeature(
                        @RequestBody FeatureRequest featureRequest, @PathVariable String id,
                        HttpServletRequest request) {
                validateToken(request);
                service.editFeature(featureRequest, id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Update feature successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @SecurityRequirement(name = "Bearer Authentication")
        @DeleteMapping(value = "delete-feature/{id}")
        public ResponseEntity<CommonResponse<String>> updateFeature(
                        @PathVariable String id, HttpServletRequest request) {
                validateToken(request);
                service.deleteFeature(id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Delete feature successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }
}
