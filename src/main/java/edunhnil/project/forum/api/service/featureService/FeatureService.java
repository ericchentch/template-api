package edunhnil.project.forum.api.service.featureService;

import java.util.Map;
import java.util.Optional;

import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.featureDTO.FeatureRequest;
import edunhnil.project.forum.api.dto.featureDTO.FeatureResponse;

public interface FeatureService {
    Optional<ListWrapperResponse<FeatureResponse>> getFeatures(Map<String, String> allParams, String keySort, int page,
            int pageSize, String sortField);

    void addNewFeature(FeatureRequest featureRequest);

    void editFeature(FeatureRequest featureRequest, String id);

    void deleteFeature(String id);
}
