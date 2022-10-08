package edunhnil.project.forum.api.service.featureService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edunhnil.project.forum.api.dao.featureRepository.Feature;
import edunhnil.project.forum.api.dao.featureRepository.FeatureRepository;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.featureDTO.FeatureRequest;
import edunhnil.project.forum.api.dto.featureDTO.FeatureResponse;
import edunhnil.project.forum.api.exception.InvalidRequestException;
import edunhnil.project.forum.api.exception.ResourceNotFoundException;
import edunhnil.project.forum.api.service.AbstractService;

@Service
public class FeatureServiceImpl extends AbstractService<FeatureRepository> implements FeatureService {

    @Override
    public Optional<ListWrapperResponse<FeatureResponse>> getFeatures(Map<String, String> allParams, String keySort,
            int page, int pageSize, String sortField) {
        List<Feature> features = repository.getFeatures(allParams, keySort, page, pageSize, sortField).get();
        return Optional.of(new ListWrapperResponse<FeatureResponse>(
                features.stream().map(f -> new FeatureResponse(f.get_id().toString(), f.getName(), f.getPath(),
                        f.getDeleted())).collect(Collectors.toList()),
                page, pageSize, features.size()));
    }

    @Override
    public void addNewFeature(FeatureRequest featureRequest) {
        validate(featureRequest);
        if (repository.getFeatureByPath(featureRequest.getPath()).isPresent()) {
            throw new InvalidRequestException("This feature existed");
        }
        Feature feature = objectMapper.convertValue(featureRequest, Feature.class);
        repository.insertAndUpdate(feature);
    }

    @Override
    public void editFeature(FeatureRequest featureRequest, String id) {
        validate(featureRequest);
        Feature feature = repository.getFeatureById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found feature!"));
        feature.setName(featureRequest.getName());
        feature.setPath(featureRequest.getPath());
        repository.insertAndUpdate(feature);
    }

    @Override
    public void deleteFeature(String id) {
        Feature feature = repository.getFeatureById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found feature!"));
        feature.setDeleted(1);
        repository.insertAndUpdate(feature);
    }

}
