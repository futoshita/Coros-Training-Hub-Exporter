package com.futoshita.ics;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futoshita.coros.api.CorosTrainingSchedule;
import com.futoshita.coros.entity.CorosResponse;
import com.futoshita.ics.entity.IcsEvent;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class IcsCalendarTest {

    @Test
    public void export() throws IOException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CorosResponse cr = mapper.readValue(new File("src/test/resources/coros-training-schedule.json"), CorosResponse.class);

        List<IcsEvent> icsEvents = CorosTrainingSchedule.toIcsEvents(cr);

        Assert.assertEquals("s1e1", icsEvents.get(0).getSummary());
    }

    @Test
    public void export_WithoutPlan() throws IOException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CorosResponse cr = mapper.readValue(new File("src/test/resources/coros-training-schedule-without-plan.json"), CorosResponse.class);

        List<IcsEvent> icsEvents = CorosTrainingSchedule.toIcsEvents(cr);

        Assert.assertEquals("test séance hors plan", icsEvents.get(0).getSummary());
        Assert.assertEquals(20221113, icsEvents.get(0).getDtStart().intValue());
    }

}
