import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ReadFormURL {

    private static final Logger log = LoggerFactory.getLogger(ReadFormURL.class);

    private final URI uri;
    private final HttpClient client;

    public ReadFormURL(URI uri) {
        this.uri = uri;
        this.client = HttpClient
                .newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .proxy(ProxySelector.getDefault())
                .build();
    }

    /**
     * sends a http request and safe it as a http response
     * @return the response from the http client
     * @throws InterruptedException thread was interrupted
     * @throws IOException when a error by accessing csv data
     */

    public HttpResponse<InputStream> sendHttpResponse() throws InterruptedException, IOException {
        HttpResponse<InputStream> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        } catch (IOException e) {
            log.info("Error while accessing csv data", e);
            throw e;
        }
        return response;
    }
}


