package com.futoshita.coros;

import com.futoshita.coros.api.CorosLogin;
import com.futoshita.coros.api.CorosTrainingSchedule;
import com.futoshita.coros.entity.CorosResponse;
import com.futoshita.ics.IcsCalendar;
import com.futoshita.ics.entity.IcsEvent;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Coros {

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder("u").longOpt("username").hasArg(true).required().desc("Coros username (required)").build())
                .addOption(Option.builder("p").longOpt("password").hasArg(true).required().desc("Coros password (required)").build())
                .addOption(Option.builder("o").longOpt("output").hasArg(true).required().desc("output file path (required)").build())
                .addOption(Option.builder("s").longOpt("startDate").hasArg(true).required().desc("startDate with format YYYYMMDD (required)").build())
                .addOption(Option.builder("e").longOpt("endDate").hasArg(true).required().desc("endDate with format YYYYMMDD (required)").build());

        String username = null;
        String password = null;
        String outputPath = null;
        String startDate = null;
        String endDate = null;

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            username = cmd.getOptionValue("u");
            password = cmd.getOptionValue("p");
            outputPath = cmd.getOptionValue("o");
            startDate = cmd.getOptionValue("s");
            endDate = cmd.getOptionValue("e");
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Coros", options);

            e.printStackTrace();
        }

        if (username != null && password != null && outputPath != null && startDate != null && endDate != null) {
            CorosLogin cl = new CorosLogin();
            String accessToken = null;
            try {
                accessToken = cl.authenticate(username, cl.hashPassword(password));
            } catch (URISyntaxException | NoSuchAlgorithmException | IOException | InterruptedException e) {
                e.printStackTrace();
            }

            CorosTrainingSchedule cts = new CorosTrainingSchedule();
            CorosResponse corosResponse = null;
            try {
                corosResponse = cts.get(accessToken, startDate, endDate);
            } catch (IOException | URISyntaxException | InterruptedException e) {
                e.printStackTrace();
            }

            List<IcsEvent> icsEvents = cts.toIcsEvents(corosResponse);

            IcsCalendar icsCalendar = new IcsCalendar();
            try {
                icsCalendar.export(outputPath, icsEvents);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
