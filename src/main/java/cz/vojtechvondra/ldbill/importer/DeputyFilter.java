package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.shared.PropertyNotFoundException;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;

import java.util.ArrayList;

/**
 * Filters out resources which are not valid deputies or miss necessary information
 */
public class DeputyFilter implements Step {

    private Model currentModel;

    public DeputyFilter(Model currentModel) {
        this.currentModel = currentModel;
    }

    @Override
    public Model getModel() {
        StmtIterator stmtIterator = currentModel.listStatements(null, RDF.type, FOAF.Person);
        ArrayList<Statement> toRemove = new ArrayList<>();
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            try {
                Resource person = stmt.getResource();
                person.getRequiredProperty(FOAF.member);
            } catch (PropertyNotFoundException e) {
                toRemove.add(stmt);
            }
        }

        currentModel.remove(toRemove);

        return currentModel;
    }
}
