package edunhnil.project.forum.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edunhnil.project.forum.api.dto.categoryDTO.CategoryRequest;
import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;
import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.service.categoryService.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "category")
// @CrossOrigin(origins = "http://localhost:3000/", methods = {
// RequestMethod.OPTIONS, RequestMethod.GET,
// RequestMethod.POST, RequestMethod.PUT,
// RequestMethod.DELETE }, allowedHeaders = "*", allowCredentials = "true")
public class CategoryController extends AbstractController<CategoryService> {

    @GetMapping(value = "/get-list")
    public ResponseEntity<CommonResponse<List<CategoryResponse>>> getCategories(HttpServletRequest request,
            @RequestParam Map<String, String> allParams) {
        return response(service.getCategories(allParams), "Get list of category successfully!");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/add-category")
    public ResponseEntity<CommonResponse<String>> addCategory(HttpServletRequest request,
            @RequestBody CategoryRequest categoryRequest) {
        validateToken(request, false);
        service.saveCategory(categoryRequest);
        return new ResponseEntity<CommonResponse<String>>(
                new CommonResponse<String>(true, "", "Add new category successfully!", HttpStatus.OK.value()),
                HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "/update-category")
    public ResponseEntity<CommonResponse<String>> updateCategory(HttpServletRequest request,
            @RequestBody CategoryRequest categoryRequest, @RequestParam(required = true) String id) {
        validateToken(request, false);
        service.updateCategory(categoryRequest, id);
        return new ResponseEntity<CommonResponse<String>>(
                new CommonResponse<String>(true, "", "Update category successfully!", HttpStatus.OK.value()),
                HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "/delete-category")
    public ResponseEntity<CommonResponse<String>> deleteCategory(HttpServletRequest request,
            @RequestParam(required = true) String id) {
        validateToken(request, false);
        service.deleteCategory(id);
        return new ResponseEntity<CommonResponse<String>>(
                new CommonResponse<String>(true, "", "Delete category successfully!", HttpStatus.OK.value()),
                HttpStatus.OK);
    }
}
