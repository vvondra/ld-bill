package cz.vojtechvondra.ldbill;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import cz.vojtechvondra.ldbill.importer.*;
import cz.vojtechvondra.ldbill.psp.H2Import;
import cz.vojtechvondra.ldbill.psp.PSPDownloader;
import cz.vojtechvondra.ldbill.psp.PSPExport;
import org.apache.log4j.BasicConfigurator;
import org.h2.jdbcx.JdbcDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class Launcher {
    public static void main(String [] args)
    {
        BasicConfigurator.configure();
        Model dataset = ModelFactory.createDefaultModel();
        //OntModel ontology = ModelFactory.createOntologyModel();
        //URL res = Launcher.class.getResource("bills.ttl");

        PSPDownloader dataDownloader = new PSPDownloader();
        importDeputies(dataset, dataDownloader);
        importBills(dataset, dataDownloader);
        try {
            dataset.write(new FileOutputStream(new File(System.getProperty("user.home") + "/data.ttl")), "TTL");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //dataset.write(System.out, "TTL");
    }

    protected static void importBills(Model dataset, PSPDownloader dataDownloader) {
        Connection con;
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:~/dev/bills.h2");
        try {
            con = ds.getConnection();
            H2Import.importAll(con, dataDownloader);
            BillAdapterStep ba = new BillAdapterStep(con, dataset);
            ba.extendModel();
            VoteStep vs = new VoteStep(con, dataset);
            vs.extendModel();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected static void importDeputies(Model dataset, PSPDownloader dataDownloader) {
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
