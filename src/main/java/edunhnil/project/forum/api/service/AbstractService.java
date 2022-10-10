package edunhnil.project.forum.api.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import edunhnil.project.forum.api.dao.accessabilityRepository.AccessabilityRepository;
import edunhnil.project.forum.api.exception.ForbiddenException;
import edunhnil.project.forum.api.exception.InvalidRequestException;
import edunhnil.project.forum.api.log.AppLogger;
import edunhnil.project.forum.api.log.LoggerFactory;
import edunhnil.project.forum.api.log.LoggerType;
import edunhnil.project.forum.api.utils.ObjectValidator;

public abstract class AbstractService<r> {
    @Autowired
    protected r repository;

    @Autowired
    protected Environment env;

    @Autowired
    protected ObjectValidator objectValidator;

    @Autowired
    protected AccessabilityRepository accessabilityRepository;

    protected ObjectMapper objectMapper;

    protected AppLogger APP_LOGGER = LoggerFactory.getLogger(LoggerType.APPLICATION);;

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    protected BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    protected <T> void validate(T request) {
        String message = objectValidator.validateRequestThenReturnMessage(request);
        if (!ObjectUtils.isEmpty(message)) {
            throw new InvalidRequestException(message);
        }
    }

    protected void checkAccessability(String loginId, String targetId, boolean skipAccessability) {
        if (!skipAccessability) {
            accessabilityRepository.getAccessability(loginId, targetId)
                    .orElseThrow(() -> new ForbiddenException("Access denied!"));
        }
    }

    protected String isPublic(String ownerId, String loginId, boolean skipAccessability) {
        if (skipAccessability)
            return "";
        if (ownerId.compareTo(loginId) == 0) {
            return "";
        } else
            return "public";
    }
}
