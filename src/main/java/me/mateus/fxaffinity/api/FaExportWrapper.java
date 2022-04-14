package me.mateus.fxaffinity.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.mateus.fxaffinity.entities.SubmissionEntity;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class FaExportWrapper {

    private final URIBuilder URI_BUILDER = new URIBuilder()
            .setScheme("https")
            .setHost("faexport.spangle.org.uk");

    public SubmissionEntity getSubmission(String submissionId) {
        SubmissionEntity submission;
        JsonObject json = getSubmissionJsonInfo(submissionId);

        if (json == null)
            return null;

        String author = json.get("profile_name").getAsString();
        String name = json.get("title").getAsString();
        String postDate = json.get("posted_at").getAsString();
        String fullUrl = json.get("full").getAsString();
        String originalLink = json.get("link").getAsString();
        String rating = json.get("rating").getAsString();

        submission = new SubmissionEntity(author, name, postDate, fullUrl, originalLink, rating);
        return submission;
    }

    private JsonObject getSubmissionJsonInfo(String submissionId)  {
        JsonObject jsonObject = null;
        try {
            URI uri = URI_BUILDER.setPath(String.format("/submission/%s.json", submissionId))
                    .build();
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet httpGet = new HttpGet(uri);
                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    HttpEntity httpEntity = response.getEntity();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
                    jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
