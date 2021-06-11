package com.xaidat.zam.vaccmonitor;

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
        URI uri;
        if (args.length == 0) {
            log.info("User did not specify URL, use default");
            uri = new URI("https://info.gesundheitsministerium.gv.at/data/timeline-bundeslaendermeldungen.csv");
        } else {
            uri = new URI(args[0]);
        }
        //URI uri = new URI("https://test.at"); for test agent.notify() error
        Timer timer = new Timer("WebQueries");
        ReadFormURL readFormURL = new ReadFormURL(uri);
        CaduceusAgent agent = Caduceus.optionalAgent();
        agent.addGlobalTags(Tags.of("TESTING"));
        TimeForResponse timeForResponse = new TimeForResponse();
// Just as a reminder for the notify() parameters
//        agent.notify(
//                "CATEGORY",
//                "SUBJECT",
//                "BODY",
//                Tags.empty(),
//                Properties.empty()
//        );
       timeForResponse.timeResponse(timer,readFormURL,agent,uri);
    }
}
