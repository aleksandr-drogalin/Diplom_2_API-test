import action.GeneralAction;
import action.CreateOrderAction;
import action.GetOrdersAction;
import action.UserAction;
import client.ClientStellarBurgers;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.AuthorizationUser;
import model.CreateNewUser;
import model.CreateOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetOrdersUserTest {

    private ClientStellarBurgers clientStellarBurgers;
    private String accessToken;

    @Before
    public void setUp() {
        clientStellarBurgers = new ClientStellarBurgers();
        //создание пользователя
        CreateNewUser createNewUser = new CreateNewUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD, UserData.REGISTRATION_NAME);
        clientStellarBurgers.requestCreateNewUser(createNewUser);
        //авторизация и получение токена
        AuthorizationUser authorizationUser = new AuthorizationUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD);
        Response responseGetToken = clientStellarBurgers.requestAuthorizationUser(authorizationUser);
        accessToken = UserAction.getAccessToken(responseGetToken);
        //создание заказа
        String[] hashIngredient = CreateOrderAction.getHashIngredients(clientStellarBurgers);
        CreateOrder createOrder = new CreateOrder(hashIngredient);
        clientStellarBurgers.requestCreateOrderWithAuthorization(createOrder, accessToken);
    }

    @Test
    @DisplayName("Получить заказы неавторизованного пользователя")
    public void checkGetOrdersNotAuthorizationUser() {
        Response response = clientStellarBurgers.getOrdersNotAuthorizationUser();
        GeneralAction.checkResponseCode(response, 401); //проверка кода ответа
        GeneralAction.checkIsSuccess(response, false); //проверка статуса success
        GeneralAction.checkErrorMessage(response, "You should be authorised"); //проверка сообщения message
    }

    @Test
    @DisplayName("Получить заказы авторизованного пользователя")
    public void checkGetOrdersAuthorizationUser() {
        Response response = clientStellarBurgers.getOrdersAuthorizationUser(accessToken);
        GeneralAction.printResponseBody(response);
        GeneralAction.checkResponseCode(response, 200); //проверка кода ответа
        GeneralAction.checkIsSuccess(response, true); //проверка статуса success
        GetOrdersAction.checkOrdersListNotNull(response);//вернулся непустой список заказов
    }

    @After
    public void tearDown() {
        clientStellarBurgers.deleteUser(accessToken);
    }
}