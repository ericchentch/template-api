package edunhnil.project.forum.api.dao.userRepository;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private ObjectId _id;
    private String username;
    private String password;
    private String role;
    private int gender;
    private String dob;
    private String address;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String token;
    private String created;
    private String modified;
    private String code;
    private boolean verified;
    private boolean verify2FA;
    private Date verifyTime;
    private int deleted;
}
