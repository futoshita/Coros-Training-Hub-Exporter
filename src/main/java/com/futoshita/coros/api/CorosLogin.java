package com.futoshita.coros.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futoshita.coros.entity.CorosResponse;
import jakarta.xml.bind.DatatypeConverter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CorosLogin {

    private final String LOGIN_URL = "https://teamapi.coros.com/account/login";

    public String authenticate(String username, String hash) throws URISyntaxException, NoSuchAlgorithmException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(LOGIN_URL))
                .header("Accept", "application/json, text/plain, */*")
                .header("Content-Type", "application/json")
                .header("User-Agent", "Mozilla/5.0")
                .POST(HttpRequest.BodyPublishers.ofString("{\"account\":\"" + username + "\",\"accountType\":2,\"pwd\":\"" + hash + "\"}"))
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CorosResponse cr = objectMapper.readValue(response.body(), CorosResponse.class);

        return cr.getData() != null ? cr.getData().getAccessToken() : null;
    }

    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter.printHexBinary(digest).toLowerCase();
        return myHash;
    }
}
