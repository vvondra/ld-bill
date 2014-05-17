package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import cz.vojtechvondra.ldbill.psp.PSPExport;
import cz.vojtechvondra.ldbill.vo.Parliament;

/**
 * Imports individual parliamentary sessions
 */
public class ParliamentFileImport extends FileImportStep<Parliament> {

    /**
     * URI representing the Czech parliament
     * From the LEX ontology
     */
    public static final String PARLIAMENT_AUTHORITY_RDF_URI = "http://linked.opendata.cz/resource/cz/authority/parliament";

    public ParliamentFileImport(PSPExport export, Model currentModel) {
        super(export, currentModel);
    }

    @Override
    protected Parliament createEntity(String[] line) {
        return new Parliament(line);
    }

    /**
     * Adds an RDF resource representing the parliamentary session to the model
     * @param model  RDF model
     * @param entity Parliamentary session to be added
     */
    @Override
    protected void addEntityToModel(Model model, Parliament entity) {
        if (entity.getShortCode().matches("PSP[0-9]+")) {
            Resource parliament = model.createResource(entity.getRdfUri());
            parliament.addProperty(RDF.type, FOAF.Group);
            parliament.addProperty(DCTerms.title, entity.getFullTitle());
            parliament.addProperty(DCTerms.identifier, entity.getShortCode());
            parliament.addProperty(FOAF.name, entity.getFullTitle());
            parliament.addProperty(OWL.sameAs, model.createResource(PARLIAMENT_AUTHORITY_RDF_URI));
        } else {
            throw new IllegalStateException("Party entity without correct shortcode!");
        }
    }
}
