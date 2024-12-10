package org.example.pom;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {

    public ProfilePage navigateToProfilePage() {
        $$("a.svelte-1rc85o5").findBy(Condition.text("Hello")).click();
        $$("li.mdc-deprecated-list-item").findBy(Condition.text("Profile"))
                .click();
        return this;
    }

    public ProfilePage openEditModal() {
        $$("button[title='More options'], button[title='Edit']").findBy(Condition.visible)
                .shouldBe(Condition.enabled, Duration.ofSeconds(10))
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .click();
        return this;
    }

    public ProfilePage changeBirthdate(String birthdate) {
        $("input.mdc-text-field__input[type='date']").setValue(birthdate);
        return this;
    }

    public ProfilePage setDatepicker(String cssSelector, String date) {
        executeJavaScript("jQuery(\"input.mdc-text-field__input[type='date']\").datepicker('setDate', '11.11.1111')");
        return this;
    }

    public ProfilePage saveChanges() {
        sleep(1000);
        $("button.button.mdc-button.mdc-button--raised").shouldBe(Condition.enabled).click();
        sleep(1000);
        return this;
    }


    public ProfilePage closeEditModal() {
        sleep(1000);
        $("button.mdc-dialog__close").shouldBe(Condition.enabled).click();
        sleep(1000);
        return this;
    }

    public String getDateOfBirth() {
        sleep(1000);
        return $$(".row.svelte-vyyzan").findBy(Condition.text("Date of birth"))
                .$$(".content.svelte-vyyzan").first().text();
    }
}