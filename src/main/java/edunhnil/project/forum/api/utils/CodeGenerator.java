package edunhnil.project.forum.api.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class CodeGenerator {
    public static String userCodeGenerator() {
        return RandomStringUtils.randomAlphabetic(2).toUpperCase() + RandomStringUtils.randomNumeric(6);
    }

    public static String usernameGenerator() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public static String passwordGenerator() {
        return RandomStringUtils.randomAlphabetic(6) + RandomStringUtils.randomAlphanumeric(1).toUpperCase()
                + RandomStringUtils.randomNumeric(1) + "@";
    }

}
