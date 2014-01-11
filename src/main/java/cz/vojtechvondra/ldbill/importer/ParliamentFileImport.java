package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import cz.vojtechvondra.ldbill.psp.PSPExport;
import cz.vojtechvondra.ldbill.vo.Parliament;

public class ParliamentFileImport extends FileImportStep<Parliament> {
    // TODO
    public static final String PARLIAMENT_AUTHORITY_RDF_URI = "http://linked.opendata.cz/resource/cz/authority/parliament";

    public ParliamentFileImport(PSPExport export, Model currentModel) {
        super(export, currentModel);
    }

    @Override
    protected Parliament createEntity(String[] line) {
        return new Parliament(line);
    }

    @Override
    protected void addEntityToModel(Model model, Parliament entity) {
        if (entity.getShortCode().matches("PSP[0-9]+")) {
            Resource parliament = model.createResource(entity.getRdfUri());
            parliament.addProperty(RDF.type, FOAF.Group);
            parliament.addProperty(DC.title, entity.getFullTitle());
            parliament.addProperty(DC.identifier, entity.getShortCode());
            parliament.addProperty(FOAF.name, entity.getFullTitle());
            parliament.addProperty(OWL.sameAs, model.createResource(PARLIAMENT_AUTHORITY_RDF_URI));
        } else {
            throw new IllegalStateException("Party entity without correct shortcode!");
        }
    }
}
