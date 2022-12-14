package com.futoshita.ics;

import com.futoshita.coros.AppParameters;
import com.futoshita.ics.entity.IcsEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.util.List;

public class IcsCalendar {

    public static void export(List<IcsEvent> icsEvents) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(AppParameters.getInstance().getOutputDirectory() + "/coros-training-calendar.ics"));

        printWriter.println("BEGIN:VCALENDAR");
        printWriter.println("VERSION:2.0");
        printWriter.println("PRODID:futoshita exporter");
        printWriter.println("METHOD:PUBLISH");
        printWriter.println("X-WR-CALNAME:Coros Training Calendar");
        printWriter.println("X-WR-TIMEZONE:" + ZoneId.systemDefault().toString());
        printWriter.println("CALSCALE:GREGORIAN");

        for (IcsEvent icsEvent : icsEvents) {
            printWriter.println("BEGIN:VEVENT");
            printWriter.println("DTEND;VALUE=DATE:" + icsEvent.getDtEnd());
            printWriter.println("SUMMARY:" + icsEvent.getSummary());
            printWriter.println("DTSTART;VALUE=DATE:" + icsEvent.getDtStart());
            printWriter.println("DESCRIPTION:" + (icsEvent.getDescription() != null ? icsEvent.getDescription().replaceAll("\n", "\\\\n").replaceAll("\"", "\\\\\"") : null));
            printWriter.println("END:VEVENT");
        }

        printWriter.println("END:VCALENDAR");

        printWriter.close();
    }
}
