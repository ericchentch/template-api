package edunhnil.project.forum.api.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import edunhnil.project.forum.api.constant.FormInput;
import edunhnil.project.forum.api.exception.InvalidPasswordException;
import edunhnil.project.forum.api.exception.NotEncodePasswordException;

public class PasswordValidator {

    public static void validatePassword(String password) {
        if (!Base64.isBase64(password)) {
            throw new NotEncodePasswordException("Password must be encoded!");
        } else {
            try {
                String decodedNewPassword = new String(Base64.decodeBase64(password));
                if (!decodedNewPassword.matches(FormInput.BASE64_REGEX)) {
                    throw new NotEncodePasswordException("Password must be encoded!");
                }
            } catch (IllegalArgumentException e) {
                throw new NotEncodePasswordException("Password must be encoded!");
            } catch (IllegalStateException e) {
                throw new NotEncodePasswordException("Password must be encoded!");
            }
        }
    }

    public static void validateNewPassword(String newPassword) {
        if (Base64.isBase64(newPassword)) {
            try {
                String decodedNewPassword = new String(Base64.decodeBase64(newPassword));
                if (!decodedNewPassword.matches(FormInput.BASE64_REGEX)) {
                    throw new NotEncodePasswordException("Password must be encoded!");
                }
                if (!decodedNewPassword.matches(FormInput.PASSWORD)) {
                    throw new InvalidPasswordException("Password must be passed condition!");
                }
            } catch (IllegalArgumentException e) {
                throw new NotEncodePasswordException("Password must be encoded!");
            } catch (IllegalStateException e) {
                throw new NotEncodePasswordException("Password must be encoded!");
            }
        } else {
            throw new NotEncodePasswordException("Password must be encoded!");
        }
    }

}
