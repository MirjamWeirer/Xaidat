import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class InputStreamCsv {
    private static final Logger log = LoggerFactory.getLogger(InputStreamCsv.class);

    /**
     * Reads data from Reader in and converts it to a list of CSV records.
     * This method closes "in" before returning.
     *
     * @param in data source to turn into CSV records
     * @return all the CSV records
     */
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
            }

            log.info("Read {} records from request", list.size());
        } catch (IOException e){
            log.warn("Error while...", e);
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
