package cz.vojtechvondra.ldbill.vo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeputyTest {
    @Test
    public void testCreateFromExport() throws Exception {
        Deputy d = new Deputy(new String[]{
                "a",
                "n",
                "c"
        });
        assertEquals("Boris", d.getFirstName());
        assertEquals("Štastný", d.getLastName());
        assertEquals("http://www.borisstastny.cz/", d.getWebsite());
        assertEquals("http://linked.opendata.cz/resource/psp.cz/person/5512", d.getRdfUri());
    }
}
