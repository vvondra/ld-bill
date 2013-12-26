package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDFS;
import cz.vojtechvondra.ldbill.PSPExport;
import cz.vojtechvondra.ldbill.entity.Bill;
import cz.vojtechvondra.ldbill.vocabulary.LB;

public class BillAdapterStep extends AdapterStep<Bill> {
    public BillAdapterStep(PSPExport export, Model currentModel) {
        super(export, currentModel);
    }

    @Override
    protected Bill createEntity(String[] line) {
        return new Bill(line);
    }

    @Override
    protected void addEntityToModel(Model model, Bill entity) {
        Resource b = model.createResource(entity.getRdfUri());
        b.addProperty(DC.identifier, entity.getIdent());
        b.addProperty(DC.title, entity.getTitle());
        b.addProperty(DC.description, entity.getDescription());
        b.addProperty(RDFS.seeAlso, "http://www.psp.cz/sqw/historie.sqw?o=6&t=756");
        b.addProperty(LB.billSponsor, "http://linked.opendata.cz/resource/legislation/bill-sponsors#Representative");
    }
}
