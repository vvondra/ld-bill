package cz.vojtechvondra.ldbill;

import com.hp.hpl.jena.rdf.model.Model;
import cz.vojtechvondra.ldbill.entity.PartyMember;

public class PartyMemberAdapter extends Adapter<PartyMember> {
    public PartyMemberAdapter(PSPExport export, Model currentModel) {
        super(export, currentModel);
    }

    @Override
    protected PartyMember createEntity(String[] line) {
        return new PartyMember(line);
    }

    @Override
    protected void addEntityToModel(Model model, PartyMember entity) {

    }
}
