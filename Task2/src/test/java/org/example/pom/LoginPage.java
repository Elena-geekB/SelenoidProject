package org.example.pom;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import com.codeborne.selenide.Condition;

public class LoginPage {

    public LoginPage openPage() {
        open("https://test-stand.gb.ru/login");
        return this;
    }

    public LoginPage typeUsername(String username) {
        $("form#login input[type='text']").shouldBe(Condition.visible).setValue(username);
        return this;
    }

    public LoginPage typePassword(String password) {
        $("form#login input[type='password']").shouldBe(Condition.visible).setValue(password);
        return this;
    }

    public void clickLoginButton() {
        $("form#login button").shouldBe(Condition.enabled).click();
    }

    public void login(String username, String password) {
        this.typeUsername(username);
        this.typePassword(password);
        this.clickLoginButton();
    }
}