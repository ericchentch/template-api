package edunhnil.project.forum.api.dao.accessabilityRepository;

import java.util.Optional;

public interface AccessabilityRepository {
    Optional<Accessability> getAccessability(String userId, String targetId);

    void addNewAccessability(Accessability accessability);

    void deleteAccessability(String id);
}
