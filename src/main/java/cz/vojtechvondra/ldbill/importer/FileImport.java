package cz.vojtechvondra.ldbill.importer;


import com.hp.hpl.jena.rdf.model.Model;
import cz.vojtechvondra.ldbill.entity.Entity;
import cz.vojtechvondra.ldbill.psp.PSPExport;

import java.io.IOException;

abstract class FileImport<E extends Entity> implements Step {

    protected final PSPExport export;

    /**
     * Data loaded from previous adapters or other sources
     * Do not change, just for reference; unions of the models should be done outside the adapter
     */
    protected final Model currentModel;

    /**
     * Creates an export adapter allowing to specify already loaded data in the model
     * @param export exported data to be added to the model
     * @param currentModel already loaded data
     */
    public FileImport(PSPExport export, Model currentModel) {
        this.export = export;
        this.currentModel = currentModel;
    }

    /**
     * Factory abstract method
     * @param line parsed line from export
     * @return entity with initialized data
     */
    abstract protected E createEntity(String[] line);

    /**
     * Adds the specified entity to the RDF model with the appropriate properties
     * @param model RDF model
     * @param entity Entity to be added
     */
    protected abstract void addEntityToModel(Model model, E entity);

    /**
     * Get the RDF model resulting from importing the supplied PSPExport added to the current model
     * @return union of the current model and data from the export
     */
    public Model extendModel() {
        String[] data;

        while ((data = export.getLine()) != null) {
            E entity;
            try {
                entity = createEntity(data);
            } catch (IllegalArgumentException ignored) {
                /* Not a valid entity in the export file, continue */
                continue;
            }
            addEntityToModel(currentModel, entity);
        }

        try {
            export.close();
        } catch (IOException e) {
            /* Log in the future probably */
        }

        return currentModel;
    }
}
