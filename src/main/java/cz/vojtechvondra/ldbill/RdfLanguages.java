package cz.vojtechvondra.ldbill;

import com.hp.hpl.jena.util.FileUtils;
import com.hp.hpl.jena.vocabulary.RDF;
import cz.vojtechvondra.ldbill.exceptions.UnknownFormatException;
import org.apache.jena.riot.Lang;

import java.io.File;
import java.net.URLConnection;

/**
 * Supported RDF serialization formats
 */
public enum RdfLanguages {

    RDFXML(Lang.RDFXML, FileUtils.langXML),
    TTL(Lang.TTL, FileUtils.langTurtle);

    /**
     * Jena language equivalent
     */
    private Lang lang;
    private String rdfSyntax;

    RdfLanguages(Lang lang, String rdfSyntax) {
        this.lang = lang;
        this.rdfSyntax = rdfSyntax;
    }

    /**
     * @return Return Jena representation of language
     */
    public Lang getLang() {
        return lang;
    }

    /**
     * @return Jena file syntax identifier
     */
    public String getRdfSyntax() {
        return rdfSyntax;
    }

    /**
     * Detect language based on file
     * @param file File to detect serialization format from
     * @return serialization format used
     */
    public static RdfLanguages fromExtension(File file) {
        return fromExtension(file.getName());
    }

    /**
     * Detect language based on file
     * @param name Name of file to detect serialization format from
     * @return serialization format used
     */
    public static RdfLanguages fromExtension(String name) {
        // Try by mime type
        String mimeType = URLConnection.guessContentTypeFromName(name);
        if (mimeType != null ) {
            switch (mimeType) {
                case "application/rdf+xml":
                    return RDFXML;
                case "text/rdf+n3":
                case "text/turtle":
                    return TTL;
            }
        }

        // Try manually by extension
        if (name.endsWith("ttl")) {
            return TTL;
        } else if (name.endsWith("xml")) {
            return RDFXML;
        }

        throw new UnknownFormatException("Cannot detect RDF serialization format from file: " + name);
    }
}
