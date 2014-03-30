package cz.vojtechvondra.ldbill;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import cz.vojtechvondra.ldbill.importer.*;
import cz.vojtechvondra.ldbill.psp.ConnectionFactory;
import cz.vojtechvondra.ldbill.psp.JdbcImport;
import cz.vojtechvondra.ldbill.psp.PSPDownloader;
import cz.vojtechvondra.ldbill.psp.PSPExport;
import org.apache.log4j.BasicConfigurator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class Converter {

    private final Configuration config;

    public Converter(Configuration config) {
        this.config = config;
    }

    public void convert() throws SQLException, ClassNotFoundException {
        BasicConfigurator.configure();
        Model dataset = ModelFactory.createDefaultModel();

        PSPDownloader dataDownloader = new PSPDownloader();
        importDeputies(dataset, dataDownloader);
        importBills(dataset, dataDownloader);
        try {
            dataset.write(new FileOutputStream(new File(System.getProperty("user.home") + "/data.ttl")), config.getOutputFormat());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void importBills(Model dataset, PSPDownloader dataDownloader) throws SQLException, ClassNotFoundException {
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

    protected void importDeputies(Model dataset, PSPDownloader dataDownloader) {
        PartyFileImport oa = new PartyFileImport(new PSPExport(dataDownloader, "organy"), dataset);
        oa.extendModel();
        ParliamentFileImport paa = new ParliamentFileImport(new PSPExport(dataDownloader, "organy"), dataset);
        paa.extendModel();
        PersonFileImport pa = new PersonFileImport(new PSPExport(dataDownloader, "osoby"), dataset);
        pa.extendModel();
        DeputyFileImport da = new DeputyFileImport(new PSPExport(dataDownloader, "poslanec"), dataset);
        da.extendModel();
        ParliamentMembershipStep pms = new ParliamentMembershipStep(new PSPExport(dataDownloader, "zarazeni"), dataset);
        pms.extendModel();
        DeputyFilter df = new DeputyFilter(dataset);
        df.extendModel();
    }
}
