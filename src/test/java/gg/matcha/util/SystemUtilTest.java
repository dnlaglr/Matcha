package gg.matcha.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SystemUtilTest {
    @AfterEach
    void cleanup() {
        System.clearProperty("os.name");
        System.clearProperty("user.home");
    }

    @Test
    void getConfigPathWindowsShouldReturnCorrectPath() {
        System.setProperty("os.name", "Windows 10");
        System.setProperty("user.home", "C:\\Users\\TestUser");

        String expectedPath = System.getenv("APPDATA") + "\\Matcha\\matcha.lua";
        assertEquals(expectedPath, SystemUtil.getConfigPath());
    }

    @Test
    void getConfigPathMacShouldReturnCorrectPath() {
        System.setProperty("os.name", "Mac OS X");
        System.setProperty("user.home", "/Users/TestUser");

        String expectedPath = "/Users/TestUser/.matcha.lua";
        assertEquals(expectedPath, SystemUtil.getConfigPath());
    }

    @Test
    void getConfigPathLinuxShouldReturnCorrectPath() {
        System.setProperty("os.name", "Linux");
        System.setProperty("user.home", "/home/TestUser");

        String expectedPath = "/home/TestUser/.matcha.lua";
        assertEquals(expectedPath, SystemUtil.getConfigPath());
    }
}
