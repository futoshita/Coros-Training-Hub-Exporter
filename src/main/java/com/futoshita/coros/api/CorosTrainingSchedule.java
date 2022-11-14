package com.futoshita.coros.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futoshita.coros.AppParameters;
import com.futoshita.coros.entity.CorosEntity;
import com.futoshita.coros.entity.CorosProgram;
import com.futoshita.coros.entity.CorosResponse;
import com.futoshita.ics.entity.IcsEvent;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CorosTrainingSchedule {

    private static final String GET_TRAINING_SCHEDULE_URI = "https://teamapi.coros.com/training/schedule/query";

    public static CorosResponse getTrainingSchedule() throws IOException, URISyntaxException, InterruptedException {
        String parameters = "?startDate=" + AppParameters.getInstance().getStartDate() + "&endDate=" + AppParameters.getInstance().getEndDate();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(GET_TRAINING_SCHEDULE_URI + parameters))
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

            PrintWriter printWriter = new PrintWriter(new FileWriter(AppParameters.getInstance().getOutputDirectory() + "/training-schedule.json"));
            printWriter.println(response.body());
            printWriter.close();
        }

        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CorosResponse cr = objectMapper.readValue(response.body(), CorosResponse.class);

        return cr;
    }

    public static List<IcsEvent> toIcsEvents(CorosResponse response) {
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
