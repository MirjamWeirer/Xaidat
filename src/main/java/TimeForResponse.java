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
    static final String date = "Datum";
    static final String countryID = "BundeslandID";
    static final String populaton ="Bevölkerung";
    static final String country = "Name";
    static final String vaccination ="GemeldeteImpfungenLaender";
    static final String vaccinationPer100 ="GemeldeteImpfungenLaenderPro100";
    /**
     * Make Response every 5 seconds from the URL and makes an event from the new CSV Records
     * @param timer
     * @param readFormURL
     * @param agent
     * @param uri
     */
    public void timeResponse(Timer timer, ReadFormURL readFormURL, CaduceusAgent agent, URI uri){
        timer.schedule(new MyTimerTask(readFormURL, agent, uri),
                new Date(),
                5000);


    }

    private static class MyTimerTask extends TimerTask {
        private final ReadFormURL readFormURL;
        private final CaduceusAgent agent;
        private final URI uri;
        int counter;
        int recordCounter;
        int newRecord;
        final Map<String, String> lastSeenDates;

        /**
         *Make Response every 5 seconds from the URL and makes an event from the new CSV Records
         * @param readFormURL reads from the URL and make request and response, is for the ReadFromURL class
         * @param agent for the events for Caduceus
         * @param uri the Url
         */
        public MyTimerTask(ReadFormURL readFormURL, CaduceusAgent agent, URI uri) {
            this.readFormURL = readFormURL;
            this.agent = agent;
            this.uri = uri;
            counter = 0;
            recordCounter = 0;
            newRecord = 0;
            lastSeenDates = new HashMap<>();

        }

        @Override
        public void run() {

            try {
                log.info("Request #{}", counter);
                HttpResponse<InputStream> response = readFormURL.sendHttpResponse();
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

                    String date = record.get(TimeForResponse.date);
                    String country = record.get(TimeForResponse.country);
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
                                    .of(TimeForResponse.date, record.get(TimeForResponse.date))
                                    .p(TimeForResponse.countryID,record.get(TimeForResponse.countryID))
                                    .p(TimeForResponse.populaton, record.get(TimeForResponse.populaton))
                                    .p(TimeForResponse.country, record.get(TimeForResponse.country))
                                    .p(TimeForResponse.vaccination, record.get(TimeForResponse.vaccination))
                                    .p(TimeForResponse.vaccinationPer100, record.get(TimeForResponse.vaccinationPer100))
                    );
                    newRecord++;
                    log.info("new Records: {}",newRecord);
                    recordCounter += 1;
                    lastSeenDates.put(country, date);
                    log.debug("Read record: {}", record); //for testing to on the console the records

                }

                log.info("Total Records: {}",recordCounter);
                counter += 1;
            } catch (InterruptedException e) {
                log.debug("Thread was interrupted.");
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
            }
        }
    }
}
