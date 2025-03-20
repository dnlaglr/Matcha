package gg.matcha.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ThemeTest {
    @AfterEach
    void cleanup() {
        Theme._instance = null;
    }

    @Test
    void constructorShouldCreateDefaultTheme() {
        Theme theme = new Theme();

        assertNotNull(theme);
        assertEquals("JetBrains Mono", theme.getFont());
        assertEquals(12, theme.getFontSize());
        assertEquals("#000000", theme.getBackgroundColor());
        assertEquals("#FFFFFF", theme.getForegroundColor());
    }

    @Test
    void parametersShouldCreateCustomTheme() {
        Theme theme = new Theme("Arial", 14, "#FF0000", "#00FF00");
        assertNotNull(theme);
        assertEquals("Arial", theme.getFont());
        assertEquals(14, theme.getFontSize());
        assertEquals("#FF0000", theme.getBackgroundColor());
        assertEquals("#00FF00", theme.getForegroundColor());
    }

    @Test
    void getterShouldReflectSetterChanges() {
        Theme theme = new Theme();

        theme.setFont("Courier New");
        theme.setFontSize(16);
        theme.setBackgroundColor("#123456");
        theme.setForegroundColor("#654321");
        theme.setWidth(1000);
        theme.setHeight(800);

        assertEquals("Courier New", theme.getFont());
        assertEquals(16, theme.getFontSize());
        assertEquals("#123456", theme.getBackgroundColor());
        assertEquals("#654321", theme.getForegroundColor());
        assertEquals(1000, theme.getWidth());
        assertEquals(800, theme.getHeight());
    }

    @Test
    void themeShouldBeSingleton() {
        Theme instance1 = Theme.getInstance();
        Theme instance2 = Theme.getInstance();

        assertSame(instance1, instance2, "getInstance() should return the same instance.");
    }

    @Test
    void getInstanceShouldBeThreadSafe() throws InterruptedException {
        Runnable getInstanceTask = () -> {
            Theme instance = Theme.getInstance();
            assertNotNull(instance);
        };

        Thread thread1 = new Thread(getInstanceTask);
        Thread thread2 = new Thread(getInstanceTask);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}
