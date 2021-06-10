import com.xaidat.caduceus.CaduceusAgent;
import com.xaidat.caduceus.Properties;
import com.xaidat.caduceus.Tags;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.*;

public class TimeForResponse {
    private static final Logger log = LoggerFactory.getLogger(TimeForResponse.class);

    /**
     * Make Response every 5 secounds from the URL and makes an event from the new CSV Records
     * @param timer time for response
     * @param readFormURL reads from the URL and make request and response, is for the ReadFromURL class
     * @param agent for the events for Caduceus
     * @param uri the Url
     */
    public static void timeResponse(Timer timer, ReadFormURL readFormURL, CaduceusAgent agent, URI uri){
        timer.scheduleAtFixedRate(new TimerTask() {
            int counter = 0;
            int recordCounter = 0;
            Map<String, String> lastSeenDates = new HashMap<>();

            @Override
            public void run() {
                try {
                    log.info("Request #{}", counter);
                    HttpResponse<InputStream> response = null;
                    try {
                        response = readFormURL.http();
                    } catch (IOException e) {
                        log.info("Error while accessing csv data", e);
                        agent.notify(
                                "ERROR",
                                "Cold not find URI",
                                "",
                                Tags.of("URI not found"),
                                Properties
                                        .of("httpstatus", "is not 200")
                                        .p("message", "URI not found")
                                        .p("url", uri)
                        );
                        return;
                    }
                    if (response.statusCode() != 200) {
                        agent.notify(
                                "ERROR",
                                "HTTP Response was not successful",
                                "",
                                Tags.of("Response has another statuscode then 200"),
                                Properties
                                        .of("http statsucode", response.statusCode())
                                        .p("message", response)
                                        .p("url", uri)
                        );
                        return;
                    }
                    Reader in = new InputStreamReader(response.body());
                    List<CSVRecord> records = InputStreamCsv.readResponse(in);
                    for (CSVRecord record : records) {
                        String date = record.get("Datum");
                        String country = record.get("Name");
                        String lastDateSeen = lastSeenDates.getOrDefault(country, "2021-01-14T23:59:59+01:00");
                        if (date.compareTo(lastDateSeen) <1) {
                            log.debug("date {} already seen", date);
                            continue;
                        }
                        agent.notify(
                                "Data",
                                "Datas from CSV file",
                                "",
                                Tags.of("parse CSV file"),
                                Properties
                                        .of("Datum", record.get("Datum"))
                                        .p("BundeslandID",record.get("BundeslandID"))
                                        .p("Bevölkerung", record.get("Bevölkerung"))
                                        .p("Name", record.get("Name"))
                                        .p("GemeldeteImpfungenLaender", record.get("GemeldeteImpfungenLaender"))
                                        .p("GemeldeteImpfungenLaenderPro100", record.get("GemeldeteImpfungenLaenderPro100"))
                        );
                        recordCounter += 1;
                        lastSeenDates.put(country, date);
                        //log.info("Read record: {}", record); for testing to on the console the records
                    }
                    log.info("new records: {}",recordCounter);
                    counter += 1;
                } catch (InterruptedException e) {
                    log.debug("Thread was interrupted.");
                }
            }
        }, new Date(),
                5000);
    }
}
