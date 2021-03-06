package cz.vojtechvondra.ldbill.importer;


import com.hp.hpl.jena.rdf.model.Model;
import cz.vojtechvondra.ldbill.exceptions.ConverterImportException;

/**
 * Every implementation implements an extension of an RDF dataModel, adding or refining data
 */
public interface ImportStep {

    /**
     * Get the RDF model resulting from this step
     */
    Model extendModel() throws ConverterImportException;
}
