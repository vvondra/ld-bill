package cz.vojtechvondra.ldbill.vo;

import cz.vojtechvondra.ldbill.exceptions.UnsupportedRowFormatException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrganTest {
    @Test
    public void testCreateOrganFromExport() throws Exception {
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

    @Test(expected = UnsupportedRowFormatException.class)
    public void testCreatePartyFromExportNotAParty() throws Exception {
        String[] oDef = {
                "151",
                "0",
                "22",
                "KDU", "Křesťanská a demokratická unie - Československá strana lidová",
                "Christian Democratic Union - Czechoslovak People's Party",
                "01.01.1900", "", "", "0", "", ""
        };
        new Party(oDef);
    }

    @Test(expected = UnsupportedRowFormatException.class)
    public void testCreateParliamentEntryWithIncorrectData() throws Exception {
        String[] oDef = {
                "151",
                "0",
                "6",
                "KDU", "Křesťanská a demokratická unie - Československá strana lidová",
                "Christian Democratic Union - Czechoslovak People's Party",
                "01.01.1900", "", "", "0", "", ""
        };
        Organ o = new Parliament(oDef);
        assertEquals("KDU", o.getShortCode());
        assertEquals("Křesťanská a demokratická unie - Československá strana lidová", o.getFullTitle());
        assertEquals("151", o.getOrganId());
        assertEquals("http://linked.opendata.cz/resource/psp.cz/group/151", o.getRdfUri());
    }

    @Test
    public void testCreateParliamentEntry() throws Exception {
        String[] oDef = {
                "151",
                "0",
                "11",
                "PSP1", "parlament1",
                "Christian Democratic Union - Czechoslovak People's Party",
                "01.01.1900", "", "", "0", "", ""
        };
        Organ o = new Parliament(oDef);
        assertEquals("PSP1", o.getShortCode());
        assertEquals("parlament1", o.getFullTitle());
        assertEquals("151", o.getOrganId());
        assertEquals("http://linked.opendata.cz/resource/psp.cz/group/151", o.getRdfUri());
    }
}
