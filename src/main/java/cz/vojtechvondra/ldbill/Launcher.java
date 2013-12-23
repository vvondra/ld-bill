package cz.vojtechvondra.ldbill;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import cz.vojtechvondra.ldbill.importer.*;
import org.apache.log4j.BasicConfigurator;

public class Launcher {
    public static void main(String [] args)
    {
        BasicConfigurator.configure();
        Model dataset = ModelFactory.createDefaultModel();
        PSPDownloader dataDownloader = new PSPDownloader();
        OrganAdapterStep oa = new OrganAdapterStep(new PSPExport(dataDownloader, "organy"), dataset);
        oa.getModel();
        PersonAdapterStep pa = new PersonAdapterStep(new PSPExport(dataDownloader, "osoby"), dataset);
        pa.getModel();
        DeputyAdapterStep da = new DeputyAdapterStep(new PSPExport(dataDownloader, "poslanec"), dataset);
        da.getModel();
        ParliamentMembershipStep pms = new ParliamentMembershipStep(new PSPExport(dataDownloader, "zarazeni"), dataset);
        pms.getModel();
        DeputyFilter df = new DeputyFilter(dataset);
        df.getModel();
        dataset.write(System.out, "TTL");
    }
}
