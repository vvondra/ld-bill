package cz.vojtechvondra.ldbill.vo;

import com.hp.hpl.jena.rdf.model.Property;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class BillTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDate() {
        new Bill("123", "invalidDate", "title", "description", "0");
    }

    @Test
    public void testParseFromExport() {
        Bill b = new Bill("123", "5.6.1992", "title", "description", "0");
        assertEquals("1992", b.getYear());
        assertEquals("1992/123", b.getIdent());
        assertTrue(b.getBillSponsor() instanceof Property);
        assertEquals("http://linked.opendata.cz/resource/legislation/cz/bill/1992/123", b.getRdfUri());
        assertEquals("title", b.getTitle());
        assertEquals("description", b.getDescription());
    }
}
