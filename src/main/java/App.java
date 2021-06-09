import com.xaidat.caduceus.Caduceus;
import com.xaidat.caduceus.CaduceusAgent;
import com.xaidat.caduceus.Properties;
import com.xaidat.caduceus.Tags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class App {
    public static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws URISyntaxException {
        URI uri = new URI("https://info.gesundheitsministerium.gv.at/data/timeline-bundeslaendermeldungen.csv");

        Timer timer = new Timer("WebQueries");
        ReadFormURL readFormURL = new ReadFormURL(uri);

        CaduceusAgent agent = Caduceus.optionalAgent();

        agent.notify(
                "CATEGORY",
                "SUBJECT",
                "BODY",
                Tags.empty(),
                Properties.empty()
        );

        agent.notify(
                "ERROR",
                "Cold not find URI",
                "",
                Tags.of("URI not found"),
                Properties
                        .of("httpstatus",400)
                        .p("message","URI not found")
                        .p("url",URI.create("https://info.gesundheitsministerium.gv.at/data/timeline-bundeslaendermeldungen.csv"))
        );

        timer.scheduleAtFixedRate(new TimerTask() {
            int counter = 0;

            @Override
            public void run() {
                System.out.println("Response: " + counter);
                System.out.println(LocalDateTime.now());
                InputStreamCsv.readResponse(in);
                counter += 1;

            }
        }, new Date(),
               5000);

    }
}
