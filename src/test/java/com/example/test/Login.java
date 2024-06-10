package com.example.test;

import com.microsoft.playwright.Page;

public class Login {
    public static void login(Page page) {
        // Fill username and password
        page.locator("#input-vaadin-text-field-6").fill("fredi");
        page.locator("#input-vaadin-password-field-7").fill("user");
        page.locator("[role=\"button\"]:has-text(\"Log in\")").click();
    }

    public static void logout(Page page) {
        page.getByText("Log out").click();
    }
}
