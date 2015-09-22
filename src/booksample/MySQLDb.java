package booksample;

import java.sql.*;
import java.util.*;

/**
 *
 * @author Matthew Cromby
 */
public class MySQLDb {

    private Connection conn;

    /**
     *
     * @param driverClass
     * @param url
     * @param userName
     * @param password
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void openConnection(String driverClass, String url, String userName, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driverClass);
        conn = DriverManager.getConnection(url, userName, password);
    }

    /**
     *
     * @throws SQLException
     */
    public void closeConnection() throws SQLException {
        conn.close();
    }

    /**
     *
     * @param tableName
     * @param columnName
     * @param value
     * @throws SQLException
     */
    public void deleteRecord(String tableName, String columnName, Object value) throws SQLException {
        String sql;
        if (value instanceof String) {
            sql = "Delete From " + tableName + " Where " + columnName + " = '" + value + "';";
        } else {
            sql = "Delete From " + tableName + " Where " + columnName + " = " + value + ";";
        }
        Statement stmt = conn.createStatement();
        int count = stmt.executeUpdate(sql);
        System.out.println(count);
    }

    /**
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> findAllRecords(String tableName) throws SQLException {
        List<Map<String, Object>> records = new ArrayList<>();
        String sql = "Select * From " + tableName;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        System.out.println(columnCount);
        while (rs.next()) {
            Map<String, Object> record = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                record.put(metaData.getCatalogName(i), rs.getObject(i));
            }
            records.add(record);
        }
        return records;
    }

    public static void main(String[] args) throws Exception {
        String driverClassName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/book";
        String userName = "root";
        String password = "admin";
        MySQLDb dB = new MySQLDb();
        dB.openConnection(driverClassName, url, userName, password);
        dB.deleteRecord("author", "author_name", "Sally Smith");
        List<Map<String, Object>> results;
        results = dB.findAllRecords("author");
        for (Map record : results) {
            System.out.println(record);
        }
        dB.closeConnection();

    }

}
