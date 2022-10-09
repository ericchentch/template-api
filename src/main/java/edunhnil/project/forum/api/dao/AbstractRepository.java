package edunhnil.project.forum.api.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import edunhnil.project.forum.api.constant.FormInput;
import edunhnil.project.forum.api.exception.InternalServerException;
import edunhnil.project.forum.api.log.AppLogger;
import edunhnil.project.forum.api.log.LoggerFactory;
import edunhnil.project.forum.api.log.LoggerType;
import edunhnil.project.forum.api.utils.StringUtils;

public abstract class AbstractRepository {

    @Autowired
    @Qualifier("MainJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    protected AppLogger APP_LOGGER = LoggerFactory.getLogger(LoggerType.APPLICATION);

    protected boolean isFirstCondition = true;

    protected <T> Optional<List<T>> replaceQuery(String sql, Class<T> clazz) {
        try {
            List<T> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(clazz));
            return Optional.of(list);
        } catch (BadSqlGrammarException | NoSuchElementException e) {
            APP_LOGGER.error("ERROR SQL QUERY: " + sql);
            return Optional.of(new ArrayList<>());
        }
    }

    protected int getMax(String sql) {
        try {
            int result = jdbcTemplate.queryForObject(sql, Integer.class);
            return result;
        } catch (NullPointerException e) {
            APP_LOGGER.error(e.getMessage());
            return 0;
        }
    }

    protected String getPrefixSelect(String name) {
        String[] check = { "like", "post", "comment" };
        for (String field : check) {
            if (name.compareTo(field) == 0)
                return "forum.";
        }
        return "";
    }

