import com.xaidat.zam.vaccmonitor.InputStreamCsv;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.List;

public class InputStreamCsvTest {

    @Test
    public void readResponseReturnsEmptyListForEmptyReader() {
        InputStreamCsv inputStreamCsv = new InputStreamCsv();
        List<CSVRecord> result =  inputStreamCsv.readResponse (new StringReader(""));

        Assert.assertEquals(0, result.size());
    }
    @Test
    public void readResponseReturnsTwoLinesForReaderWithTwoLineCSV() {
        InputStreamCsv inputStreamCsv = new InputStreamCsv();
        String csv = "Datum;BundeslandID;Bevölkerung;Name;GemeldeteImpfungenLaender;GemeldeteImpfungenLaenderPro100\n" +
                "2021-01-10T23:59:59+01:00;1;294436;Burgenland;;\n" +
                "2021-01-10T23:59:59+01:00;2;561293;Kärnten;;";
        List<CSVRecord> result = inputStreamCsv.readResponse(new StringReader(csv));

        Assert.assertEquals(2,result.size());
    }
    @Test
    public void readResponseReturnsOneLineForReaderWithOneLineCSV() {
        InputStreamCsv inputStreamCsv = new InputStreamCsv();
        String csv = "Datum;BundeslandID;Bevölkerung;Name;GemeldeteImpfungenLaender;GemeldeteImpfungenLaenderPro100\n" +
                "2021-01-10T23:59:59+01:00;1;294436;Burgenland;;";
        List<CSVRecord> result = inputStreamCsv.readResponse(new StringReader(csv));

        Assert.assertEquals(1, result.size());
    }

    @Test
    public void smokeTest() {
        InputStreamCsv inputStreamCsv = new InputStreamCsv();
        inputStreamCsv.readResponse(new StringReader(""));
    }

    @Test
    public void anotherSmokeTest() {
        InputStreamCsv inputStreamCsv = new InputStreamCsv();
        inputStreamCsv.readResponse(new StringReader(""));
    }

}
