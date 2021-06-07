import java.io.FileReader;
import java.io.Reader;

public class ParsingExcelToCSV {

    public static void main(String[] args) {
        Reader in = new FileReader("C:\\Users\\mirja\\Google Drive\\FH Campus02\\Xaidat\\timeline-bundeslaendermeldungen.csv");
        Iterable<>records = CSVFormat.EXECEL.parse(in);
    }
}
