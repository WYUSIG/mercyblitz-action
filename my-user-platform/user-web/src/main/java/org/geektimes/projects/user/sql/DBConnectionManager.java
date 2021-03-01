package org.geektimes.projects.user.sql;

import java.sql.*;

/**
 * JDBC {@link java.sql.Connection}管理类
 *
 * @since 1.0
 */
public class DBConnectionManager {

    private static Connection connection;

    private DBConnectionManager() {
    }

    /**
     * 使用双端锁检测实现derby数据库只能用一个连接
     *
     * @return
     */
    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        synchronized (DBConnectionManager.class) {
            if (connection == null) {
                String databaseURL = "jdbc:derby:/db/user-platform;create=true";
                try {
                    connection = DriverManager.getConnection(databaseURL);
                    Statement statement = connection.createStatement();
                    // 删除 users 表
                    statement.execute(DROP_USERS_TABLE_DDL_SQL); // false
                    // 创建 users 表
                    statement.execute(CREATE_USERS_TABLE_DDL_SQL); // false
                    int count = statement.executeUpdate(INSERT_USER_DML_SQL); // 5
                    System.out.println("往Users表插入了" + count + "行数据");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return connection;
    }

    public static final String DROP_USERS_TABLE_DDL_SQL = "DROP TABLE users";

    public static final String CREATE_USERS_TABLE_DDL_SQL = "CREATE TABLE users(" +
            "id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "name VARCHAR(16) NOT NULL, " +
            "password VARCHAR(64) NOT NULL, " +
            "email VARCHAR(64) NOT NULL, " +
            "phoneNumber VARCHAR(64) NOT NULL" +
            ")";

    public static final String INSERT_USER_DML_SQL = "INSERT INTO users(name,password,email,phoneNumber) VALUES " +
            "('A','******','a@gmail.com','1') , " +
            "('B','******','b@gmail.com','2') , " +
            "('C','******','c@gmail.com','3') , " +
            "('D','******','d@gmail.com','4') , " +
            "('E','******','e@gmail.com','5')";
}
