package dataAccess;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import connection.ConnectionFactory;

/**
 * Generic Data Access Object that provides common database operations such as
 * findById, findAll, insert, and update for any type T using reflection.
 * @param <T> The type of the data model.
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    protected final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    /**
     * Finds all entries of type T.
     * @return A list of all entries.
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + type.getSimpleName();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }


    /**
     * Finds a single entry by ID.
     * @param id The ID of the entry.
     * @return The entry found, or null if not found.
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            List<T> results = createObjects(resultSet);
            if (results.isEmpty()) {
                return null;
            }
            return results.get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }


    /**
     * Creates a list of objects from a ResultSet using reflection.
     * @param resultSet The result set from a query.
     * @return List of objects of type T.
     */
    List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        ctor = Arrays.stream(ctors)
                .filter(c -> c.getGenericParameterTypes().length == 0)
                .findFirst()
                .orElse(null);
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                Arrays.stream(type.getDeclaredFields()).forEach(field -> {
                    try {
                        String fieldName = field.getName();
                        Object value = resultSet.getObject(fieldName);

                        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                        Method method = propertyDescriptor.getWriteMethod();
                        Class<?> paramType = method.getParameterTypes()[0];

                        if (value != null) {
                            if (paramType == int.class || paramType == Integer.class) {
                                value = ((Number) value).intValue();
                            } else if (paramType == long.class || paramType == Long.class) {
                                value = ((Number) value).longValue();
                            } else if (paramType == double.class || paramType == Double.class) {
                                value = ((Number) value).doubleValue();
                            } else if (paramType == float.class || paramType == Float.class) {
                                value = ((Number) value).floatValue();
                            } else if (paramType == boolean.class || paramType == Boolean.class) {
                                value = (Boolean) value;
                            } else if (paramType == String.class) {
                                value = value.toString();
                            }
                        }
                        method.invoke(instance, value);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Inserts an object into the database.
     * @param t The object to insert.
     * @return The inserted object.
     */
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(type.getSimpleName());
        query.append(" (");

        Field[] fields = type.getDeclaredFields();
        for (int i = 1; i < fields.length; i++) {
            query.append(fields[i].getName());
            if (i < fields.length - 1)
                query.append(", ");
        }
        query.append(") VALUES (");
        String placeholders = IntStream.range(1, fields.length)
                .mapToObj(i -> "?")
                .collect(Collectors.joining(", "));
        query.append(placeholders);
        query.append(")");

        try {
            connection = ConnectionFactory.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query.toString());
            statement = stmt;

            IntStream.range(1, fields.length).forEach(i -> {
                try {
                    fields[i].setAccessible(true);
                    Object value = fields[i].get(t);

                    if (value instanceof java.util.Date && !(value instanceof java.sql.Date)) {
                        value = new java.sql.Date(((java.util.Date) value).getTime());
                    }

                    stmt.setObject(i, value);
                } catch (IllegalAccessException | SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return t;
    }

    /**
     * Updates an existing object in the database.
     * @param t The object to update.
     */
    public void update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder query = new StringBuilder();
        query.append("UPDATE ");
        query.append(type.getSimpleName());
        query.append(" SET ");

        Field[] fields = type.getDeclaredFields();
        IntStream.range(1, fields.length).forEach(i -> {
            query.append(fields[i].getName()).append(" = ");
            query.append("?");
            if (i < fields.length - 1)
                query.append(", ");
        });
        query.append(" WHERE ");
        query.append(fields[0].getName()).append(" = ?");

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query.toString());
            for (int i = 1; i < fields.length; i++) {
                fields[i].setAccessible(true);
                statement.setObject(i, fields[i].get(t));
            }
            fields[0].setAccessible(true);
            statement.setObject(fields.length, fields[0].get(t));
            statement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

}
