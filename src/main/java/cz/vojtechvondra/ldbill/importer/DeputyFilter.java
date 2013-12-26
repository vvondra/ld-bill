package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.shared.PropertyNotFoundException;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Filters out resources which are not valid deputies or miss necessary information
 */
public class DeputyFilter implements Step {

    static Logger logger = Logger.getLogger(DeputyFilter.class);

    private Model currentModel;

    public DeputyFilter(Model currentModel) {
        this.currentModel = currentModel;
    }

    @Override
    public Model getModel() {
        StmtIterator stmtIterator = currentModel.listStatements(null, RDF.type, FOAF.Person);
        ArrayList<Resource> toRemove = new ArrayList<>();
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            Resource person = stmt.getSubject();
            Property[] required = new Property[] { FOAF.member, FOAF.mbox };
            for (Property property : required) {
                try {
                    person.getRequiredProperty(property);
                } catch (PropertyNotFoundException e) {
                    toRemove.add(person);
                }
            }
        }

        for (Resource r : toRemove) {
            logger.debug("About to remove " + r.getURI());
            currentModel.removeAll(r, null, null);
        }

        return currentModel;
    }
}
