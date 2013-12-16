package cz.vojtechvondra.ldbill;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class Launcher {
    public static void main(String [] args)
    {
        Model dataset = ModelFactory.createDefaultModel();
        PSPDownloader dataDownloader = new PSPDownloader();
        OrganAdapter oa = new OrganAdapter(new PSPExport(dataDownloader, "organy"));
        PersonAdapter pa = new PersonAdapter(new PSPExport(dataDownloader, "osoby"));
        Model organs = oa.getModel();
        organs.write(System.out, "TTL");
        Model people = pa.getModel();
        people.write(System.out, "TTL");
        dataset.union(organs);
        dataset.union(people);
        dataset.write(System.out, "TTL");
    }
}
