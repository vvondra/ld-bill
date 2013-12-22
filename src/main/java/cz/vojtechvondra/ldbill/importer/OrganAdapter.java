package cz.vojtechvondra.ldbill.importer;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import cz.vojtechvondra.ldbill.PSPExport;
import cz.vojtechvondra.ldbill.entity.Organ;

public class OrganAdapter extends Adapter<Organ> {

    public OrganAdapter(PSPExport organExport, Model currentModel) {
        super(organExport, currentModel);
    }

    @Override
    protected Organ createEntity(String[] line) {
        return new Organ(line);
    }

    /**
     * Adds the organ entity to the RDF model with the correct predicate names
     * @param organModel model being built
     * @param o Organ entity with data
     */
    protected void addEntityToModel(Model organModel, Organ o) {
        Resource organ = organModel.createResource(o.getRdfUri());
        organ.addProperty(RDF.type, FOAF.Group);
        organ.addProperty(DC.title, o.getFullTitle());
        organ.addProperty(DC.identifier, o.getShortCode());
        organ.addProperty(FOAF.name, o.getFullTitle());
    }
}
