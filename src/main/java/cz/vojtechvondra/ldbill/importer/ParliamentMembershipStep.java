package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.OWL;
import cz.vojtechvondra.ldbill.entity.Organ;
import cz.vojtechvondra.ldbill.entity.Person;
import cz.vojtechvondra.ldbill.psp.PSPExport;

import java.io.IOException;

public class ParliamentMembershipStep implements ImportStep {

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
    public Model extendModel() {
        String[] data;

        while ((data = export.getLine()) != null) {
            Resource group = currentModel.createResource(Organ.getRdfPrefix() + data[1]);
            if (group.hasProperty(OWL.sameAs, ParliamentFileImport.PARLIAMENT_AUTHORITY_RDF_URI)) {
                if (data[2].equals(MEMBERSHIP_FLAG)) {
                    Person member = new Person(data[0]);
                    currentModel.createResource(member.getRdfUri()).addProperty(FOAF.member, group);
                }
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
