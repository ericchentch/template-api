package edunhnil.project.forum.api.dao.fileRepository;

import java.util.List;
import java.util.Optional;


public interface FileRepository {
    void createFile(File file);
    Optional<File> getFile(String id);
    Optional<List<File>> getFileByUserId(String userId);
    void deleteFile(String id);
}
