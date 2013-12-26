package cz.vojtechvondra.ldbill;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import cz.vojtechvondra.ldbill.importer.*;
import org.apache.log4j.BasicConfigurator;

import java.io.*;
import java.net.URL;

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
    }

    protected static void importDeputies(Model dataset, PSPDownloader dataDownloader) {
        PartyAdapterStep oa = new PartyAdapterStep(new PSPExport(dataDownloader, "organy"), dataset);
        oa.extendModel();
        ParliamentAdapterStep paa = new ParliamentAdapterStep(new PSPExport(dataDownloader, "organy"), dataset);
        paa.extendModel();
        PersonAdapterStep pa = new PersonAdapterStep(new PSPExport(dataDownloader, "osoby"), dataset);
        pa.extendModel();
        DeputyAdapterStep da = new DeputyAdapterStep(new PSPExport(dataDownloader, "poslanec"), dataset);
        da.extendModel();
        ParliamentMembershipStep pms = new ParliamentMembershipStep(new PSPExport(dataDownloader, "zarazeni"), dataset);
        pms.extendModel();
        DeputyFilter df = new DeputyFilter(dataset);
        df.extendModel();
    }
}
