package edunhnil.project.forum.api.dao;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import edunhnil.project.forum.api.exception.BadSqlException;
import edunhnil.project.forum.api.log.AppLogger;
import edunhnil.project.forum.api.log.LoggerFactory;
import edunhnil.project.forum.api.log.LoggerType;

public abstract class AbstractMongoRepository {

    @Autowired
    @Qualifier("authenticationMongoTemplate")
    protected MongoTemplate authenticationTemplate;

    protected AppLogger APP_LOGGER = LoggerFactory.getLogger(LoggerType.APPLICATION);

    protected Query generateQueryMongoDB(Map<String, String> allParams, Class<?> clazz, String keySort,
            String sortField, int page, int pageSize) {
        Query query = new Query();
        Field[] fields = clazz.getDeclaredFields();
        for (Map.Entry<String, String> items : allParams.entrySet()) {
            for (Field field : fields) {
                if (field.getName().compareTo(items.getKey()) == 0) {
                    String[] values = items.getValue().split(",");
                    if (field.getType() == ObjectId.class) {
                        for (int i = 0; i < values.length; i++) {
                            try {
                                query.addCriteria(Criteria.where(items.getKey()).is(new ObjectId(values[i])));
                            } catch (IllegalArgumentException e) {
                                APP_LOGGER.error(e.getMessage());
                                throw new BadSqlException("id must be objectId format!");
                            }
                        }
                    } else {
                        for (int i = 0; i < values.length; i++) {
                            query.addCriteria(Criteria.where(items.getKey()).is(values[i]));
                        }
                    }
                }
            }
        }
        if (keySort.trim().compareTo("") != 0 && sortField.trim().compareTo("ASC") != 0) {
            query.with(Sort.by(Sort.Direction.ASC, sortField));
        }
        if (keySort.trim().compareTo("") != 0 && sortField.trim().compareTo("DESC") != 0) {
            query.with(Sort.by(Sort.Direction.DESC, sortField));
        }
        if (page > 0 && pageSize > 0) {
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            query.with(pageable);
        }
        return query;
    }

    protected <T> Optional<List<T>> replaceFind(Query query, Class<T> clazz) {
        try {
            List<T> result = authenticationTemplate.find(query, clazz);
            return Optional.of(result);
        } catch (IllegalArgumentException e) {
            APP_LOGGER.error(e.getMessage());
            return Optional.empty();
        } catch (NullPointerException e) {
            APP_LOGGER.error(e.getMessage());
            return Optional.empty();
        }
    }

    protected <T> Optional<T> replaceFindOne(Query query, Class<T> clazz) {
        try {
            T result = authenticationTemplate.findOne(query, clazz);
            return Optional.of(result);
        } catch (IllegalArgumentException e) {
            APP_LOGGER.error(e.getMessage());
            return Optional.empty();
        } catch (NullPointerException e) {
            APP_LOGGER.error(e.getMessage());
            return Optional.empty();
        }
    }
}
