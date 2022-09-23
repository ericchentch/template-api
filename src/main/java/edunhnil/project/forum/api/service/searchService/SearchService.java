package edunhnil.project.forum.api.service.searchService;

import java.util.Optional;

import edunhnil.project.forum.api.dto.searchDTO.SearchResponse;

public interface SearchService {
    public Optional<SearchResponse> totalSearch(String keySearch);
}
