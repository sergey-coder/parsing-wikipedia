
package ru.digitalhabbits.homework1.service;

import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.slf4j.LoggerFactory.getLogger;

public class WikipediaClient {
    private static final Logger logger = getLogger(WikipediaClient.class);

    private static final String WIKIPEDIA_SEARCH_URL = "https://en.wikipedia.org/w/api.php";
    private static final String CONTENT_FORMAT = "wikitext";
    private static final String FORMAT_TYPE = "json";
    private static final String ACTION = "parse";

    @Nonnull
    public String search(@Nonnull String searchString) {
        return search(searchString, null);
    }

    @Nonnull
    public String search(@Nonnull String searchString, @Nullable String revision) {
        final URI uri = prepareSearchUrl(searchString, revision);
        System.out.println(uri);
        // TODO: NotImplemented
        String result = "";
        String jsonString;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpGet request = new HttpGet(uri);
            CloseableHttpResponse response = httpClient.execute(request);

            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {

                    jsonString = EntityUtils.toString(entity);

                    final String text = JsonParser.parseString(jsonString)
                            .getAsJsonObject().get("parse")
                            .getAsJsonObject().get("wikitext")
                            .getAsString();
                    result = MarkdownParser.parseToText(text);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    return result;
    }

    @Nonnull
    private URI prepareSearchUrl(@Nonnull String searchString, @Nullable String revision) {
        try {
            final URIBuilder builder = new URIBuilder(WIKIPEDIA_SEARCH_URL)
                    .addParameter("action", ACTION)
                    .addParameter("prop", CONTENT_FORMAT)
                    .addParameter("format", FORMAT_TYPE)
                    .addParameter("formatversion", "2");

            if (revision != null) {
                builder.addParameter("oldid", revision);
            } else {
                builder.addParameter("page", searchString);
            }

            return builder.build();
        } catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }
}
