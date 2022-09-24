package edunhnil.project.forum.api.dao.fileRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import edunhnil.project.forum.api.dao.AbstractRepository;

@Repository
public class FileRepositoryImpl extends AbstractRepository implements FileRepository {

    @Override
    public void createFile(File file) {
        // TODO Auto-generated method stub
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO forum.files (userId, typeFile, linkFile, createdAt)");
        sql.append("VALUES (?,?,?,?)");
        jdbcTemplate.update(sql.toString(), file.getUserId(), file.getTypeFile(), file.getLinkFile(), file.getCreatedAt());
    }

    @Override
    public Optional<File> getFile(String id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public Optional<List<File>> getFileByUserId(String userId) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public void deleteFile(String id) {
        // TODO Auto-generated method stub
        
    }
    
}
