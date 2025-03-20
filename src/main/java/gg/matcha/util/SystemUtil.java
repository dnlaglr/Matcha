package gg.matcha.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface SystemUtil {
    static String getOSType() {
        String osType = System.getProperty("os.name").toLowerCase();
        if (osType.contains("win")) {
            return "win";
        } else if (osType.contains("mac")) {
            return "mac";
        } else if (osType.contains("linux")) {
            return "linux";
        } else {
            return null;
        }
    }

    static String getConfigPath() {
        if (getOSType().equals("win")) {
            return System.getenv("APPDATA") + "\\Matcha\\matcha.lua";
        } else {
            return System.getProperty("user.home") + "/.matcha.lua";
        }
    }

    static boolean doesConfigExist() {
        Path configPath = Paths.get(getConfigPath());
        return Files.exists(configPath);
    }
}
