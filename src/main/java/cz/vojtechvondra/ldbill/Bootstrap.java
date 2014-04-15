package cz.vojtechvondra.ldbill;

import cz.vojtechvondra.ldbill.exceptions.ConverterImportException;
import cz.vojtechvondra.ldbill.exceptions.ConverterOutputException;
import org.apache.commons.cli.*;

import java.sql.SQLException;

public class Bootstrap {

    static public void main(String[] args) {
        // CLI options setup
        CommandLineParser parser = new GnuParser();
        HelpFormatter formatter = new HelpFormatter();
        Options options = createOptions();

        // Run application
        try {
            CommandLine commandLineOpts = parser.parse(options, args);
            if (commandLineOpts.hasOption("h")) {
                formatter.printHelp("ldbill", options);
                System.exit(0);
            }

            Converter c = new Converter(Configuration.fromCli(commandLineOpts));
            c.convert();

        } catch (ParseException e) {
            System.err.println("Invalid arguments supplied: " + e.getMessage());
            formatter.printHelp("ldbill", options);
        } catch (ConverterImportException | ConverterOutputException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up a collection of options
     */
    static private Options createOptions() {
        Options opts = new Options();

        opts.addOption("i", "import", false, "re-import data into relational database");
        opts.addOption("d", "dbms", true, "database management system to use (default mysql, h2)");
        opts.addOption("u", "dbuser", true, "database user");
        opts.addOption("p", "dbpass", true, "database password");
        opts.addOption("n", "dbname", true, "database host/name");
        opts.addOption("o", "output", true, "output file destination");
        opts.addOption("f", "format", true, "output file format (default ttl, rdf)");
        opts.addOption("h", false, "print this help message");

        return opts;
    }
}
