import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class App {
    public static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        Timer timer = new Timer("WebQueries");
        ReadFormURL read = new ReadFormURL();

        timer.scheduleAtFixedRate(new TimerTask() {
            int counter = 0;

            @Override
            public void run() {
                System.out.println("Response: " + counter);
                System.out.println(LocalDateTime.now());

                Reader in = new InputStreamReader(read.http().body());
                InputStreamCsv.readResponse(in);
                counter += 1;
            }
        }, new Date(),
               5000);

    }
}
