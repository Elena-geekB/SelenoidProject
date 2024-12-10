package org.example.tests;

import com.codeborne.selenide.Selenide;
import org.example.pom.LoginPage;
import org.example.pom.MainPage;
import com.codeborne.selenide.Configuration;
import org.example.pom.ProfilePage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.ArrayList;

public abstract class BaseTest {

    protected LoginPage loginPage;
    protected MainPage mainPage;
    protected ProfilePage profilePage;

    protected static final String USERNAME = System.getProperty("geekbrains_username", System.getenv("geekbrains_username"));
    protected static final String PASSWORD = System.getProperty("geekbrains_password", System.getenv("geekbrains_password"));
    private static final String SELENOID_URL = "http://localhost:4444/wd/hub";

    @BeforeEach
    public void setUp() {
        Configuration.browserSize = "1920x1080";
        Configuration.baseUrl = "https://test-stand.gb.ru";
        Configuration.timeout = 30000;
        Configuration.remote = SELENOID_URL;
        Configuration.browser = "chrome";

        Configuration.browserCapabilities.setCapability("browserVersion", "123.0");
        HashMap<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVideo", true);
        selenoidOptions.put("name", "Test badge");
        selenoidOptions.put("sessionTimeout", "15m");
        ArrayList<String> env = new ArrayList<>();
        env.add("TZ=UTC");
        selenoidOptions.put("env", env);
        selenoidOptions.put("enableLog", true);
        HashMap<String, Object> labels = new HashMap<>();
        labels.put("manual", "true");
        selenoidOptions.put("labels", labels);

        Configuration.browserCapabilities.setCapability("selenoid:options", selenoidOptions);

        loginPage = new LoginPage();
        mainPage = new MainPage();
        profilePage = new ProfilePage();
    }

    @AfterEach
    public void tearDown() {
        Selenide.screenshot("screenshot-" + System.currentTimeMillis());
        com.codeborne.selenide.Selenide.closeWebDriver();
    }
}