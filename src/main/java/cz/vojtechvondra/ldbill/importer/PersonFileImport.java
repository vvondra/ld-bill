package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.VCARD;
import cz.vojtechvondra.ldbill.psp.PSPExport;
import cz.vojtechvondra.ldbill.vo.Person;

import java.text.SimpleDateFormat;


public class PersonFileImport extends FileImportStep<Person> {

    public PersonFileImport(PSPExport export, Model currentModel) {
        super(export, currentModel);
    }

    @Override
    protected Person createEntity(String[] line) {
        return new Person(line);
    }

    @Override
    protected void addEntityToModel(Model model, Person entity) {
        Resource person = model.createResource(entity.getRdfUri());
        person.addProperty(RDF.type, FOAF.Person);
        person.addProperty(RDF.type, VCARD.NAME); // TODO check name
        person.addProperty(DCTerms.title, entity.getFirstName() + " " + entity.getLastName());
        person.addProperty(FOAF.firstName, entity.getFirstName());
        person.addProperty(FOAF.family_name, entity.getLastName()); // TODO change from deprecated
        if (entity.getTitleBeforeName().length() > 0) {
            person.addProperty(VCARD.Prefix, entity.getTitleBeforeName());
        }
        if (entity.getTitleAfterName().length() > 0) {
            person.addProperty(VCARD.Suffix, entity.getTitleAfterName());
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        person.addProperty(VCARD.BDAY, format.format(entity.getDateOfBirth()));
    }
}
