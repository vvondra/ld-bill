package cz.vojtechvondra.ldbill;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import cz.vojtechvondra.ldbill.exceptions.ConverterImportException;
import cz.vojtechvondra.ldbill.exceptions.ConverterOutputException;
import cz.vojtechvondra.ldbill.importer.*;
import cz.vojtechvondra.ldbill.psp.ConnectionFactory;
import cz.vojtechvondra.ldbill.psp.JdbcImport;
import cz.vojtechvondra.ldbill.psp.PSPDownloader;
import cz.vojtechvondra.ldbill.psp.PSPExport;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Converter {

    private final Configuration config;

    /**
     * psp.cz archive downloader
     */
    private final PSPDownloader dataDownloader;

    /**
     * The converted RDF dataset being iteratively constructed
     */
    private final Model dataset;

    public Converter(Configuration config) {
        this.config = config;
        dataDownloader = new PSPDownloader();
        dataset = ModelFactory.createDefaultModel();
    }

    /**
     * Executes the whole conversion process
     * @throws ConverterImportException thrown during an error in the data preparation phase
     * @throws ConverterOutputException throw during an error while outputting the resulting dataset
     */
    public void convert() throws ConverterImportException, ConverterOutputException {
        try {
            fileConverterStep();
            dbConverterStep();
        } catch (SQLException | ClassNotFoundException e) {
            throw new ConverterImportException("Could not import PSP dataset", e);
        }

        try (OutputStream os = new FileOutputStream(config.getOutputFile())) {
            dataset.write(os, config.getOutputFormat());
        } catch (IOException e) {
            throw new ConverterOutputException("Error during writing of output", e);
        }
    }

    /**
     * Executes conversion steps which need a relational databases
     * Database connection should not leak out of the scope of this method
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    protected void dbConverterStep() throws SQLException, ClassNotFoundException {
        try (Connection con = ConnectionFactory.create(config.getJdbcDriver())) {
            if (config.shouldImportData()) {
                JdbcImport.importAll(con, dataDownloader);
            }

            BillAdapterStep ba = new BillAdapterStep(con, dataset);
            ba.extendModel();
            VoteStep vs = new VoteStep(con, dataset);
            vs.extendModel();
        }
    }

    /**
     * Executes conversion steps which only work with flat files
     */
    protected void fileConverterStep() {
        List<ImportStep> steps = new ArrayList<>();
        
        steps.add(new PartyFileImport(createExportForDataset("organy"), dataset));
        steps.add(new ParliamentFileImport(createExportForDataset("organy"), dataset));
        steps.add(new PersonFileImport(createExportForDataset("osoby"), dataset));
        steps.add(new DeputyFileImport(createExportForDataset("poslanec"), dataset));
        steps.add(new ParliamentMembershipStep(createExportForDataset("zarazeni"), dataset));
        steps.add(new DeputyFilter(dataset));

        for (ImportStep step : steps) {
            step.extendModel();
        }
    }

    /**
     * Factory method for creating an export service for a given dataset
     * @param name name of the dataset file
     */
    protected PSPExport createExportForDataset(String name) {
        return new PSPExport(dataDownloader, name);
    }
}
