package com.futoshita.util;

import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class HashUtilTest {

    @Test
    public void hashPassword() throws NoSuchAlgorithmException {
        Assert.assertEquals("3858f62230ac3c915f300c664312c63f", HashUtil.hashPassword("foobar"));
    }
}
