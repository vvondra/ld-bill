package cz.vojtechvondra.ldbill.importer;

import com.hp.hpl.jena.rdf.model.Model;

import java.sql.Connection;

/**
 * Base step implementation for steps using a connection to the temporary database
 */
public abstract class JdbcImportStep implements ImportStep {

    protected final Connection connection;
    protected final Model currentModel;

    /**
     * @param connection   connection to database with temporary data
     * @param currentModel model to be extended
     */
    public JdbcImportStep(Connection connection, Model currentModel) {
        this.connection = connection;
        this.currentModel = currentModel;
    }
}
