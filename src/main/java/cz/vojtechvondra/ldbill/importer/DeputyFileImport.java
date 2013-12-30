package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import cz.vojtechvondra.ldbill.entity.Deputy;
import cz.vojtechvondra.ldbill.entity.Organ;
import cz.vojtechvondra.ldbill.psp.PSPExport;


public class DeputyFileImport extends FileImport<Deputy> {

    public DeputyFileImport(PSPExport export, Model currentModel) {
        super(export, currentModel);
    }

    @Override
    protected Deputy createEntity(String[] line) {
        return new Deputy(line);
    }

    @Override
    protected void addEntityToModel(Model model, Deputy entity) {
        Resource r = currentModel.getResource(entity.getRdfUri());

        if (entity.getWebsite().length() > 0) {
            r.addProperty(FOAF.homepage, entity.getWebsite());
        }

        for (String email : entity.getEmails()) {
            r.addProperty(FOAF.mbox, email);
        }

        Organ party = new Organ(entity.getPartyId());
        r.addProperty(FOAF.member, model.createResource(party.getRdfUri()));
    }
}
