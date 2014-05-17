package cz.vojtechvondra.ldbill.psp;

import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates a JDBC connection
 */
public class ConnectionFactory {

    private final static Logger logger = Logger.getLogger(ConnectionFactory.class);

    /**
     * Establishes a connection to the database with default credentials
     * @param driver Type of connection to create
     * @return Established connection to the database
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    static public Connection create(JdbcDrivers driver) throws SQLException, ClassNotFoundException {
        return ConnectionFactory.create(driver, "", "root", "");
    }

    /**
     * Establishes a connection to the database
     * @param driver Type of connection to create
     * @param database Name of the database
     * @param user Database use
     * @param password Database password
     * @return Established connection to the database
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    static public Connection create(JdbcDrivers driver, String database, String user, String password) throws SQLException, ClassNotFoundException {
        switch (driver) {
            case MySQL:
                return createMySQLConnection(database, user, password);
            case H2:
                return createH2Connection(database, user, password);
            default:
                throw new IllegalArgumentException("Unknown JDBC driver");
        }
    }

    static private Connection createMySQLConnection(String database, String user, String password) throws ClassNotFoundException, SQLException {
        if (database.equals("")) {
            database = "localhost/ldbill";
        }

        Class.forName("com.mysql.jdbc.Driver");
        String dsn = "jdbc:mysql://" + database + "?characterEncoding=utf8";
        logger.debug("MySQL JDBC DSN: " + dsn);
        return DriverManager.getConnection(dsn, user, password);
    }

    static private Connection createH2Connection(String database, String user, String password) throws ClassNotFoundException, SQLException {
        if (database.equals("")) {
            database = "~/bills.h2";
        }

        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:" + database + ";LOG=0;CACHE_SIZE=65536;LOCK_MODE=0;UNDO_LOG=0", user, password);
    }

    /**
     * Supported connection types
     */
    public enum JdbcDrivers {
        MySQL,
        H2
    }
}
