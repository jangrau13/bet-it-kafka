package ch.unisg.api2kafka.clients;

import ch.unisg.api2kafka.models.LiveScoresResponse;
import ch.unisg.api2kafka.models.MatchEventResponse;
import ch.unisg.api2kafka.models.MatchFixturesResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LiveScoreAPI {
    private final static String API_KEY = "1HoL0Y76QSeVPWgk";
    private final static String API_SECRET = "CORLYM9bCWy3zN3AvrmvquChmQdcNuxq";
    private final static String BASE_URI = "https://livescore-api.com/api-client/";
    private static String DATA_FILE_PATH;

    public LiveScoreAPI() {
        var currentDirectory = new File(".");
        String currentDirectoryPath;
        try {
            currentDirectoryPath = currentDirectory.getCanonicalPath();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        DATA_FILE_PATH = currentDirectoryPath + File.separator + "data" + File.separator;
    }

    public boolean verifyCredentials() {
        String path = "users/pair.json";
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(makeURI(path, null, null))
                .header("accept", "application/json")
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
        return (response.statusCode() == 200);
    }

    public LiveScoresResponse getLiveScores() {
        String path = "scores/live.json";
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(makeURI(path, null, null))
                .header("accept", "application/json")
                .build();

        HttpResponse<String> apiResponse;
        try {
            apiResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (apiResponse.statusCode() != 200) {
            System.out.println("Status code is:" + apiResponse.statusCode());
            return null;
        }

        LiveScoresResponse lsr;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            lsr = objectMapper.readValue(apiResponse.body(), LiveScoresResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        writeToFile(uriTofilename(path), apiResponse.body());
        return lsr;
    }

    public MatchFixturesResponse getMatchFixtures(Integer page) {
        String path = "fixtures/matches.json";
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(makeURI(path, page, null))
                .header("accept", "application/json")
                .build();

        HttpResponse<String> apiResponse;
        try {
            apiResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (apiResponse.statusCode() != 200) {
            return null;
        }

        MatchFixturesResponse mfr;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            mfr = objectMapper.readValue(apiResponse.body(), MatchFixturesResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // fix data
        try {
            JSONObject body = new JSONObject(apiResponse.body());
            JSONArray fixtures = body.getJSONObject("data").getJSONArray("fixtures");
            for (int i = 0; i < fixtures.length(); i++) {
                JSONObject match = fixtures.getJSONObject(i);
                JSONObject home = match.optJSONObject("home_translations");
                if (home != null) {
                    var keys = home.keys();
                    while (keys.hasNext()) {
                        String key = keys.next().toString();
                        String value = home.getString(key);
                        mfr.getData().getFixtures().get(i).getHomeTranslations().put(key, value);
                    }
                }
                JSONObject away = match.optJSONObject("away_translations");
                if (away != null) {
                    var keys = away.keys();
                    while (keys.hasNext()) {
                        String key = keys.next().toString();
                        String value = away.getString(key);
                        mfr.getData().getFixtures().get(i).getAwayTranslations().put(key, value);
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        if (mfr.getData().getNext_page().equals("false")) {
            mfr.getData().setNext_page(null);
        }
        if (mfr.getData().getPrev_page().equals("false")) {
            mfr.getData().setPrev_page(null);
        }

//        writeToFile(uriTofilename(path), apiResponse.body());
        return mfr;
    }

    public MatchEventResponse getMatchEvents(String id) {
        String path = "scores/events.json";
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(makeURI(path, null, id))
                .header("accept", "application/json")
                .build();

        HttpResponse<String> apiResponse;
        try {
            apiResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (apiResponse.statusCode() != 200) {
            return null;
        }

        MatchEventResponse mer;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            mer = objectMapper.readValue(apiResponse.body(), MatchEventResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return mer;
//        writeToFile(uriTofilename(path), apiResponse.body());
//        return new MatchEventResponse();
    }

    private static String uriTofilename(String path) {
        return path.replace("/", ".");
    }

    private static void writeToFile(String filename, String payload) {
        try (FileWriter fw = new FileWriter(DATA_FILE_PATH + filename)) {
            fw.write(payload);
            fw.flush();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static URI makeURI(String path, Integer page, String id) {
        try {
            URIBuilder url = null;
            url = new URIBuilder(BASE_URI + path);
            url.addParameter("key", API_KEY);
            url.addParameter("secret", API_SECRET);
            if (page != null) {
                url.addParameter("page", page.toString());
            }
            if (id != null) {
                url.addParameter("id", id);
            }
            return url.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
