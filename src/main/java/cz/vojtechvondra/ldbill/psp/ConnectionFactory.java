package cz.vojtechvondra.ldbill.psp;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    static public Connection create(JdbcDrivers driver) throws SQLException, ClassNotFoundException {
        switch (driver) {
            case MySQL:
                return createMySQLConnection();
            case H2:
                return createH2Connection();
            default:
                throw new IllegalArgumentException("Unknown JDBC driver");
        }
    }

    static private Connection createMySQLConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost/ldbill?characterEncoding=utf8", "root", "");
    }

    static private Connection createH2Connection() throws ClassNotFoundException, SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:~/dev/bills.h2;LOG=0;CACHE_SIZE=65536;LOCK_MODE=0;UNDO_LOG=0");
        return ds.getConnection();
    }

    public enum JdbcDrivers {
        MySQL,
        H2
    }
}
