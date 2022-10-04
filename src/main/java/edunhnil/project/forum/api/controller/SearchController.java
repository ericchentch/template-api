package edunhnil.project.forum.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.dto.searchDTO.SearchResponse;
import edunhnil.project.forum.api.service.searchService.SearchService;

@RestController
@RequestMapping(value = "search")
public class SearchController extends AbstractController<SearchService> {

    @GetMapping(value = "total-search")
    public ResponseEntity<CommonResponse<SearchResponse>> searchTotal(@RequestParam String keySearch) {
        return response(service.totalSearch(keySearch), "Get search result successfully");
    }
}
