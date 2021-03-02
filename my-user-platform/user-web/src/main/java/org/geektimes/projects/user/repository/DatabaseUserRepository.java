package org.geektimes.projects.user.repository;

import org.geektimes.function.ThrowableFunction;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.sql.DBConnectionManager;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.lang.ClassUtils.wrapperToPrimitive;

/**
 * 数据库 {@link UserRepository} 实现
 *
 * @since 1.0
 */
public class DatabaseUserRepository implements UserRepository {

    private static Logger logger = Logger.getLogger(DatabaseUserRepository.class.getName());

    private static Consumer<Throwable> COMMON_EXCEPTION_HANDLER = e -> logger.log(Level.SEVERE, e.getMessage());

    @Override
    public boolean save(User user) {
        return executeUpdate("INSERT INTO users(name, phoneNumber, email, password) VALUES(?, ?, ?, ?)",
                COMMON_EXCEPTION_HANDLER, user.getName(), user.getPhoneNumber(), user.getEmail(), user.getPassword());
    }

    @Override
    public boolean deleteById(Long userId) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User getById(Long userId) {
        return null;
    }

    @Override
    public User getByNameAndPassword(String userNam, String password) {
        return executeQuery("SELECT id,name,password,email,phoneNumber FROM users WHERE ( email = ? OR phoneNumber = ? ) AND password = ?", resultSet -> {
            BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
            if (resultSet.next()) {
                User user = new User();
                resultSetToPojo(resultSet, userBeanInfo, user);
                return user;
            } else {
                return null;
            }
        }, COMMON_EXCEPTION_HANDLER, userNam, userNam, password);
    }

    @Override
    public Collection<User> getAll() {
        return executeQuery("SELECT id,name,password,email,phoneNumber FROM users", resultSet -> {
            BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User();
                resultSetToPojo(resultSet, userBeanInfo, user);
                users.add(user);
            }
            return users;
        }, COMMON_EXCEPTION_HANDLER);
    }

    protected boolean executeUpdate(String sql, Consumer<Throwable> exceptionHandler, Object... args) {
        Connection connection = DBConnectionManager.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            handlePreparedStatementArgs(preparedStatement, args);
            preparedStatement.execute();
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            exceptionHandler.accept(e);
            return false;
        }
    }

    protected <T> T executeQuery(String sql, ThrowableFunction<ResultSet, T> function,
                                 Consumer<Throwable> exceptionHandler, Object... args) {
        Connection connection = DBConnectionManager.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            handlePreparedStatementArgs(preparedStatement, args);
            ResultSet resultSet = preparedStatement.executeQuery();
            return function.apply(resultSet);
        } catch (Throwable e) {
            e.printStackTrace();
            exceptionHandler.accept(e);
        }
        return null;
    }

    private void handlePreparedStatementArgs(PreparedStatement preparedStatement, Object... args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Class argType = arg.getClass();

            Class wrapperType = wrapperToPrimitive(argType);

            if (wrapperType == null) {
                wrapperType = argType;
            }

            String methodName = preparedStatementMethodMappings.get(wrapperType);
            Method method = PreparedStatement.class.getMethod(methodName, int.class, wrapperType);
            method.invoke(preparedStatement, i + 1, arg);
        }
    }

    private <T> void resultSetToPojo(ResultSet resultSet, BeanInfo userBeanInfo, T pojo) throws Exception {
        for (PropertyDescriptor propertyDescriptor : userBeanInfo.getPropertyDescriptors()) {
            String fieldName = propertyDescriptor.getName();
            Class fieldType = propertyDescriptor.getPropertyType();
            String methodName = resultSetMethodMappings.get(fieldType);
            // 可能存在映射关系（不过此处是相等的）
            String columnLabel = mapColumnLabel(fieldName);
            Method resultSetMethod = ResultSet.class.getMethod(methodName, String.class);
            Object resultValue = resultSetMethod.invoke(resultSet, columnLabel);

            Method setterMethodFromUser = propertyDescriptor.getWriteMethod();
            setterMethodFromUser.invoke(pojo, resultValue);
        }
    }

    private static String mapColumnLabel(String fieldName) {
        return fieldName;
    }

    /**
     * 类型与 ResultSet 方法名映射
     */
    static Map<Class, String> resultSetMethodMappings = new HashMap<>();


    static Map<Class, String> preparedStatementMethodMappings = new HashMap<>();

    static {
        resultSetMethodMappings.put(Long.class, "getLong");
        resultSetMethodMappings.put(String.class, "getString");

        preparedStatementMethodMappings.put(Long.class, "setLong");
        preparedStatementMethodMappings.put(String.class, "setString");
    }


}
