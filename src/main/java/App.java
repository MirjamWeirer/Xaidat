import com.xaidat.caduceus.Caduceus;
import com.xaidat.caduceus.CaduceusAgent;
import com.xaidat.caduceus.Properties;
import com.xaidat.caduceus.Tags;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class App {
    public static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws URISyntaxException {
        URI uri = new URI("https://info.gesundheitsministerium.gv.at/data/timeline-bundeslaendermeldungen.csv");
        //URI uri = new URI("https://test.at"); for test agent.notify() error
        Timer timer = new Timer("WebQueries");
        ReadFormURL readFormURL = new ReadFormURL(uri);

        CaduceusAgent agent = Caduceus.optionalAgent();
        agent.addGlobalTags(Tags.of("TESTING"));
// Just as a reminder for the notify() parameters
//        agent.notify(
//                "CATEGORY",
//                "SUBJECT",
//                "BODY",
//                Tags.empty(),
//                Properties.empty()
//        );



        timer.scheduleAtFixedRate(new TimerTask() {
            int counter = 0;

            @Override
            public void run() {
                try {
                    log.info("Request #{}", counter);
                    HttpResponse<InputStream> response = readFormURL.http();
                    if (response == null) {
                        agent.notify(
                                "ERROR",
                                "Cold not find URI",
                                "",
                                Tags.of("URI not found"),
                                Properties
                                        .of("httpstatus","is not 200")
                                        .p("message","URI not found")
                                        .p("url",uri)
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
                                        .p("url",uri)
                        );
                        return;
                    }
                    Reader in = new InputStreamReader(response.body());
                    List<CSVRecord> records = InputStreamCsv.readResponse(in);
                    for (CSVRecord record : records) {
                        agent.notify(
                                "Data",
                                "Datas from CSV file",
                                "",
                                Tags.of("parse CSV file"),
                                Properties
                                        .of("Datum", record.get(0))
                                        .p("Bevölkerung", record.get(1))
                                        .p("Name", record.get(2))
                                        .p("GemeldeteImpfungenLaender", record.get(3))
                                        .p("GemeldeteImpfungenLaenderPro100", record.get(4))
                        );
                    }
                    counter += 1;
                } catch (InterruptedException e) {
                    log.debug("Thread was interrupted.");
                }
            }
        }, new Date(),
               5000);

    }
}
