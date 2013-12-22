package cz.vojtechvondra.ldbill;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import cz.vojtechvondra.ldbill.importer.*;

public class Launcher {
    public static void main(String [] args)
    {
        Model dataset = ModelFactory.createDefaultModel();
        PSPDownloader dataDownloader = new PSPDownloader();
        OrganAdapter oa = new OrganAdapter(new PSPExport(dataDownloader, "organy"), dataset);
        oa.getModel();
        PersonAdapter pa = new PersonAdapter(new PSPExport(dataDownloader, "osoby"), dataset);
        pa.getModel();
        DeputyAdapter da = new DeputyAdapter(new PSPExport(dataDownloader, "poslanec"), dataset);
        da.getModel();
        PartyMembershipStep pms = new PartyMembershipStep(new PSPExport(dataDownloader, "zarazeni"), dataset);
        pms.getModel();
        DeputyFilter df = new DeputyFilter(dataset);
        df.getModel();
        dataset.write(System.out, "TTL");
    }
}
