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
        try {
            Iterable<CSVRecord>records = CSVFormat.EXCEL
                    .withDelimiter(';')
                    .withSkipHeaderRecord()
                    .withHeader("Datum", "BundeslandID", "Bevölkerung", "Name", "GemeldeteImpfungenLaender", "GemeldeteImpfungenLaenderPro100")
                    .parse(in);

            for (CSVRecord record : records) {
                list.add(record);
                System.out.println(record);
            }
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
