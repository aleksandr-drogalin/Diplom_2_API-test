import action.CreateOrderAction;
import action.GeneralAction;
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

public class CreateOrderTest {

    private ClientStellarBurgers clientStellarBurgers; //создали клиент
    private String accessToken;
    private String[] correctHash;
    private final String[] EMPTY_HASH = new String[] {};

    @Before
    public void setUp() {
        clientStellarBurgers = new ClientStellarBurgers();
        // создание пользователся
        CreateNewUser createNewUser = new CreateNewUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD, UserData.REGISTRATION_NAME);
        clientStellarBurgers.requestCreateNewUser(createNewUser);
        //авторизация и получение токена
        AuthorizationUser authorizationUser = new AuthorizationUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD);
        Response responseGetToken = clientStellarBurgers.requestAuthorizationUser(authorizationUser);
        accessToken = UserAction.getAccessToken(responseGetToken);
        //получение валидного хеша ингредиента
        correctHash = CreateOrderAction.getHashIngredients(clientStellarBurgers);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void checkCreateOrderNotAuthorization(){
        CreateOrder createOrder = new CreateOrder(correctHash);
        Response response = clientStellarBurgers.requestCreateOrderNotAuthorization(createOrder);
        GeneralAction.printResponseBody(response);
        GeneralAction.checkResponseCode(response, 308); //перенаправление на страницу авторизации
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем, с ингредиентом")
    public void checkCreateOrderWithAuthorization() {
        CreateOrder createOrder = new CreateOrder(correctHash);
        Response response = clientStellarBurgers.requestCreateOrderWithAuthorization(createOrder, accessToken);
        GeneralAction.printResponseBody(response);
        GeneralAction.checkResponseCode(response,200); //проверка кода ответа
        GeneralAction.checkIsSuccess(response, true); //проверка статуса success
        CreateOrderAction.checkNumberOrder(response); //вернулся номер заказа
        CreateOrderAction.checkNameIngredient(response); //вернулось название ингредиента
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void checkCreateOrderWithEmptyIngredient() {
        CreateOrder createOrder = new CreateOrder(EMPTY_HASH);
        Response response = clientStellarBurgers.requestCreateOrderWithAuthorization(createOrder,accessToken);
        GeneralAction.printResponseBody(response);
        GeneralAction.checkResponseCode(response, 400); //проверка кода ответа
        GeneralAction.checkIsSuccess(response, false); //проверка статуса success
        GeneralAction.checkErrorMessage(response, "Ingredient ids must be provided"); //проверка сообщения message
    }

    @After
    public void tearDown() {
        clientStellarBurgers.deleteUser(accessToken);
    }
}