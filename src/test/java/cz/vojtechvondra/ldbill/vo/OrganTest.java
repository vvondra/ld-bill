package cz.vojtechvondra.ldbill.vo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrganTest {
    @Test
    public void testCreateFromExport() throws Exception {
        String[] oDef = {
                "151",
                "0",
                "6",
                "KDU", "Křesťanská a demokratická unie - Československá strana lidová",
                "Christian Democratic Union - Czechoslovak People's Party",
                "01.01.1900", "", "", "0", "", ""
        };
        Organ o = new Organ(oDef);
        assertEquals("KDU", o.getShortCode());
        assertEquals("Křesťanská a demokratická unie - Československá strana lidová", o.getFullTitle());
        assertEquals("151", o.getOrganId());
        assertEquals("http://linked.opendata.cz/resource/psp.cz/group/151", o.getRdfUri());
    }
}
