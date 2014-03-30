package cz.vojtechvondra.ldbill;

import cz.vojtechvondra.ldbill.psp.ConnectionFactory;
import org.apache.commons.cli.CommandLine;

/**
 * Static configuration holder for RDF converter
 */
public class Configuration {

    /**
     * If true, data will be re-imported into the relational database before converting to RDF
     */
    private boolean importData = false;

    /**
     * Database driver for importing data
     */
    private ConnectionFactory.JdbcDrivers jdbcDriver = ConnectionFactory.JdbcDrivers.MySQL;

    /**
     * Output format of converted data
     * Possible options are those accepted by {@link com.hp.hpl.jena.rdf.model.Model#write}
     */
    private String outputFormat = "TTL";

    private Configuration() {

    }

    public static Configuration fromCli(CommandLine cliOptions) {
        Configuration c = new Configuration();
        if (cliOptions.hasOption("i")) {
            c.importData = true;
        }


        return c;
    }

    public boolean shouldImportData() {
        return importData;
    }

    public ConnectionFactory.JdbcDrivers getJdbcDriver() {
        return jdbcDriver;
    }

    public String getOutputFormat() {
        return outputFormat;
    }
}
