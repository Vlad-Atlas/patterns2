package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru"); //компилятор идет в generateUser и просит значение

        var firstMeetingDate = DataGenerator.generateDate(4);
        var secondMeetingDate = DataGenerator.generateDate(7);

        SelenideElement formElement = $("form");
        formElement.$("[placeholder='Город']").setValue(validUser.getCity());
        formElement.$("[placeholder='Дата встречи']").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        formElement.$("[placeholder='Дата встречи']").setValue(firstMeetingDate);
        formElement.$("[name='name']").setValue(validUser.getName());
        formElement.$("[name='phone']").setValue(validUser.getPhone());
        formElement.$("label").click();
        $(withText("Запланировать")).click();
        $(Selectors.withText("Успешно")).should(Condition.visible, Duration.ofSeconds(15));
        $("[class='notification__content']")
                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(Condition.visible);
        formElement.$("[placeholder='Дата встречи']").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        formElement.$("[placeholder='Дата встречи']").setValue(secondMeetingDate);
        $(withText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(Condition.visible);
        $(withText("Перепланировать")).click();
        $("[class='notification__content']")
                .shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate))
                .shouldBe(Condition.visible);
        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
    }
}