package com.example.test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Tag("playwright")
public class TrivialPlaywrightTest {

    // This will be injected with the random free port
    // number that was allocated
    @LocalServerPort
    private int port;

    static Playwright playwright = Playwright.create();

    @Test
    public void testClicking() {
        port = 8080;
        Browser browser = playwright.chromium().launch();
        Page page = browser.newPage();
        page.navigate("http://localhost:" + port + "/");
        System.out.println("Site opened");
        assertThat(page.locator("#vaadin-add-button")).isVisible();

        // page.locator("//vaadin-button[contains(text(),'Click Me')]").click();

        // assertThat(page.locator("#msg")).containsText("Clicked!");

    }
}