package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;

import java.sql.Connection;

public abstract class H2ImportStep implements ImportStep {

    protected final Connection connection;
    protected final Model currentModel;

    /**
     * @param connection   connection to database with temporary data
     * @param currentModel model to be extended
     */
    public H2ImportStep(Connection connection, Model currentModel) {
        this.connection = connection;
        this.currentModel = currentModel;
    }
}
