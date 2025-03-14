package action;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.equalTo;

public class GeneralAction {

    //вывести ответ на запрос в консоль
    public static void printResponseBody(Response response) {
        System.out.println(response.body().asString());
    }

    @Step("Возвращается корректный код ответа")
    public static void checkResponseCode(Response response, int expectedCode) {
        response.then().statusCode(expectedCode);
    }

    @Step("Возвращается корректный статус (success)")
    public static void checkIsSuccess(Response response, boolean expectedResult) {
        response.then().assertThat().body("success", equalTo(expectedResult));
    }

    @Step("Возвращается сообщение об ошибке (message)")
    public static void checkErrorMessage(Response response, String expectedMessage) {
        response.then().assertThat().body("message", equalTo(expectedMessage));
    }
}