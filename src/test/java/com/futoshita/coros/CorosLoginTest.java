package com.futoshita.coros;

import com.futoshita.coros.api.CorosLogin;
import com.futoshita.util.HashUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class CorosLoginTest {

    @Before
    public void before() {
        try (InputStream input = new FileInputStream("src/test/resources/authentication.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            AppParameters.getInstance().setUsername(properties.getProperty("username"));
            AppParameters.getInstance().setPassword(HashUtil.hashPassword(properties.getProperty("password")));
            AppParameters.getInstance().setOutputDirectory("target");
            AppParameters.getInstance().setDebug(true);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @After
    public void after() {
        AppParameters.getInstance().reset();
    }

    @Test
    public void login() {
        try {
            CorosLogin.login();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(AppParameters.getInstance().getAccessToken());
    }
}
