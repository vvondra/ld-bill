package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;
import cz.vojtechvondra.ldbill.RdfLanguages;

import java.io.File;

/**
 * Imports a serialized RDF file (e.g. in RDF/XML or TTL) into the graph
 */
public class RdfImportStep implements ImportStep {

    /**
     * File to load RDF data from
     */
    private File file;

    /**
     * Model which will be extended
     */
    private Model currentModel;

    public RdfImportStep(File file, Model currentModel) {
        this.file = file;
        this.currentModel = currentModel;
    }

    @Override
    public Model extendModel() {
        RdfLanguages lang = RdfLanguages.fromExtension(file);
        Model fileModel = FileManager.get().loadModel(file.getAbsolutePath(), lang.getRdfSyntax());
        return currentModel.add(fileModel);
    }
}
