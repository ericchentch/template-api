package edunhnil.project.forum.api.dao.fileRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edunhnil.project.forum.api.dao.AbstractMongoRepository;

@Repository
public class FileRepositoryImpl extends AbstractMongoRepository implements FileRepository {

    @Override
    public void saveFile(File file) {
        authenticationTemplate.save(file);
    }

    @Override
    public Optional<List<File>> getFiles(Map<String, String> allParams, int page, int pageSize, String keySort,
            String sortField) {
        Query query = generateQueryMongoDB(allParams, File.class, keySort, sortField, page, pageSize);
        Optional<List<File>> total = replaceFind(query, File.class);
        return total;
    }

    @Override
    public Optional<File> getFileById(String _id) {
        File file = authenticationTemplate.findById(_id, File.class);
        return Optional.ofNullable(file);
    }

    @Override
    public long getTotal(Map<String, String> allParams) {
        Query query = generateQueryMongoDB(allParams, File.class, "", "", 0, 0);
        long total = authenticationTemplate.count(query, File.class);
        return total;
    }

    @Override
    public Optional<List<File>> getFilesByBelongId(String belongId) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("belongId").in(belongId));
            return replaceFind(query, File.class);
        } catch (IllegalArgumentException e) {
            APP_LOGGER.error("wrong type user id");
            return Optional.empty();
        }
    }

}
