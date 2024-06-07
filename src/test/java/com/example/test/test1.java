
package com.example.test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import com.example.test.Login;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


@Tag("playwright")
public class test1 {

    // This will be injected with the random free port
    // number that was allocated
    @LocalServerPort
    private int port = 8080;

    static Playwright playwright = Playwright.create();
    Browser browser = playwright.chromium().launch();
    Page page = browser.newPage();

    @Test
    public void logInAndOut() {        
        page.navigate("http://localhost:" + port + "/");
        Login.login(page);
        assertThat(page.getByText("Log out")).isVisible();
        Login.logout(page);
        assertThat(page.locator("[role=\"button\"]:has-text(\"Log in\")")).isVisible();
    }
    @Test
    public void addAndDelete() {        
        page.navigate("http://localhost:" + port + "/");
        Login.login(page);
        page.locator("#input-vaadin-combo-box-28").fill("Fredi Wasström");
        page.locator("#input-vaadin-date-picker-29").fill("6/8/2024");
        page.locator("#input-vaadin-integer-field-32").fill("30");
        page.locator("#input-vaadin-integer-field-32").fill("30");
        page.locator("#input-vaadin-integer-field-36").fill("30");
        page.locator("#input-vaadin-text-field-37").fill("TEST");
        page.locator("#input-vaadin-combo-box-39").fill("intern project");
        assertThat(page.locator("#vaadin-add-button")).isVisible();
    }
}