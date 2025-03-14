package action;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserAction {

    // получить accessToken
    public static String getAccessToken(Response response) {
        User user = response.body().as(User.class); //десериализация ответа в объект
        return user.getAccessToken(); //возвращение accessToken
    }

    @Step("При успешном запросе возвращается email пользователя")
    public static void checkUserEmail(Response response, String expectedEmail) {
        response.then().assertThat().body("user.email", equalTo(expectedEmail));
    }

    @Step("При успешном запросе возвращается имя пользователя")
    public static void checkUserName(Response response, String expectedName) {
        response.then().assertThat().body("user.name", equalTo(expectedName));
    }

    @Step("При успешном запросе возвращается accessToken")
    public static void checkAccessToken(Response response) {
        response.then().assertThat().body("accessToken", notNullValue());
    }

    @Step("При успешном запросе возвращается refreshToken")
    public static void checkRefreshToken(Response response) {
        response.then().assertThat().body("refreshToken", notNullValue());
    }
}