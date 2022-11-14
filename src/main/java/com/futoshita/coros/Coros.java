package com.futoshita.coros;

import com.futoshita.coros.api.CorosActivities;
import com.futoshita.coros.api.CorosLogin;
import com.futoshita.coros.api.CorosTrainingSchedule;
import com.futoshita.coros.entity.CorosActivity;
import com.futoshita.coros.entity.CorosResponse;
import com.futoshita.ics.IcsCalendar;
import com.futoshita.ics.entity.IcsEvent;
import com.futoshita.util.HashUtil;
import com.futoshita.util.ProgressBar;
import org.apache.commons.cli.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Coros {

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder("u").longOpt("username").hasArg(true).required().desc("Coros username (required)").build())
                .addOption(Option.builder("p").longOpt("password").hasArg(true).required().desc("Coros password (required)").build())
                .addOption(Option.builder("s").longOpt("startDate").hasArg(true).required().desc("startDate with format YYYYMMDD (required)").build())
                .addOption(Option.builder("e").longOpt("endDate").hasArg(true).required().desc("endDate with format YYYYMMDD (required)").build())
                .addOption(Option.builder("t").longOpt("type").hasArg(true).required().desc("type of export (TRAINING_CALENDAR, ACTIVITIES) (required)").build())
                .addOption(Option.builder("o").longOpt("output").hasArg(true).required().desc("output file(s) directory (required)").build())
                .addOption(Option.builder("d").longOpt("DEBUG").desc("debug mode").build());

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            AppParameters.getInstance().setUsername(cmd.getOptionValue("u"));
            AppParameters.getInstance().setPassword(HashUtil.hashPassword(cmd.getOptionValue("p")));
            AppParameters.getInstance().setStartDate(Integer.valueOf(cmd.getOptionValue("s")));
            AppParameters.getInstance().setEndDate(Integer.valueOf(cmd.getOptionValue("e")));
            AppParameters.getInstance().setExportType(cmd.getOptionValue("t"));
            AppParameters.getInstance().setOutputDirectory(cmd.getOptionValue("o"));
            AppParameters.getInstance().setDebug(cmd.hasOption("d") ? true : false);

            CorosLogin.login();

            if (AppParameters.getInstance().getExportType().equals("TRAINING_CALENDAR")) {
                CorosResponse corosResponse = CorosTrainingSchedule.getTrainingSchedule();
                List<IcsEvent> icsEvents = CorosTrainingSchedule.toIcsEvents(corosResponse);
                IcsCalendar.export(icsEvents);
            } else if (AppParameters.getInstance().getExportType().equals("ACTIVITIES")) {
                List<CorosActivity> activities = new ArrayList<>();

                System.out.println("Getting activities list...");

                CorosResponse corosResponse = CorosActivities.getActivities(1);
                for (CorosActivity activity : corosResponse.getData().getDataList()) {
                    activities.add(activity);
                }

                for (int i = 2; i <= corosResponse.getData().getTotalPage(); i++) {
                    corosResponse = CorosActivities.getActivities(i);
                    for (CorosActivity activity : corosResponse.getData().getDataList()) {
                        activities.add(activity);
                    }
                }

                System.out.println("Downloading " + activities.size() + " .fit files");

                ProgressBar bar = new ProgressBar();
                bar.update(0, activities.size());

                Files.createDirectories(Paths.get(AppParameters.getInstance().getOutputDirectory()));
                for (int i = 0; i < activities.size(); i++) {
                    CorosActivities.downloadActivity(activities.get(i).getDate(), activities.get(i).getLabelId(), activities.get(i).getSportType());
                    bar.update(i, activities.size());
                }

                System.out.println("Download Completed.");
            }
        } catch (ParseException e) {
            System.out.println("[ERROR] parameters parsing failed. " + e.getMessage());
            System.out.println();

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar coros.jar", options);

            if (AppParameters.getInstance().isDebug()) {
                System.out.println();
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("[ERROR] password hash failed. " + e.getMessage());

            if (AppParameters.getInstance().isDebug()) {
                System.out.println();
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("[ERROR] data export failed. " + e.getMessage());

            if (AppParameters.getInstance().isDebug()) {
                System.out.println();
                e.printStackTrace();
            }
        }
    }
}
