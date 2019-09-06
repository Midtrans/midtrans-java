package com.midtrans.java;

import com.midtrans.api.Config;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.midtrans.java.Constant.*;
import static org.junit.Assert.*;

public class ConfigTest {

    private Config config;

    @Before
    public void setUp() throws Exception {
        config = new Config("serverKey", "clientKey", true);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setSERVER_KEY() {
        config.setSERVER_KEY(serverKey);
        assertEquals(serverKey, config.getSERVER_KEY());
    }

    @Test
    public void setCLIENT_KEY() {
        config.setCLIENT_KEY(clientKey);
        assertEquals(clientKey, config.getCLIENT_KEY());
    }

    @Test
    public void setProduction() {
        config.setProduction(false);
        assertEquals(isProduction, false);
    }

    @Test
    public void getBASE_URL() {
    }

    @Test
    public void getSERVER_KEY() {
        assertEquals(serverKey, config.getSERVER_KEY());
    }

    @Test
    public void getCLIENT_KEY() {
        assertEquals(clientKey, config.getCLIENT_KEY());
    }

    @Test
    public void getCoreApiURL() {
        config.getCoreApiURL();
        if (isProduction) {
            assertEquals(COREAPI_PRODUCTION_BASE_URL, config.getBASE_URL());
        } else {
            assertEquals(COREAPI_SANDBOX_BASE_URL, config.getBASE_URL());
        }
    }

    @Test
    public void getSnapApiURL() {
        config.getSnapApiURL();
        if (isProduction) {
            assertEquals(SNAP_PRODUCTION_BASE_URL, config.getBASE_URL());
        } else {
            assertEquals(SNAP_SANDBOX_BASE_URL, config.getBASE_URL());
        }
    }


}