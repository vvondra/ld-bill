package cz.vojtechvondra.ldbill;

import cz.vojtechvondra.ldbill.exceptions.UnknownFormatException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class RdfLanguagesTest {

    @Test
    public void testDetectByExtension() {
        RdfLanguages lang = RdfLanguages.fromExtension("bills.ttl");
        Assert.assertEquals(RdfLanguages.TTL, lang);

        lang = RdfLanguages.fromExtension("bills.xml");
        Assert.assertEquals(RdfLanguages.RDFXML, lang);

        lang = RdfLanguages.fromExtension(new File("bills.xml"));
        Assert.assertEquals(RdfLanguages.RDFXML, lang);
    }

    @Test(expected = UnknownFormatException.class)
    public void testDetectByExtensionUnknown() {
        RdfLanguages lang = RdfLanguages.fromExtension("bills.foobar");
    }

}
