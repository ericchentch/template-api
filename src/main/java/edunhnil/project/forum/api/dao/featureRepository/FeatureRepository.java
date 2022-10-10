package edunhnil.project.forum.api.dao.featureRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FeatureRepository {
    Optional<List<Feature>> getFeatures(Map<String, String> allParams, String keySort, int page, int pageSize,
            String sortField);

    Optional<Feature> getFeatureById(String id);

    Optional<Feature> getFeatureByPath(String path);

    void insertAndUpdate(Feature feature);

    void deleteFeature(String id);
}
