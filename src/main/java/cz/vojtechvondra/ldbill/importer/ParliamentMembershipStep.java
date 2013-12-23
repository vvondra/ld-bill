package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;
import cz.vojtechvondra.ldbill.PSPExport;

import java.io.IOException;

public class ParliamentMembershipStep implements Step {

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
            if (data[1].equals("170") && data[2].equals("0")) {

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
