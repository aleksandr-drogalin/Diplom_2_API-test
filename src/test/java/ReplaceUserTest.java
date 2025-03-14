import action.GeneralAction;
import action.UserAction;
import client.ClientStellarBurgers;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.AuthorizationUser;
import model.CreateNewUser;
import model.ReplaceUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReplaceUserTest {

    private ClientStellarBurgers clientStellarBurgers;
    private String accessToken;

    @Before
    public void setUp(){
        clientStellarBurgers = new ClientStellarBurgers();
        // создание пользователя
        CreateNewUser createNewUser = new CreateNewUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD, UserData.REGISTRATION_NAME);
        clientStellarBurgers.requestCreateNewUser(createNewUser);
        //авторизация и получение токена
        AuthorizationUser authorizationUser = new AuthorizationUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD);
        Response responseGetToken = clientStellarBurgers.requestAuthorizationUser(authorizationUser);
        accessToken = UserAction.getAccessToken(responseGetToken);
    }

    @Test
    @DisplayName("Изменение email пользователя на идентичный")
    public void checkReplaceUserWithIdenticalEmail() {
        ReplaceUser replaceUser = new ReplaceUser(UserData.REGISTRATION_EMAIL, null, null);
        Response response = clientStellarBurgers.requestReplaceUserWithAuthorization(replaceUser, accessToken);
        GeneralAction.printResponseBody(response);
        GeneralAction.checkResponseCode(response, 403); //проверка кода ответа
        GeneralAction.checkIsSuccess(response, false); //проверка статуса success
        GeneralAction.checkErrorMessage(response, "User with such email already exists"); //проверка сообщения message
    }

    @After
    public void tearDown() {
        clientStellarBurgers.deleteUser(accessToken);
    }
}