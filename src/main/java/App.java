import com.xaidat.caduceus.Caduceus;
import com.xaidat.caduceus.CaduceusAgent;
import com.xaidat.caduceus.Tags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;

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
        TimeForResponse.timeResponse(timer,readFormURL,agent,uri);
    }
}
