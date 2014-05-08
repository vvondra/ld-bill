package cz.vojtechvondra.ldbill.importer;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import cz.vojtechvondra.ldbill.psp.PSPExport;
import cz.vojtechvondra.ldbill.vo.Party;

public class PartyFileImport extends FileImportStep<Party> {

    public PartyFileImport(PSPExport organExport, Model currentModel) {
        super(organExport, currentModel);
    }

    @Override
    protected Party createEntity(String[] line) {
        return new Party(line);
    }

    /**
     * Adds the organ entity to the RDF model with the correct predicate names
     *
     * @param organModel model being built
     * @param o          Organ entity with data
     */
    protected void addEntityToModel(Model organModel, Party o) {
        Resource party = organModel.createResource(o.getRdfUri());
        party.addProperty(RDF.type, FOAF.Group);
        party.addProperty(DCTerms.title, o.getFullTitle());
        party.addProperty(DCTerms.identifier, o.getShortCode());
        party.addProperty(FOAF.name, o.getFullTitle());
    }
}
