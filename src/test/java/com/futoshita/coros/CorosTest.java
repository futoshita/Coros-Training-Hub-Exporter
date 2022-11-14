package com.futoshita.coros;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CorosTest {

    private static Properties properties;

    @Before
    public void before() {
        try (InputStream input = new FileInputStream("src/test/resources/authentication.properties")) {
            properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void after() {
        AppParameters.getInstance().reset();
    }

    @Test
    public void main_Activities() {
        Coros.main(new String[] {"--username", properties.getProperty("username"), "--password", properties.getProperty("password"), "--startDate", "20221101", "--endDate", "20221130", "--type", "ACTIVITIES", "--output", "target/coros-activities", "--DEBUG"});
    }

    @Test
    public void main_TrainingSchedule() {
        Coros.main(new String[] {"--username", properties.getProperty("username"), "--password", properties.getProperty("password"), "--startDate", "20221101", "--endDate", "20221130", "--type", "TRAINING_CALENDAR", "--output", "target", "--DEBUG"});
    }

    @Test
    public void main_MissingRequiresOptions() {
        Coros.main(new String[] {"--username", properties.getProperty("username")});
    }

    @Test
    public void main_UnrecognizedOption() {
        Coros.main(new String[] {"--help", "--DEBUG"});
    }
}
