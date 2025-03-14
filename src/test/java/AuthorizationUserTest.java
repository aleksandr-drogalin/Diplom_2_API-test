import action.GeneralAction;
import action.UserAction;
import client.ClientStellarBurgers;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.AuthorizationUser;
import model.CreateNewUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuthorizationUserTest {

    private ClientStellarBurgers clientStellarBurgers;

    @Before
    public void setUp() {
        CreateNewUser createNewUser = new CreateNewUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD, UserData.REGISTRATION_NAME);
        // создание пользователя
        clientStellarBurgers = new ClientStellarBurgers();
        clientStellarBurgers.requestCreateNewUser(createNewUser);
    }

    @Test
    @DisplayName("Успешная авторизация под существующим пользователем")
    public void successfulAuthorizationWithCorrectUser() {
        AuthorizationUser authorizationUser = new AuthorizationUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD);
        Response response = clientStellarBurgers.requestAuthorizationUser(authorizationUser);

        GeneralAction.checkResponseCode(response, 200); //проверка кода ответа;
        GeneralAction.checkIsSuccess(response, true); //проверка статуса success
        UserAction.checkAccessToken(response); //проверка наличия accessToken
        UserAction.checkRefreshToken(response); //проверка наличия refreshToken
        UserAction.checkUserEmail(response, UserData.REGISTRATION_EMAIL); //вернулся email пользователя
        UserAction.checkUserName(response, UserData.REGISTRATION_NAME); //вернулось имя пользователя
    }

    @After
    public void tearDown(){
        AuthorizationUser authorizationUser = new AuthorizationUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD);
        Response response = clientStellarBurgers.requestAuthorizationUser(authorizationUser);//запрос авторизации
        String accessToken = UserAction.getAccessToken(response);//получение accessToken
        clientStellarBurgers.deleteUser(accessToken);//удаление пользователя
    }
}