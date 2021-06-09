import com.xaidat.caduceus.Caduceus;
import com.xaidat.caduceus.CaduceusAgent;
import com.xaidat.caduceus.Properties;
import com.xaidat.caduceus.Tags;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class InputStreamCsv {
    private static Logger log = LoggerFactory.getLogger(InputStreamCsv.class);

    public static List<CSVRecord> readResponse(Reader in) {
        List<CSVRecord> list = new ArrayList<>();
        CaduceusAgent agent = Caduceus.optionalAgent();
        try {
            Iterable<CSVRecord>records = CSVFormat.EXCEL
                    .withDelimiter(';')
                    .withSkipHeaderRecord()
                    .withHeader("Datum", "BundeslandID", "Bevölkerung", "Name", "GemeldeteImpfungenLaender", "GemeldeteImpfungenLaenderPro100")
                    .parse(in);

            for (CSVRecord record : records) {
                list.add(record);
                agent.notify(
                        "Data",
                        "Datas from CSV file",
                        "",
                        Tags.of("parse CSV file"),
                        Properties
                                .of("Datum",record.get(0))
                                .p("Bevölkerung",record.get(1))
                                .p("Name", record.get(2))
                                .p("GemeldeteImpfungenLaender",record.get(3))
                                .p("GemeldeteImpfungenLaenderPro100",record.get(4))
                );
                System.out.println(record);
            }
            log.info("Read {} records from request", list.size());
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
