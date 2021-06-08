import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ParsingExcelToCSV {

    public static void main(String[] args) {

        File getCSVFile = new File("C:\\Users\\mirja\\Xaidat\\timeline-bundeslaendermeldungen.csv");

//        try {
//            Reader in = new FileReader(getCSVFile);
//            Iterable<CSVRecord> records = CSVFormat.EXCEL.withDelimiter(';').withHeader("Datum","BundeslandID","Bevölkerung","Name","GemeldeteImpfungenLaender","GemeldeteImpfungenLaenderPro100").parse(in);
//            for (CSVRecord record : records) {
//                String date = record.get("Datum");
//                String stateID = record.get("BundeslandID");
//                String population = record.get("Bevölkerung");
//                String name = record.get("Name");
//                String reportVaccinationsPerState = record.get("GemeldeteImpfungenLaender");
//                String reportVaccinationsPerStatePer100 = record.get("GemeldeteImpfungenLaenderPro100");
//                System.out.println(date + " "+stateID + " "+population+" "+name+" "+reportVaccinationsPerState+" "+reportVaccinationsPerStatePer100);
//
//            }
//            in.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {

            URI uri = new URI("https://info.gesundheitsministerium.gv.at/data/timeline-bundeslaendermeldungen.csv");
            HttpRequest request = HttpRequest.newBuilder()
                   .uri(uri)
                   .GET()
                   .build();

           log.trace("overwhelmingly detailed");
           log.debug("Sent request to {} and received {}", uri, request);
           log.info("Send request to server for csv");
           log.warn("Could not connect to server");
           log.error("Something is very wrong");


            System.out.println(request);


            HttpResponse<InputStream>response = HttpClient
                    .newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(request,HttpResponse.BodyHandlers.ofInputStream());
            //FileUtils.copyURLToFile(,new File("timeline.csv"));

            System.out.println(response);
            HttpHeaders responseHeaders = response.headers();
            System.out.println(responseHeaders);
//            HttpResponse<String> file =  response;

            Reader in = new InputStreamReader(response.body());
//            Reader in = new FileReader(String.valueOf(file));
            Iterable<CSVRecord> records = CSVFormat.EXCEL
                    .withDelimiter(';')
                    .withHeader("Datum","BundeslandID","Bevölkerung","Name","GemeldeteImpfungenLaender","GemeldeteImpfungenLaenderPro100")
                    .parse(in);
            for (CSVRecord record : records) {
                String date = record.get("Datum");
                String stateID = record.get("BundeslandID");
                String population = record.get("Bevölkerung");
                String name = record.get("Name");
                String reportVaccinationsPerState = record.get("GemeldeteImpfungenLaender");
                String reportVaccinationsPerStatePer100 = record.get("GemeldeteImpfungenLaenderPro100");
                System.out.println(date + " " + stateID + " " + population + " " + name + " " + reportVaccinationsPerState + " " + reportVaccinationsPerStatePer100);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
