package com.futoshita.coros.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futoshita.coros.AppParameters;
import com.futoshita.coros.entity.CorosResponse;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CorosLogin {

    private static final String LOGIN_URL = "https://teamapi.coros.com/account/login";

    public static void login() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(LOGIN_URL))
                .header("Accept", "application/json, text/plain, */*")
                .header("Content-Type", "application/json")
                .header("User-Agent", "Mozilla/5.0")
                .POST(HttpRequest.BodyPublishers.ofString("{\"account\":\"" + AppParameters.getInstance().getUsername() + "\",\"accountType\":2,\"pwd\":\"" + AppParameters.getInstance().getPassword() + "\"}"))
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (AppParameters.getInstance().isDebug()) {
            Files.createDirectories(Paths.get(AppParameters.getInstance().getOutputDirectory()));

            PrintWriter printWriter = new PrintWriter(new FileWriter(AppParameters.getInstance().getOutputDirectory() + "/login.json"));
            printWriter.println(response.body());
            printWriter.close();
        }

        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CorosResponse cr = objectMapper.readValue(response.body(), CorosResponse.class);

        AppParameters.getInstance().setAccessToken(cr.getData() != null ? cr.getData().getAccessToken() : null);
    }

}
