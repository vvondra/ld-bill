package cz.vojtechvondra.ldbill;

import org.apache.commons.cli.*;

import java.sql.SQLException;

public class Bootstrap {

    static public void main(String[] args) {
        CommandLineParser parser = new GnuParser();
        HelpFormatter formatter = new HelpFormatter();
        Options options = createOptions();

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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static private Options createOptions() {
        Options opts = new Options();

        opts.addOption("i", "import", false, "re-import data into relational database");
        opts.addOption("d", "dbms", true, "database management system to use (default mysql, h2)");
        opts.addOption("o", "output", true, "output file destination");
        opts.addOption("f", "format", true, "output file format (default ttl, rdf)");
        opts.addOption("h", false, "print this help message");

        return opts;
    }
}
