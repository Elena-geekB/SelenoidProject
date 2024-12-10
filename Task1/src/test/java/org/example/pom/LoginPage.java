package org.example.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriverWait wait;

    @FindBy(css="form#login input[type='text']")
    private WebElement usernameField;

    @FindBy(css="form#login input[type='password']")
    private WebElement passwordField;

    @FindBy(css="form#login button")
    private WebElement loginButton;

    @FindBy(css = "div.error-block")
    private WebElement errorBlock;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    // Метод для выполнения всех шагов входа в систему
    public void login(String username, String password) {
        enterText(usernameField, username);
        enterText(passwordField, password);
        clickElement(loginButton);
    }

    // Универсальный метод для ввода текста в поле
    private void enterText(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
    }

    // Универсальный метод для клика по элементу
    private void clickElement(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    // Получение текста ошибки
    public String getErrorBlockText() {
        return getTextFromElement(errorBlock);
    }

    // Универсальный метод для получения текста из элемента
    private String getTextFromElement(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element))
                .getText().replace("\n", " ");
    }
}
