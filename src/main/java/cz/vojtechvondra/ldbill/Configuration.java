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


    public static Configuration fromCli(CommandLine cliOptions) {
        Configuration c = new Configuration();
        if (cliOptions.hasOption("i")) {
            c.importData = true;
        }

        String driver = cliOptions.getOptionValue("d");
        switch (driver) {
            case "h2":
                c.jdbcDriver = ConnectionFactory.JdbcDrivers.H2;
                break;
            case "mysql":
            default:
                c.jdbcDriver = ConnectionFactory.JdbcDrivers.MySQL;
        }

        String format = cliOptions.getOptionValue("f");
        if (format != null && allowedFormats.contains(format)) {
            c.outputFormat = format;
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
     * Relevant if {@link #shouldImportData()} is true
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
}
