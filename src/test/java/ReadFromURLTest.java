import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

public class ReadFromURLTest {
    @Test
    public void httpRequest() throws URISyntaxException {

        URI uri = new URI("https://info.gesundheitsministerium.gv.at/data/timeline-bundeslaendermeldungen.csv");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpRequest result =HttpRequest.newBuilder()
                .uri( new URI("https://info.gesundheitsministerium.gv.at/data/timeline-bundeslaendermeldungen.csv"))
                .GET()
                .build();
        Assert.assertEquals(request, result);
    }

    @Test
    public void httpResponse() throws IOException {
        InputStream result = ReadFormURL.http().body();
        Assert.assertEquals(ReadFormURL.http().body().read(),result.read());
    }

}
