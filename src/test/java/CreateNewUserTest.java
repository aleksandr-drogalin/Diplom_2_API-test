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

public class CreateNewUserTest {

    private ClientStellarBurgers clientStellarBurgers;

    @Before
    public void setUp() {
        clientStellarBurgers = new ClientStellarBurgers();
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    public void successfulCreateUser(){
        CreateNewUser createNewUser = new CreateNewUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD, UserData.REGISTRATION_NAME);
        Response response = clientStellarBurgers.requestCreateNewUser(createNewUser);
        GeneralAction.checkResponseCode(response, 200); // проверка кода ответа
        GeneralAction.checkIsSuccess(response, true); //проверка статуса success
        UserAction.checkUserEmail(response, UserData.REGISTRATION_EMAIL); //вернулся email пользователя
        UserAction.checkUserName(response, UserData.REGISTRATION_NAME); //вернулось имя пользователя
        UserAction.checkAccessToken(response); //проверка наличия accessToken
        UserAction.checkRefreshToken(response); //проверка наличия refreshToken
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых пользователей")
    public void errorCreateTwoIdenticalUser() {
        CreateNewUser createNewUser1 = new CreateNewUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD, UserData.REGISTRATION_NAME);
        CreateNewUser createNewUser2 = new CreateNewUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD, UserData.REGISTRATION_NAME);
        clientStellarBurgers.requestCreateNewUser(createNewUser1);
        Response response2 = clientStellarBurgers.requestCreateNewUser(createNewUser2);
        GeneralAction.checkResponseCode(response2, 403); //проверка кода ответа
        GeneralAction.checkIsSuccess(response2, false); //проверка статуса success
        GeneralAction.checkErrorMessage(response2, "User already exists"); //проверка сообщения message
    }

    @After
    public void tearDown(){
        AuthorizationUser authorizationUser = new AuthorizationUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD);
        Response response = clientStellarBurgers.requestAuthorizationUser(authorizationUser);//запрос авторизации
        String accessToken = UserAction.getAccessToken(response);//получение accessToken
        clientStellarBurgers.deleteUser(accessToken);//удаление пользователя
    }
}