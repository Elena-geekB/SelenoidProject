package org.example.tests;

import io.qameta.allure.*;
import org.example.pom.LoginPage;
import org.example.pom.MainPage;
import org.example.utils.ScreenshotUtil;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Тестирование веб-приложения")
@Feature("Управление пользователями и группами")
public class GeekBrainsStandTests {

    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private MainPage mainPage;

    private static String USERNAME;
    private static String PASSWORD;
    private static final String SELENOID_URL = "http://localhost:4444/wd/hub";

    @BeforeAll
    public static void setupClass() {
        USERNAME = System.getProperty("geekbrains_username", System.getenv("geekbrains_username"));
        PASSWORD = System.getProperty("geekbrains_password", System.getenv("geekbrains_password"));
    }

    @BeforeEach
    public void setupTest() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.setCapability("browserVersion", "123.0");
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

        options.setCapability("selenoid:options", selenoidOptions);

        driver = new RemoteWebDriver(new URL(SELENOID_URL), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();
        driver.get("https://test-stand.gb.ru/login");
        loginPage = new LoginPage(driver, wait);
    }

    @Test
    @Owner("Саникидзе Елена")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Авторизация в системе")
    @DisplayName("Тест входа с пустыми полями")
    @Description("Проверка ошибки авторизации при попытке входа без данных")
    public void testLoginWithEmptyFields() throws IOException {
        loginPage.clickLoginButton();
        assertEquals("401 Invalid credentials.", loginPage.getErrorBlockText());
        ScreenshotUtil.saveScreenshot(driver, "login-error-" + System.currentTimeMillis());
    }

    @Test
    @Owner("Саникидзе Елена")
    @Severity(SeverityLevel.NORMAL)
    @Story("Управление группами")
    @DisplayName("Тест создания группы")
    @Description("Проверка возможности создания новой группы")
    public void testAddingGroupOnMainPage() throws IOException {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        String groupTestName = "New Test Group " + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        assertTrue(mainPage.waitAndGetGroupTitleByText(groupTestName).isDisplayed());
        ScreenshotUtil.saveScreenshot(driver, "create-group-" + System.currentTimeMillis());
    }

    @Test
    @Owner("Саникидзе Елена")
    @Severity(SeverityLevel.NORMAL)
    @Story("Управление группами")
    @DisplayName("Тест архивирования и восстановления группы")
    @Description("Проверка возможности архивирования и восстановления группы")
    public void testArchiveGroupOnMainPage() throws IOException {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        String groupTestName = "New Test Group " + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        mainPage.closeCreateGroupModalWindow();
        assertEquals("active", mainPage.getStatusOfGroupWithTitle(groupTestName));
        mainPage.clickTrashIconOnGroupWithTitle(groupTestName);
        assertEquals("inactive", mainPage.getStatusOfGroupWithTitle(groupTestName));
        mainPage.clickRestoreFromTrashIconOnGroupWithTitle(groupTestName);
        assertEquals("active", mainPage.getStatusOfGroupWithTitle(groupTestName));
        ScreenshotUtil.saveScreenshot(driver, "archive-restore-group-" + System.currentTimeMillis());
    }

    @Test
    @Owner("Саникидзе Елена")
    @Severity(SeverityLevel.NORMAL)
    @Story("Управление пользователями")
    @DisplayName("Тест блокировки студента")
    @Description("Проверка возможности блокировки и разблокировки студента в таблице")
    public void testBlockingStudentInTableOnMainPage() throws InterruptedException, IOException {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        String groupTestName = "New Test Group " + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        mainPage.closeCreateGroupModalWindow();
        mainPage.clickAddStudentsIconOnGroupWithTitle(groupTestName);
        mainPage.typeAmountOfStudentsInCreateStudentsForm(3);
        mainPage.clickSaveButtonOnCreateStudentsForm();
        mainPage.closeCreateStudentsModalWindow();
        mainPage.clickZoomInIconOnGroupWithTitle(groupTestName);
        String firstGeneratedStudentName = mainPage.getFirstGeneratedStudentName();
        assertEquals("active", mainPage.getStatusOfStudentWithName(firstGeneratedStudentName));
        mainPage.clickTrashIconOnStudentWithName(firstGeneratedStudentName);
        assertEquals("block", mainPage.getStatusOfStudentWithName(firstGeneratedStudentName));
        mainPage.clickRestoreFromTrashIconOnStudentWithName(firstGeneratedStudentName);
        assertEquals("active", mainPage.getStatusOfStudentWithName(firstGeneratedStudentName));
        ScreenshotUtil.saveScreenshot(driver, "block-unblock-student-" + System.currentTimeMillis());
    }

    @Test
    @Owner("Саникидзе Елена")
    @Severity(SeverityLevel.NORMAL)
    @Story("Управление пользователями")
    @DisplayName("Тест редактирования пользователя")
    @Description("Проверка возможности редактирования информации о пользователе")
    public void testEditUserInfo() throws IOException {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        String userNameToEdit = mainPage.getFirstUserName();
        mainPage.clickEditUserButton(userNameToEdit);
        String newUserName = "Edited User " + System.currentTimeMillis();
        mainPage.updateUserName(newUserName);
        assertEquals(newUserName, mainPage.getUserName(userNameToEdit));
        ScreenshotUtil.saveScreenshot(driver, "edit-user-" + System.currentTimeMillis());
    }

    @AfterEach
    public void teardown() {
        try {
            Thread.sleep(10000);  // Задержка перед закрытием для просмотра или логирования
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.quit();
    }
}
