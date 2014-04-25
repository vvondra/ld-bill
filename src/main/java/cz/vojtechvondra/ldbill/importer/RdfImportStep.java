package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.n3.turtle.TurtleReader;
import com.hp.hpl.jena.rdf.arp.JenaReader;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.util.FileManager;
import cz.vojtechvondra.ldbill.RdfLanguages;
import cz.vojtechvondra.ldbill.exceptions.ConverterImportException;

import java.io.*;

/**
 * Imports a serialized RDF file (e.g. in RDF/XML or TTL) into the graph
 */
public class RdfImportStep implements ImportStep, Closeable {

    /**
     * File to load RDF data from
     */
    private InputStream file;

    /**
     * Serialization format of imported file
     */
    private RdfLanguages format;

    /**
     * Model which will be extended
     */
    private Model currentModel;

    private boolean isFinished = false;

    public RdfImportStep(InputStream file, RdfLanguages format, Model currentModel) {
        this.file = file;
        this.format = format;
        this.currentModel = currentModel;
    }

    public RdfImportStep(File file, Model currentModel) throws ConverterImportException {
        try {
            this.file = new FileInputStream(file);
            format = RdfLanguages.fromExtension(file);
        } catch (FileNotFoundException e) {
            throw new ConverterImportException("Could not open RDF file.", e);
        }
        this.currentModel = currentModel;
    }

    @Override
    public Model extendModel() throws ConverterImportException {
        if (isFinished) {
            throw new ConverterImportException("Cannot load same RDF file twice in RdfImportStep");
        }
        RDFReader reader;
        switch (format) {
            case RDFXML:
                reader = new JenaReader();
                break;
            case TTL:
                reader = new TurtleReader();
                break;
            default:
                throw new ConverterImportException("Unknown format: " + format.toString());
        }

        reader.read(currentModel, file, null);
        isFinished = true;
        return currentModel;
    }

    @Override
    public void close() throws IOException {
        file.close();
        isFinished = true;
    }
}
