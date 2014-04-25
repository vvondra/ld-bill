package cz.vojtechvondra.ldbill.vo;

import cz.vojtechvondra.ldbill.exceptions.UnknownLegislativeStageException;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class BillRevisionTest {

    @Test(expected = UnknownLegislativeStageException.class)
    public void testUnknownLegislativeStage() {
        new BillRevision(getBillMock(), 1, "description", "5.5.1993", "222", "X");
    }

    @Test
    public void testParse() {
        BillRevision br = new BillRevision(getBillMock(), 1, "brdesc", "1993-05-05 22:22:22", "0", "X");
        assertEquals("title - brdesc", br.getTitle());
        assertTrue(br.getStage() != null);
    }

    private Bill getBillMock() {
        return new Bill("123", "5.5.1992", "title", "description", "0");
    }
}
