package edunhnil.project.forum.api.dao.featureRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edunhnil.project.forum.api.dao.AbstractMongoRepository;

@Repository
public class FeatureRepositoryImpl extends AbstractMongoRepository implements FeatureRepository {

    @Override
    public Optional<List<Feature>> getFeatures(Map<String, String> allParams, String keySort, int page, int pageSize,
            String sortField) {
        Query query = generateQueryMongoDB(allParams, Feature.class, keySort, sortField, page, pageSize);
        return replaceFind(query, Feature.class);
    }

    @Override
    public Optional<Feature> getFeatureById(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("_id", id);
        Query query = generateQueryMongoDB(params, Feature.class, "", "", 0, 0);
        return replaceFindOne(query, Feature.class);
    }

    @Override
    public void insertAndUpdate(Feature feature) {
        authenticationTemplate.save(feature, "features");
    }

    @Override
    public Optional<Feature> getFeatureByPath(String path) {
        Map<String, String> params = new HashMap<>();
        params.put("path", path);
        Query query = generateQueryMongoDB(params, Feature.class, "", "", 0, 0);
        return replaceFindOne(query, Feature.class);
    }

}
