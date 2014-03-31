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
        PartyFileImport oa = new PartyFileImport(createExportForDataset("organy"), dataset);
        oa.extendModel();
        ParliamentFileImport paa = new ParliamentFileImport(createExportForDataset("organy"), dataset);
        paa.extendModel();
        PersonFileImport pa = new PersonFileImport(createExportForDataset("osoby"), dataset);
        pa.extendModel();
        DeputyFileImport da = new DeputyFileImport(createExportForDataset("poslanec"), dataset);
        da.extendModel();
        ParliamentMembershipStep pms = new ParliamentMembershipStep(createExportForDataset("zarazeni"), dataset);
        pms.extendModel();
        DeputyFilter df = new DeputyFilter(dataset);
        df.extendModel();
    }

    /**
     * Factory method for creating an export service for a given dataset
     * @param name name of the dataset file
     * @return
     */
    protected PSPExport createExportForDataset(String name) {
        return new PSPExport(dataDownloader, name);
    }
}
