package cz.vojtechvondra.ldbill;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.VCARD;
import cz.vojtechvondra.ldbill.entity.Deputy;


public class DeputyAdapter extends Adapter<Deputy> {

    public DeputyAdapter(PSPExport export, Model currentModel) {
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
    }
}
