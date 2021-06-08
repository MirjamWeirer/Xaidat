import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class InputStreamCsv {
    private static Logger log = LoggerFactory.getLogger(InputStreamCsv.class);

    public static void main(String[] args) {
        Timer timer = new Timer("WebQueries");
        ReadFormURL read = new ReadFormURL();

        timer.scheduleAtFixedRate(new TimerTask() {
            int counter = 0;
            @Override
            public void run() {
                for (int i = 0; i < 6; i++){
                    System.out.println("Response: " + counter);
                    System.out.println(LocalDateTime.now());

                    Reader in = new InputStreamReader(read.http().body());
                    readResponse(in);
                    counter += 1;
                }
            }
        }, new Date(),
                5000);




    }

    public static List<String> readResponse(Reader in) {
//        Reader in = null;
        try {
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
//            synchronized (read) {
//                read.notify();
//                read.wait(5000);
//                read.notifyAll();
//            }
            //Thread.sleep(5000);
        } catch (IOException e){//| InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return List.of();
    }

}
