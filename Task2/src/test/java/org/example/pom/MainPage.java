package org.example.pom;

import org.openqa.selenium.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import com.codeborne.selenide.Condition;
import java.time.Duration;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

    public MainPage openAddStudentModal() {
        $("#create-btn").shouldBe(Condition.enabled).click();
        return this;
    }

    public MainPage fillStudentDetails(String firstName, String login) {
        $(By.xpath("//input[@placeholder=' ' and @type='text']")).shouldBe(visible).setValue(firstName);
        $(By.xpath("//span[contains(text(),'Login')]/ancestor::label/descendant::input[@class='mdc-text-field__input']"))
                .shouldBe(visible).setValue(login);
        return this;
    }

    public MainPage clickSaveButton() {
        $(By.xpath("//span[contains(text(),'Save')]/ancestor::button")).shouldBe(Condition.enabled).click();
        return this;
    }

    public MainPage closeAddStudentModal() {
        $("button.mdc-dialog__close").shouldBe(Condition.enabled).click();
        return this;
    }

    public boolean isStudentPresentInTable(String studentName) {
        return $$("td.mdc-data-table__cell").findBy(text(studentName)).shouldBe(visible, Duration.ofSeconds(10)).exists();
    }

    public MainPage clickDeleteButtonForStudent(String studentName) {
        $(By.xpath("//tr[td[contains(@class,'mdc-data-table__cell') and text()='" + studentName +
                "']]/td[last()]/button[contains(@class,'mdc-icon-button')][3]")).click();
        return this;
    }

    public MainPage waitForDeletionToComplete(String uniqueName) {
        $(By.xpath("//td[contains(@class,'mdc-data-table__cell') and text()='" +
                uniqueName + "']/following-sibling::td[contains(text(), 'inactive')]")).shouldBe(visible);
        return this;
    }
}