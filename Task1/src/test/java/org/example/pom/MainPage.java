package org.example.pom;

import org.example.pom.elements.GroupTableRow;
import org.example.pom.elements.StudentTableRow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Optional;

public class MainPage {

    private final WebDriverWait wait;

    @FindBy(css = "nav li.mdc-menu-surface--anchor a")
    private WebElement usernameLinkInNavBar;

    // Create Group Modal Window
    @FindBy(id = "create-btn")
    private WebElement createGroupButton;
    @FindBy(xpath = "//form//span[contains(text(), 'Group name')]/following-sibling::input")
    private WebElement groupNameField;
    @FindBy(css = "form div.submit button")
    private WebElement submitButtonOnModalWindow;
    @FindBy(xpath = "//span[text()='Creating Study Group']" +
            "//ancestor::div[contains(@class, 'form-modal-header')]//button")
    private WebElement closeCreateGroupIcon;

    // Create Students Modal Window
    @FindBy(css = "div#generateStudentsForm-content input")
    private WebElement createStudentsFormInput;
    @FindBy(css = "div#generateStudentsForm-content div.submit button")
    private WebElement saveCreateStudentsForm;
    @FindBy(xpath = "//h2[@id='generateStudentsForm-title']/../button")
    private WebElement closeCreateStudentsFormIcon;

    @FindBy(xpath = "//table[@aria-label='Tutors list']/tbody/tr")
    private List<WebElement> rowsInGroupTable;
    @FindBy(xpath = "//table[@aria-label='User list']/tbody/tr")
    private List<WebElement> rowsInStudentTable;

    public MainPage(WebDriver driver, WebDriverWait wait) {
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    // Обработчик универсальных ожиданий
    private WebElement waitForElementToBeVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    private WebElement waitForElementToBeVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitAndGetGroupTitleByText(String title) {
        return waitForElementToBeVisible(By.xpath(String.format("//table[@aria-label='Tutors list']/tbody//td[text()='%s']", title)));
    }

    public void createGroup(String groupName) {
        waitForElementToBeVisible(createGroupButton).click();
        waitForElementToBeVisible(groupNameField).sendKeys(groupName);
        submitButtonOnModalWindow.click();
        waitAndGetGroupTitleByText(groupName);
    }

    public void closeCreateGroupModalWindow() {
        closeCreateGroupIcon.click();
        wait.until(ExpectedConditions.invisibilityOf(closeCreateGroupIcon));
    }

    public void typeAmountOfStudentsInCreateStudentsForm(int amount) {
        waitForElementToBeVisible(createStudentsFormInput).sendKeys(String.valueOf(amount));
    }

    public void clickSaveButtonOnCreateStudentsForm() throws InterruptedException {
        waitForElementToBeVisible(saveCreateStudentsForm).click();
        Thread.sleep(5000);  // Можно заменить на явное ожидание
    }

    public void closeCreateStudentsModalWindow() {
        closeCreateStudentsFormIcon.click();
        wait.until(ExpectedConditions.invisibilityOf(closeCreateStudentsFormIcon));
    }

    public String getUsernameLabelText() {
        return waitForElementToBeVisible(usernameLinkInNavBar).getText().replace("\n", " ");
    }

    // Универсальные методы для работы с таблицами
    private Optional<GroupTableRow> getGroupRowByTitle(String title) {
        return rowsInGroupTable.stream()
                .map(GroupTableRow::new)
                .filter(row -> row.getTitle().equals(title))
                .findFirst();
    }

    private Optional<StudentTableRow> getStudentRowByName(String name) {
        return rowsInStudentTable.stream()
                .map(StudentTableRow::new)
                .filter(row -> row.getName().equals(name))
                .findFirst();
    }

    public void clickTrashIconOnGroupWithTitle(String title) {
        getGroupRowByTitle(title).ifPresent(GroupTableRow::clickTrashIcon);
    }

    public void clickRestoreFromTrashIconOnGroupWithTitle(String title) {
        getGroupRowByTitle(title).ifPresent(GroupTableRow::clickRestoreFromTrashIcon);
    }

    public void clickAddStudentsIconOnGroupWithTitle(String title) {
        getGroupRowByTitle(title).ifPresent(GroupTableRow::clickAddStudentsIcon);
    }

    public void clickZoomInIconOnGroupWithTitle(String title) {
        getGroupRowByTitle(title).ifPresent(GroupTableRow::clickZoomInIcon);
    }

    public String getStatusOfGroupWithTitle(String title) {
        return getGroupRowByTitle(title).map(GroupTableRow::getStatus).orElse("Unknown");
    }

    public void clickTrashIconOnStudentWithName(String name) {
        getStudentRowByName(name).ifPresent(StudentTableRow::clickTrashIcon);
    }

    public void clickRestoreFromTrashIconOnStudentWithName(String name) {
        getStudentRowByName(name).ifPresent(StudentTableRow::clickRestoreFromTrashIcon);
    }

    public String getStatusOfStudentWithName(String name) {
        return getStudentRowByName(name).map(StudentTableRow::getStatus).orElse("Unknown");
    }

    public String getFirstGeneratedStudentName() {
        wait.until(ExpectedConditions.visibilityOfAllElements(rowsInStudentTable));
        return getStudentRowByName("first").map(StudentTableRow::getName).orElse("No students found");
    }
}
