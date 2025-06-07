package com.steve.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HttpVersionTest {
    @Test
    void getCompatibleVersionExactMatch() {
        HttpVersions version = null;
        try {
            version = HttpVersions.getCompatibleVersion("HTTP/1.1");
        } catch (BadHttpVersionException e) {
            e.printStackTrace();
            fail();
        }
        assertNotNull(version);
        assertEquals(version, HttpVersions.HTTP_1_1);

    }

    @Test
    void getCompatibleVersionBadFormat() {
        HttpVersions version = null;
        try {
            version = HttpVersions.getCompatibleVersion("http/1.1");
            fail();
        } catch (BadHttpVersionException e) {


        }

    }

    @Test
    void getCompatibleVersionHigher() {
        HttpVersions version = null;
        try {
            version = HttpVersions.getCompatibleVersion("HTTP/1.2");
            assertNotNull(version);
            assertEquals(version, HttpVersions.HTTP_1_1);
        } catch (BadHttpVersionException e) {
            e.printStackTrace();
            fail();
        }

    }
}
