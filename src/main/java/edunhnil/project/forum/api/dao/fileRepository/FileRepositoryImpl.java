package edunhnil.project.forum.api.dao.fileRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edunhnil.project.forum.api.dao.AbstractMongoRepository;
@Repository
public class FileRepositoryImpl extends AbstractMongoRepository implements FileRepository {

    @Override
    public void saveFile(File file) {
        authenticationTemplate.save(file);
    }

    // @Override
    // public void deleteFile(File file) {
    //     // TODO Auto-generated method stub
    //     authenticationTemplate.save(file);
    // }

    @Override
    public Optional<List<File>> getFiles(Map<String, String> allParams, int page, int pageSize) {
        // TODO Auto-generated method stub
        Query query = generateQueryMongoDB(allParams, File.class, "", "", page, pageSize);
        Optional<List<File>> total = replaceFind(query, File.class);
        return total;
    }

    @Override
    public Optional<File> getFileById(String _id) {
        // TODO Auto-generated method stub
        File file = authenticationTemplate.findById(_id, File.class);
        return Optional.ofNullable(file);
    }
    
}
