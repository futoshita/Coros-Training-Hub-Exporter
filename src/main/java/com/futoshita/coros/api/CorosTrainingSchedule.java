package com.futoshita.coros.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futoshita.coros.entity.CorosEntity;
import com.futoshita.coros.entity.CorosProgram;
import com.futoshita.coros.entity.CorosResponse;
import com.futoshita.ics.entity.IcsEvent;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CorosTrainingSchedule {

    private final String GET_URL = "https://teamapi.coros.com/training/schedule/query";

    public CorosResponse get(String accessToken, String startDate, String endDate) throws IOException, URISyntaxException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(GET_URL + "?startDate=" + startDate + "&endDate=" + endDate))
                .header("Accept", "application/json, text/plain, */*")
                .header("Content-Type", "application/json")
                .header("User-Agent", "Mozilla/5.0")
                .header("accessToken", accessToken)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CorosResponse cr = objectMapper.readValue(response.body(), CorosResponse.class);

        return cr;
    }

    public List<IcsEvent> toIcsEvents(CorosResponse response) {
        Map<String, CorosProgram> programMap = new HashMap<>();
        if (response.getData() != null && response.getData().getPrograms() != null) {
            for (CorosProgram program : response.getData().getPrograms()) {
                programMap.put(program.getPlanId() + "-" + program.getIdInPlan(), program);
            }
        }

        List<IcsEvent> icsEvents = new ArrayList<>();

        if (response.getData() != null && response.getData().getEntities() != null) {
            for (CorosEntity entity : response.getData().getEntities()) {
                IcsEvent icsEvent = new IcsEvent();
                icsEvent.setDescription(programMap.get(entity.getPlanId() + "-" + entity.getIdInPlan()).getOverview().replace("\\", "\\\\"));
                icsEvent.setDtEnd(entity.getHappenDay());
                icsEvent.setDtStart(entity.getHappenDay());
                icsEvent.setSummary(programMap.get(entity.getPlanId() + "-" + entity.getIdInPlan()).getName());

                icsEvents.add(icsEvent);
            }
        }

        return icsEvents;
    }
}
