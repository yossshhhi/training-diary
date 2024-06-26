package kz.yossshhhi.util;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to map ResultSet data to Java objects.
 *
 * @param <T> The type of objects to map ResultSet data to.
 */
public class ResultSetMapper<T> implements ResultSetExtractor<List<T>> {

    /**
     * Represents the class type of the objects that will be mapped from the ResultSet.
     */
    private final Class<T> clazz;

    /**
     * Constructs a ResultSetMapper instance for mapping ResultSet data to objects of the specified class.
     *
     * @param clazz The class of objects to map ResultSet data to.
     */
    public ResultSetMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Maps the ResultSet data to a list of objects of type T.
     *
     * @param resultSet The ResultSet containing the data to be mapped.
     * @return A list of objects of type T.
     * @throws SQLException If an SQL error occurs while processing the ResultSet.
     * @throws DataAccessException If a data access error occurs.
     */
    @Override
    public List<T> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<T> resultList = new ArrayList<>();
        while (resultSet.next()) {
            T obj = mapResultSetToObject(resultSet);
            resultList.add(obj);
        }
        return resultList;
    }

    /**
     * Maps the current row of the ResultSet to an object of type T.
     *
     * @param resultSet The ResultSet containing the data to be mapped.
     * @return An object of type T mapped from the current row of the ResultSet.
     * @throws SQLException If an SQL error occurs while processing the ResultSet.
     */
    public T mapResultSetToObject(ResultSet resultSet) throws SQLException {
        T obj;
        try {
            obj = clazz.getDeclaredConstructor().newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String columnName = field.getName();
                Object value;

                try {
                    value = resultSet.getObject(convertToSnakeCase(columnName));
                } catch (Exception e) {
                    value = null;
                }
                if (field.getType().isEnum() && value != null) {
                    Class<?> enumType = field.getType();
                    for (Object enumConstant : enumType.getEnumConstants()) {
                        if (enumConstant.toString().equals(value.toString())) {
                            value = enumConstant;
                            break;
                        }
                    }
                } else if (field.getType().equals(LocalDate.class)) {
                    value = resultSet.getDate(convertToSnakeCase(columnName)).toLocalDate();
                } else if (field.getType().equals(LocalDateTime.class)) {
                    value = resultSet.getTimestamp(convertToSnakeCase(columnName)).toLocalDateTime();
                }

                if (value != null)
                    field.set(obj, value);
            }
        } catch (Exception ex) {
            throw new SQLException("Error mapping ResultSet to object: " + ex.getMessage(), ex);
        }
        return obj;
    }

    /**
     * Converts a camelCase string to snake_case.
     *
     * @param camelCase The camelCase string to convert.
     * @return The snake_case string.
     */
    private String convertToSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}
