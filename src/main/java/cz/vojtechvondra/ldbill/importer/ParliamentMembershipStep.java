package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.OWL;
import cz.vojtechvondra.ldbill.PSPExport;
import cz.vojtechvondra.ldbill.entity.Organ;

import java.io.IOException;

public class ParliamentMembershipStep implements Step {

    /**
     * Value marking active membership in group in PSP export
     */
    public static final String MEMBERSHIP_FLAG = "0";
    private final PSPExport export;
    private final Model currentModel;

    /**
     * @param export export data of person to deputy pairings
     * @param currentModel loaded model with deputies, organs and people
     */
    public ParliamentMembershipStep(PSPExport export, Model currentModel) {
        this.export = export;
        this.currentModel = currentModel;
    }

    @Override
    public Model getModel() {
        String[] data;

        while ((data = export.getLine()) != null) {
            Resource group = currentModel.createResource(Organ.getRdfPrefix() + data[1]);
            if (group.hasProperty(OWL.sameAs, ParliamentAdapterStep.PARLIAMENT_AUTHORITY_RDF_URI) && data[2].equals(MEMBERSHIP_FLAG)){

            }
        }

        try {
            export.close();
        } catch (IOException e) {
            /* Log in the future probably */
        }

        return currentModel;
    }
}
