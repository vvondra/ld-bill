package cz.vojtechvondra.ldbill.importer;


import com.hp.hpl.jena.rdf.model.Model;

public interface Step {

    /**
     * Get the RDF model resulting from this step
     */
    Model getModel();
}
