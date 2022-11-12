package com.futoshita.coros;

import com.futoshita.coros.api.CorosLogin;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class CorosLoginTest {

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
    public void authenticate() {
        CorosLogin cl = new CorosLogin();
        try {
            String accessToken = cl.authenticate(properties.getProperty("username"), cl.hashPassword(properties.getProperty("password")));
            Assert.assertNotNull(accessToken);
        } catch (URISyntaxException | NoSuchAlgorithmException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void hashPassword() throws NoSuchAlgorithmException {
        Assert.assertEquals("3858f62230ac3c915f300c664312c63f", new CorosLogin().hashPassword("foobar"));
    }
}
