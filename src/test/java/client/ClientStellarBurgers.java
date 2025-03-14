package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import model.AuthorizationUser;
import model.CreateNewUser;
import model.CreateOrder;
import model.ReplaceUser;

import static io.restassured.RestAssured.given;

public class ClientStellarBurgers {

    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final String END_URI_CREATE_NEW_USER = "/api/auth/register"; //эндпоинт создания нового пользователя
    private static final String END_URI_DELETE_USER = "/api/auth/user";//эндпоинт удаления пользователя
    private static final String END_URI_AUTHORIZATION_USER = "/api/auth/login";//эндпоинт авторизации пользователя
    private static final String END_URI_REPLACE_USER="/api/auth/user"; //эндпоинт редактирования профиля
    private static final String END_URI_GET_INGREDIENTS = "/api/ingredients"; //эндпоинт получения данных об ингредиентах
    private static final String END_URI_CREATE_ORDER = "/api/orders"; //эндпоинт создания заказа
    private static final String END_URI_GET_ORDERS_USER = "/api/orders"; //эндпоинт получчения заказа конкретного пользователя

    @Step("Отправка запроса на создание нового пользователя")
    public Response requestCreateNewUser(CreateNewUser createNewUser){
        return given().baseUri(BASE_URI).header("Content-type", "application/json").body(createNewUser).post(END_URI_CREATE_NEW_USER);
    }

    @Step("Отправка запроса на авторизацию пользователя")
    public Response requestAuthorizationUser(AuthorizationUser authorizationUser) {
        return given().baseUri(BASE_URI).header("Content-type", "application/json").body(authorizationUser).post(END_URI_AUTHORIZATION_USER);
    }

    @Step("Отправка запроса на редактирование профиля с авторизацией")
    public Response requestReplaceUserWithAuthorization(ReplaceUser replaceUser, String accessToken) {
        return given().baseUri(BASE_URI).header("Authorization", accessToken).header("Content-type", "application/json").body(replaceUser).patch(END_URI_REPLACE_USER);
    }

    @Step("Отправка запроса на редактирование профиля без авторизациии")
    public Response requestReplaceUserNotAuthorization(ReplaceUser replaceUser) {
        return given().baseUri(BASE_URI).header("Content-type", "application/json").body(replaceUser).patch(END_URI_REPLACE_USER);
    }

    @Step ("Отправка запроса на удаление пользователя")
    public void deleteUser(String accessToken) {
        given().baseUri(BASE_URI).header("Authorization", accessToken).delete(END_URI_DELETE_USER);
    }

    @Step ("Отправка запроса на создание заказа без авторизации")
    public Response requestCreateOrderNotAuthorization(CreateOrder createOrder) {
        return given().baseUri(BASE_URI).header("Content-type", "application/json").body(createOrder).post(END_URI_CREATE_ORDER);
    }

    @Step ("Отправка запроса на создание заказа с авторизацией")
    public Response requestCreateOrderWithAuthorization(CreateOrder createOrder, String accessToken) {
        return given().baseUri(BASE_URI).header("Authorization", accessToken).header("Content-type", "application/json").body(createOrder).post(END_URI_CREATE_ORDER);
    }

    @Step ("Отправка запроса на получение заказа конкретного пользователя без авторизации")
    public Response getOrdersNotAuthorizationUser() {
        return given().baseUri(BASE_URI).get(END_URI_GET_ORDERS_USER);
    }

    @Step ("Отправка запроса на получение заказа конкретного авторизованного пользователя")
    public Response getOrdersAuthorizationUser(String accessToken) {
        return given().baseUri(BASE_URI).header("Authorization", accessToken).get(END_URI_GET_ORDERS_USER);
    }

    //получить данные об ингредиентах
    public Response getIngredients() {
        return given().baseUri(BASE_URI).get(END_URI_GET_INGREDIENTS);
    }
}