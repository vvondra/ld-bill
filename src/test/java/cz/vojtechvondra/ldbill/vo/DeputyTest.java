package cz.vojtechvondra.ldbill.vo;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DeputyTest {
    @Test
    public void testCreateFromExport() throws Exception {
        Deputy d = new Deputy(new String[]{
                "0",
                "5512",
                "",
                "123",
                "",
                "http://www.borisstastny.cz/",
                "adresa",
                "mesto",
                "",
                "boris@stastny.cz,stastny@psp.cz,"


        });
        assertEquals("123", d.getPartyId());
        assertEquals("http://www.borisstastny.cz/", d.getWebsite());
        assertEquals("http://linked.opendata.cz/resource/psp.cz/person/5512", d.getRdfUri());
        assertArrayEquals(new String[] {"boris@stastny.cz", "stastny@psp.cz"}, d.getEmails());
    }
}
