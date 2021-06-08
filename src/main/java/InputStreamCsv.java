import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class InputStreamCsv{
    public static void main(String[] args) {
        ReadFormURL read = new ReadFormURL();
        read.http();
        Reader in = new InputStreamReader(read.http().body());


        Iterable<CSVRecord> records = null;
        try {
            records = CSVFormat.EXCEL
                    .withDelimiter(';')
                    .withHeader("Datum","BundeslandID","Bevölkerung","Name","GemeldeteImpfungenLaender","GemeldeteImpfungenLaenderPro100")
                    .parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (CSVRecord record : records) {
            String date = record.get("Datum");
            String stateID = record.get("BundeslandID");
            String population = record.get("Bevölkerung");
            String name = record.get("Name");
            String reportVaccinationsPerState = record.get("GemeldeteImpfungenLaender");
            String reportVaccinationsPerStatePer100 = record.get("GemeldeteImpfungenLaenderPro100");
            System.out.println(date + " " + stateID + " " + population + " " + name + " " + reportVaccinationsPerState + " " + reportVaccinationsPerStatePer100);
        }
    }



}
