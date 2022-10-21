package edunhnil.project.forum.api.utils;

import org.springframework.stereotype.Component;

import edunhnil.project.forum.api.constant.DateTime;
import edunhnil.project.forum.api.dao.userRepository.User;
import edunhnil.project.forum.api.dto.userDTO.UserResponse;

@Component
public class UserUtils {

    public UserResponse generateUserResponse(User user, String type) {
        if (type.compareTo("public") == 0) {
            return new UserResponse(user.get_id().toString(),
                    "",
                    "",
                    user.getGender(),
                    "",
                    "",
                    user.getFirstName(),
                    user.getLastName(),
                    "",
                    "",
                    null,
                    false,
                    false,
                    "",
                    "",
                    0);
        } else {
            return new UserResponse(user.get_id().toString(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getGender(),
                    user.getDob(),
                    user.getAddress(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getTokens(),
                    user.isVerified(),
                    user.isVerify2FA(),
                    DateFormat.toDateString(user.getCreated(), DateTime.YYYY_MM_DD),
                    DateFormat.toDateString(user.getModified(), DateTime.YYYY_MM_DD),
                    user.getDeleted());
        }
    }
}
