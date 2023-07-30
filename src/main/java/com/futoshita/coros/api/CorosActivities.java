package com.futoshita.coros.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futoshita.coros.AppParameters;
import com.futoshita.coros.entity.CorosResponse;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CorosActivities {

    private static final String GET_ACTIVITIES_URI = "https://teameuapi.coros.com/activity/query";
    private static final String DOWNLOAD_ACTIVITY_URI = "https://teameuapi.coros.com/activity/detail/download";

    public static CorosResponse getActivities(int pageNumber) throws URISyntaxException, IOException, InterruptedException {
        String parameters = "?size=30&pageNumber=" + pageNumber + "&startDay=" + AppParameters.getInstance().getStartDate() + "&endDay=" + AppParameters.getInstance().getEndDate() + "&modeList=";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(GET_ACTIVITIES_URI + parameters))
                .header("Accept", "application/json, text/plain, */*")
                .header("Content-Type", "application/json")
                .header("User-Agent", "Mozilla/5.0")
                .header("accessToken", AppParameters.getInstance().getAccessToken())
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (AppParameters.getInstance().isDebug()) {
            Files.createDirectories(Paths.get(AppParameters.getInstance().getOutputDirectory()));

            PrintWriter printWriter = new PrintWriter(new FileWriter(AppParameters.getInstance().getOutputDirectory() + "/activities-" + pageNumber + ".json"));
            printWriter.println(response.body());
            printWriter.close();
        }

        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CorosResponse cr = objectMapper.readValue(response.body(), CorosResponse.class);

        return cr;
    }

    public static void downloadActivity(Integer date, String labelId, Integer sportType) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(DOWNLOAD_ACTIVITY_URI + "?labelId=" + labelId + "&sportType=" + sportType + "&fileType=4"))
                .header("Accept", "application/json, text/plain, */*")
                .header("Content-Type", "application/json")
                .header("User-Agent", "Mozilla/5.0")
                .header("accessToken", AppParameters.getInstance().getAccessToken())
                .POST(HttpRequest.BodyPublishers.ofString("{\"account\":\"" + AppParameters.getInstance().getUsername() + "\",\"accountType\":2,\"pwd\":\"" + AppParameters.getInstance().getPassword() + "\"}"))
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (AppParameters.getInstance().isDebug()) {
            Files.createDirectories(Paths.get(AppParameters.getInstance().getOutputDirectory()));

            PrintWriter printWriter = new PrintWriter(new FileWriter(AppParameters.getInstance().getOutputDirectory() + "/activity.json"));
            printWriter.println(response.body());
            printWriter.close();
        }

        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CorosResponse cr = objectMapper.readValue(response.body(), CorosResponse.class);

        if (cr.getData() != null && cr.getData().getFileUrl() != null) {
            ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(cr.getData().getFileUrl()).openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(AppParameters.getInstance().getOutputDirectory() + "/" + date + "-" + labelId + ".fit");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
        }
    }
}
