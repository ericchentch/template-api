package edunhnil.project.forum.api.dao;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import edunhnil.project.forum.api.exception.BadSqlException;
import edunhnil.project.forum.api.log.AppLogger;
import edunhnil.project.forum.api.log.LoggerFactory;
import edunhnil.project.forum.api.log.LoggerType;
import edunhnil.project.forum.api.utils.StringUtils;

public abstract class AbstractRepository {

    @Autowired
    @Qualifier("MainJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    protected AppLogger APP_LOGGER = LoggerFactory.getLogger(LoggerType.APPLICATION);

    protected <T> Optional<List<T>> replaceQuery(String sql, Class<T> clazz) {
        try {
            List<T> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(clazz));
            return Optional.of(list);
        } catch (BadSqlGrammarException e) {
            APP_LOGGER.error("ERROR SQL QUERY: " + sql);
            return Optional.empty();
        }
    }

    protected <T> Optional<T> replaceQueryForObjectWithId(String sql, Class<T> clazz, Object objectInput) {
        try {
            T object = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(clazz), objectInput);
            return Optional.of(object);
        } catch (BadSqlGrammarException e) {
            APP_LOGGER.error("ERROR SQL QUERY: " + sql);
            throw new BadSqlException("Internal Server Error");
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    protected String generateConditionInQuery(Field field, String value) {
        StringBuilder result = new StringBuilder();
        if (field.getType() == String.class) {
            result.append(StringUtils.camelCaseToSnakeCase(field.getName()))
                    .append(" ILIKE '%").append(value.toLowerCase()).append("%'");
        }
        if (field.getType() == int.class) {
            result.append(StringUtils.camelCaseToSnakeCase(field.getName()))
                    .append(" = ")
                    .append(value.toLowerCase());
        }
        return result.toString();
    }

    protected String convertParamsFilterSelectQuery(Map<String, String> allParams, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder result = new StringBuilder();
        boolean isFirstCondition = true;
        for (Map.Entry<String, String> items : allParams.entrySet()) {
            for (Field field : fields) {
                if (field.getName().compareTo(items.getKey()) == 0) {
                    if (isFirstCondition) {
                        result.append(" WHERE ");
                        isFirstCondition = false;
                    } else {
                        result.append(" AND ");
                    }
                    String[] values = items.getValue().split(",");
                    for (int i = 0; i < values.length; i++) {
                        result.append(generateConditionInQuery(field, values[i]));
                        if (i != values.length - 1)
                            result.append(" OR ");
                    }
                }
            }
        }
        return result.toString();
    }

    protected int getMax(String sql) {
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (NullPointerException e) {
            APP_LOGGER.error(e.getMessage());
            return 0;
        }
    }

    protected List<String> listAttributeName(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
    }

    protected String attributeNamesForSelect(Class<?> clazz) {
        List<String> listAttribute = listAttributeName(clazz);
        if (listAttribute == null || listAttribute.isEmpty()) {
            return "";
        }
        // Covert CamelCase to SnakeCase
        String attributeNames = listAttribute.stream().map(StringUtils::camelCaseToSnakeCase)
                .collect(Collectors.joining(","));
        return attributeNames;
    }

}
