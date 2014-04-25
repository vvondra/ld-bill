package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

public class RdfImportStepTest {

    @Test
    public void testImportFile() throws URISyntaxException {
        Model dataset = ModelFactory.createDefaultModel();
        Resource r = dataset.createResource("http://fixture");
        r.addProperty(RDF.type, "http://type");
        RdfImportStep s = new RdfImportStep(new File(getClass().getResource("/rdftest.ttl").toURI()), dataset);
        s.extendModel();

        Assert.assertTrue(dataset.containsResource(r));
        Assert.assertEquals(5, dataset.listStatements().toList().size());
    }

}