    protected <T> void save(T entity, String[] ignoreFields) {
        StringBuilder sql = new StringBuilder();
        try {
            Field fieldId = entity.getClass().getDeclaredField("id");
            fieldId.setAccessible(true);
            jdbcTemplate.queryForObject(
                    "SELECT * FROM " + getPrefixSelect(entity.getClass().getSimpleName().toLowerCase())
                            + entity.getClass().getSimpleName().toLowerCase() + " WHERE id = "
                            + "'" + fieldId.get(entity) + "'",
                    new BeanPropertyRowMapper<>(entity.getClass()));
            sql.append("UPDATE " + getPrefixSelect(entity.getClass().getSimpleName().toLowerCase())
                    + entity.getClass().getSimpleName().toLowerCase() + " SET ");
            Field[] fields = entity.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if (fields[i].getType() == String.class) {
                    try {
                        sql.append(StringUtils.camelCaseToSnakeCase(fields[i].getName()) + "=" + "'"
                                + fields[i].get(entity) + "'");
                    } catch (IllegalArgumentException | IllegalAccessException e1) {
                        APP_LOGGER.error("Not found " + fields[i].getName() + " in "
                                + entity.getClass().getName());
                        throw new InternalServerException("something went wrong");
                    }
                }
                if (fields[i].getType() == int.class) {
                    try {
                        sql.append(StringUtils.camelCaseToSnakeCase(fields[i].getName()) + "=" + fields[i].get(entity));
                    } catch (IllegalArgumentException | IllegalAccessException e1) {
                        APP_LOGGER.error("Not found " + fields[i].getName() + " in "
                                + entity.getClass().getName());
                        throw new InternalServerException("something went wrong");
                    }
                }
                if (fields[i].getType() == Date.class) {
                    try {
                        Date date = (Date) fields[i].get(entity);
                        sql.append(StringUtils.camelCaseToSnakeCase(fields[i].getName()) + "=" + "'"
                                + new java.sql.Date(date.getTime()) + "'");
                    } catch (IllegalArgumentException | IllegalAccessException e1) {
                        APP_LOGGER.error("Not found " + fields[i].getName() + " in "
                                + entity.getClass().getName());
                        throw new InternalServerException("something went wrong");
                    }
                }
                if (i != fields.length - 1) {
                    sql.append(",");
                }
            }
            sql.append(" WHERE id='" + fieldId.get(entity) + "'");
        } catch (EmptyResultDataAccessException | NoSuchFieldException | IllegalArgumentException
                | IllegalAccessException e) {
            APP_LOGGER.info(e.getLocalizedMessage());
            StringBuilder fieldInsert = new StringBuilder();
            StringBuilder valueInsert = new StringBuilder();
            Field[] fields = entity.getClass().getDeclaredFields();
            sql.append("INSERT INTO " + getPrefixSelect(entity.getClass().getSimpleName().toLowerCase())
                    + entity.getClass().getSimpleName().toLowerCase());
            fieldInsert.append(" (");
            valueInsert.append(" (");
            for (int i = 0; i < fields.length; i++) {
                boolean isInsert = true;
                for (String ignore : ignoreFields) {
                    if (fields[i].getName().compareTo(ignore) == 0) {
                        isInsert = false;
                        break;
                    }
                }
                if (isInsert) {
                    fields[i].setAccessible(true);
                    fieldInsert.append(StringUtils.camelCaseToSnakeCase(fields[i].getName()));
                    if (fields[i].getType() == String.class) {
                        try {
                            valueInsert.append("'" + fields[i].get(entity) + "'");

                        } catch (IllegalArgumentException | IllegalAccessException e1) {
                            APP_LOGGER.error("Not found " + fields[i].getName() + " in " + entity.getClass().getName());
                            throw new InternalServerException("something went wrong");
                        }
                    }
                    if (fields[i].getType() == int.class) {
                        try {
                            valueInsert.append(fields[i].get(entity));
                        } catch (IllegalArgumentException | IllegalAccessException e1) {
                            APP_LOGGER.error("Not found " + fields[i].getName() + " in " + entity.getClass().getName());
                            throw new InternalServerException("something went wrong");
                        }
                    }
                    if (fields[i].getType() == Date.class) {
                        try {
                            Date date = (Date) fields[i].get(entity);
                            valueInsert.append("'" + new java.sql.Date(date.getTime()) + "'");
                        } catch (IllegalArgumentException | IllegalAccessException e1) {
                            APP_LOGGER.error("Not found " + fields[i].getName() + " in " + entity.getClass().getName());
                            throw new InternalServerException("something went wrong");
                        }
                    }
                    if (i != fields.length - 1) {
                        fieldInsert.append(",");
                        valueInsert.append(",");
                    }
                }
            }
            fieldInsert.append(")");
            valueInsert.append(")");
            sql.append(fieldInsert.toString() + " VALUES " + valueInsert.toString());
        } catch (BadSqlGrammarException e) {
            APP_LOGGER.error("Bad SQL: " + sql.toString());
            throw new InternalServerException("something went wrong");
        }
        try {
            jdbcTemplate.execute(sql.toString());
        } catch (BadSqlGrammarException e) {
            APP_LOGGER.error("BAD SQL: " + sql.toString());
            throw new InternalServerException("something went wrong");
        }
    }

    protected String insertFirstCondition(String type) {
        if (isFirstCondition) {
            isFirstCondition = false;
            return " WHERE ";
        }
        if (type == "date")
            return " OR ";
        return " AND ";
    }

    protected String generateConditionInQuery(Field field, String value) {
        StringBuilder result = new StringBuilder();
        if (field.getType() == String.class) {
            result.append(insertFirstCondition(""));
            result.append(StringUtils.camelCaseToSnakeCase(field.getName()))
                    .append(" ILIKE '%").append(value.toLowerCase()).append("%'");
        }
        if (field.getType() == int.class) {
            try {
                int valueParse = Integer.parseInt(value.toLowerCase());
                result.append(insertFirstCondition(""));
                result.append(StringUtils.camelCaseToSnakeCase(field.getName()))
                        .append(" = ")
                        .append(valueParse);
            } catch (NumberFormatException e) {
                APP_LOGGER.error("error parse number: " + field.getName() + " = " + value);
            }
        }
        if (field.getType() == Date.class) {
            if (value.matches(FormInput.DATE)) {
                result.append(insertFirstCondition(""));
                result.append(StringUtils.camelCaseToSnakeCase(field.getName()))
                        .append(" = ")
                        .append("'" + value + "'");
            }
        }
        return result.toString();
    }

    protected String generateDateCondition(Field field, String[] values) {
        StringBuilder result = new StringBuilder();
        if (values.length >= 2) {
            int max = values.length - values.length % 2;
            for (int i = 0; i < max; i += 2) {
                if (values[i].matches(FormInput.DATE) && values[i + 1].matches(FormInput.DATE)) {
                    result.append(insertFirstCondition("date"));
                    result.append(StringUtils.camelCaseToSnakeCase(field.getName()) + " >= '" + values[i] + "'");
                    result.append(" AND ");
                    result.append(StringUtils.camelCaseToSnakeCase(field.getName()) + " <= '" + values[i + 1] + "'");
                }
            }
        }
        return result.toString();
    }

    protected String convertParamsFilterSelectQuery(Map<String, String> allParams, Class<?> clazz, int page,
            int pageSize, String keySort, String sortField) {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> items : allParams.entrySet()) {
            for (Field field : fields) {
                if (field.getName().compareTo(items.getKey()) == 0) {
                    String[] values = items.getValue().split(",");
                    if (field.getType() == Date.class) {
                        result.append(generateDateCondition(field, values));
                    } else {
                        for (int i = 0; i < values.length; i++) {
                            result.append(generateConditionInQuery(field, values[i]));
                            if (i != values.length - 1)
                                result.append(" OR ");
                        }
                    }
                }
            }
        }
        isFirstCondition = true;
        if (keySort.trim().compareTo("") != 0 && sortField.trim().compareTo("") != 0) {
            result.append(" ORDER BY ").append(StringUtils.camelCaseToSnakeCase(sortField)).append(" ").append(keySort);
        }
        if (pageSize != 0) {
            result.append(" OFFSET ").append((page - 1) * pageSize).append(" ROWS FETCH NEXT ").append(pageSize)
                    .append(" ROWS ONLY");
        }
        return result.toString();
    }

    protected int getTotal(Map<String, String> allParams, Class<?> clazz) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM forum." + clazz.getSimpleName().toLowerCase());
        sql.append(convertParamsFilterSelectQuery(allParams, clazz, 0, 0, "", ""));
        try {
            int result = jdbcTemplate.queryForObject(sql.toString(), Integer.class);
            return result;
        } catch (NullPointerException e) {
            APP_LOGGER.error(e.getMessage());
            return 0;
        } catch (BadSqlGrammarException e) {
            APP_LOGGER.error("Bad SQL: " + sql.toString());
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
