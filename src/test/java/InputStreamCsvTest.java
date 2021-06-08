
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class InputStreamCsvTest {

    @Test
    public void readResponseReturnsEmptyListForEmptyReader() {
        List<String> result = InputStreamCsv.readResponse(new StringReader(""));

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void readResponseReturnsOneLineForReaderWithOneLineCSV() {
        String csv = "Datum;BundeslandID;Bevölkerung;Name;GemeldeteImpfungenLaender;GemeldeteImpfungenLaenderPro100\n" +
                "2021-01-10T23:59:59+01:00;1;294436;Burgenland;;";
        List<String> result = InputStreamCsv.readResponse(new StringReader(csv));

        Assert.assertEquals(1, result.size());
    }

    @Test
    public void smokeTest() {
        InputStreamCsv.readResponse(new StringReader(""));
    }

    @Test
    public void anotherSmokeTest() {
        InputStreamCsv.readResponse(new StringReader(""));
    }

}
