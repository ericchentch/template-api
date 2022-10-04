package edunhnil.project.forum.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edunhnil.project.forum.api.dto.categoryDTO.CategoryResponse;
import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.service.categoryService.CategoryService;

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
}
