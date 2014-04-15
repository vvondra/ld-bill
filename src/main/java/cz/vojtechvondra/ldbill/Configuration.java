package cz.vojtechvondra.ldbill;

import cz.vojtechvondra.ldbill.psp.ConnectionFactory;
import org.apache.commons.cli.CommandLine;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Static configuration holder for RDF converter
 */
public class Configuration {

    private boolean importData = false;

    private ConnectionFactory.JdbcDrivers jdbcDriver = ConnectionFactory.JdbcDrivers.MySQL;

    private String databaseName = "";

    private String databaseUser = "root";

    private String databasePassword = "";

    private String outputFormat = "TTL";

    private File outputFile = new File(System.getProperty("user.home") + "/data.ttl");

    /**
     * Allowed formats for RDF conversion result file
     */
    private static final Set<String> allowedFormats = new HashSet<>(
            Arrays.asList(
                    new String[] {"TTL", "RDF", "RDF/XML"}
            ));

    private Configuration() {

    }

    /**
     * Create configuration from passed command line options
     * @param cliOptions arguments specified in CLI
     * @return Configuration for converter as specified in CLI
     */
    public static Configuration fromCli(CommandLine cliOptions) {
        Configuration c = new Configuration();
        if (cliOptions.hasOption("i")) {
            c.importData = true;
        }

        String driver = cliOptions.getOptionValue("d");
        if (driver != null) {
            switch (driver) {
                case "h2":
                    c.jdbcDriver = ConnectionFactory.JdbcDrivers.H2;
                    break;
                case "mysql":
                default:
                    c.jdbcDriver = ConnectionFactory.JdbcDrivers.MySQL;
            }
        }

        String val = cliOptions.getOptionValue("f");
        if (val != null && allowedFormats.contains(val)) {
            c.outputFormat = val;
        }

        val = cliOptions.getOptionValue("u");
        if (val != null) {
            c.databaseUser = val;
        }

        val = cliOptions.getOptionValue("p");
        if (val != null) {
            c.databasePassword = val;
        }

        val = cliOptions.getOptionValue("n");
        if (val != null) {
            c.databaseName = val;
        }

        val = cliOptions.getOptionValue("o");
        if (val != null) {
            c.outputFile = new File(val);
        }

        return c;
    }

    /**
     * If true, data will be re-imported into the relational database before converting to RDF
     */
    public boolean shouldImportData() {
        return importData;
    }

    /**
     * Database driver for importing data
     * Relevant even if {@link #shouldImportData()} is false, to fetch pre-imported data
     */
    public ConnectionFactory.JdbcDrivers getJdbcDriver() {
        return jdbcDriver;
    }

    /**
     * Output format of converted data
     * Possible options are those accepted by {@link com.hp.hpl.jena.rdf.model.Model#write}
     */
    public String getOutputFormat() {
        return outputFormat;
    }

    /**
     * Destination path for dataset model serialization
     */
    public File getOutputFile() {
        return outputFile;
    }

    /**
     * Database name
     * For MySQL in {host}/{dbName} format
     * @return database name
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * @return Database user for connecting to database
     */
    public String getDatabaseUser() {
        return databaseUser;
    }

    /**
     * @return Database password for connecting to database
     */
    public String getDatabasePassword() {
        return databasePassword;
    }
}
