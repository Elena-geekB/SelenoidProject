package org.example.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Instant;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codeborne.selenide.Condition;
import io.qameta.allure.*;

@Epic("Тестирование веб-приложения")
@Feature("Управление профилем и студентами")
public class GeekBrainsStandTests extends BaseTest {

    @Test
    @Owner("Саникидзе Елена")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Авторизация в системе")
    @DisplayName("Проверка авторизации без ввода данных")
    @Description("Тест на отображение ошибки при попытке входа в систему без указания учетных данных")
    public void testLoginWithoutCredentialsShowsError() {
        loginPage.openPage().clickLoginButton();

        String errorMessage = $("div.error-block.svelte-uwkxn9 p")
                .shouldBe(Condition.visible)
                .text();
        assertTrue(errorMessage.contains("Invalid credentials."),
                "Ожидаемое сообщение: 'Invalid credentials.', получено: '" + errorMessage + "'");
    }

    @Test
    @Owner("Саникидзе Елена")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Управление студентами")
    @DisplayName("Добавление и удаление студента")
    @Description("Тест функциональности добавления нового студента и его удаления")
    public void testAddAndDeleteStudentFunctionality() {
        // Генерация уникального имени и логина для нового студента
        String uniqueName = "TestName" + Instant.now().toEpochMilli();
        String uniqueLogin = "TestLogin" + Instant.now().toEpochMilli();

        // Вход в систему и добавление студента
        loginPage.openPage().login(USERNAME, PASSWORD);
        mainPage.openAddStudentModal()
                .fillStudentDetails(uniqueName, uniqueLogin)
                .clickSaveButton();
        mainPage.closeAddStudentModal();

        // Проверка наличия студента в таблице
        assertTrue(mainPage.isStudentPresentInTable(uniqueName),
                "Студент с именем " + uniqueName + " не найден в таблице.");

        // Удаление студента и проверка его отсутствия в таблице
        mainPage.clickDeleteButtonForStudent(uniqueName)
                .waitForDeletionToComplete(uniqueName);
    }

    @Test
    @Owner("Саникидзе Елена")
    @Severity(SeverityLevel.NORMAL)
    @Story("Редактирование профиля")
    @DisplayName("Изменение даты рождения пользователя")
    @Description("Тест функционала изменения даты рождения в профиле пользователя")
    public void testBirthdateChangeFunctionality() {
        // Установка новой даты рождения
        String newBirthdate = "25.08.1989";

        // Вход в систему
        loginPage.openPage().login(USERNAME, PASSWORD);

        // Навигация на страницу профиля пользователя и открытие модального окна редактирования
        profilePage.navigateToProfilePage()
                .openEditModal()
                // Изменение даты рождения и сохранение изменений
                .changeBirthdate(newBirthdate)
                .saveChanges()
                // Закрытие модального окна
                .closeEditModal();

        // Получение и проверка установленной даты рождения
        String observedDate = profilePage.getDateOfBirth();
        assertEquals(newBirthdate, observedDate,
                "Ожидаемая дата рождения после изменения: " + newBirthdate + ", фактическая: " + observedDate);
    }
}
