package com.futoshita.coros;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CorosTest {

    private static Properties properties;

    @BeforeClass
    public static void beforeClass() {
        try (InputStream input = new FileInputStream("src/test/resources/authentication.properties")) {
            properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void main_Ok() {
        Coros.main(new String[] {"--username", properties.getProperty("username"), "--password", properties.getProperty("password"), "--startDate", "20221101", "--endDate", "20221130", "--output", "target/coros-calendar.ics"});
    }

    @Test
    public void main_MissingRequiresOptions() {
        Coros.main(new String[] {"--username", properties.getProperty("username")});
    }

    @Test
    public void main_UnrecognizedOption() {
        Coros.main(new String[] {"--help"});
    }
}
