package cz.vojtechvondra.ldbill;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.VCARD;
import cz.vojtechvondra.ldbill.entity.Person;


public class PersonAdapter extends Adapter<Person> {

    public PersonAdapter(PSPExport export) {
        super(export);
    }

    @Override
    protected Person createEntity(String[] line) {
        return new Person(line);
    }

    @Override
    protected void addEntityToModel(Model model, Person entity) {
        Resource person = model.createResource(entity.getRdfUri());
        person.addProperty(DC.title, entity.getFirstName() + " " + entity.getLastName());
        person.addProperty(FOAF.firstName, entity.getFirstName());
        person.addProperty(FOAF.family_name, entity.getLastName());
        if (entity.getTitleBeforeName().length() > 0) {
            person.addProperty(VCARD.Prefix, entity.getTitleBeforeName());
        }
        if (entity.getTitleAfterName().length() > 0) {
            person.addProperty(VCARD.Suffix, entity.getTitleAfterName());
        }
        person.addProperty(VCARD.BDAY, entity.getDateOfBirth().toString());
    }
}
