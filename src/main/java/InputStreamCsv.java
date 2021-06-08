import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;

public class InputStreamCsv {
    private static Logger log = LoggerFactory.getLogger(InputStreamCsv.class);

    public static void main(String[] args) {
        for (int i = 0; i < 6; i++){
            System.out.println("Response: " + i);
            System.out.println(LocalDateTime.now());
            readResponse();

        }

    }

    public static void readResponse() {
        Reader in = null;
        try {
            ReadFormURL read = new ReadFormURL();

            in = new InputStreamReader(read.http().body());


            Iterable<CSVRecord> records = null;

            records = CSVFormat.EXCEL
                    .withDelimiter(';')
                    .withHeader("Datum", "BundeslandID", "Bevölkerung", "Name", "GemeldeteImpfungenLaender", "GemeldeteImpfungenLaenderPro100")
                    .parse(in);

            for (CSVRecord record : records) {
                String date = record.get("Datum");
                String stateID = record.get("BundeslandID");
                String population = record.get("Bevölkerung");
                String name = record.get("Name");
                String reportVaccinationsPerState = record.get("GemeldeteImpfungenLaender");
                String reportVaccinationsPerStatePer100 = record.get("GemeldeteImpfungenLaenderPro100");
                System.out.println(date + " " + stateID + " " + population + " " + name + " " + reportVaccinationsPerState + " " + reportVaccinationsPerStatePer100);
            }
            synchronized (read) {
                read.notify();
                read.wait(5000);
                read.notifyAll();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
