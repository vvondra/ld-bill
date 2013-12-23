package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import cz.vojtechvondra.ldbill.PSPExport;
import cz.vojtechvondra.ldbill.entity.Organ;

public class ParliamentAdapterStep extends AdapterStep<Organ> {
    // TODO
    public static final String PARLIAMENT_AUTHORITY_RDF_URI = "http://linked.opendata.cz/resource/cz/authority/parliament";

    public ParliamentAdapterStep(PSPExport export, Model currentModel) {
        super(export, currentModel);
    }

    @Override
    protected Organ createEntity(String[] line) {
        return new Organ(line);
    }

    @Override
    protected void addEntityToModel(Model model, Organ entity) {
        if (entity.getShortCode().matches("PSP[0-9]+")) {
            Resource organ = model.createResource(entity.getRdfUri());
            organ.addProperty(RDF.type, FOAF.Group);
            organ.addProperty(DC.title, entity.getFullTitle());
            organ.addProperty(DC.identifier, entity.getShortCode());
            organ.addProperty(FOAF.name, entity.getFullTitle());
            organ.addProperty(OWL.sameAs, PARLIAMENT_AUTHORITY_RDF_URI);
        }
    }
}
