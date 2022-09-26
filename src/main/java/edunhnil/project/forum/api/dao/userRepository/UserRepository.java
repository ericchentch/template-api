package edunhnil.project.forum.api.dao.userRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository {
        Optional<List<User>> getUsers(Map<String, String> allParams, String keySort, int page, int pageSize,
                        String sortField);

        Optional<User> getUserById(String id);

        Optional<User> checkUsername(String username);

        Optional<User> getUserByEmail(String email);

        void insertAndUpdate(User user);

        long getTotalPage(Map<String, String> allParams);
}