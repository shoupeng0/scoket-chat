package site.prodigal.utils;
import site.prodigal.constants.StringConstants;

import java.sql.*;

public class DBUtils {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(StringConstants.URL, StringConstants.USERNAME, StringConstants.PASSWORD);
    }

    public static void release(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
