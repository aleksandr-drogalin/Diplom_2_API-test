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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ReplaceUserParameterizedTest implements UserData {

    private ClientStellarBurgers clientStellarBurgers;
    private static String accessToken;

    //поля с тестовыми данными
    private final ReplaceUser replaceUser;
    private final String expectedEmail;
    private final String expectedName;
    private final String testName;

    //конструктор класса
    public ReplaceUserParameterizedTest(ReplaceUser replaceUser, String expectedEmail, String expectedName, String testName) {
        this.replaceUser = replaceUser;
        this.expectedEmail = expectedEmail;
        this.expectedName = expectedName;
        this.testName = testName;
    }

    //тестовые данные
    @Parameterized.Parameters (name="{3}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {new ReplaceUser(UserData.REPLACE_EMAIL,null,null), UserData.REPLACE_EMAIL, UserData.REGISTRATION_NAME, "замена email"},
                {new ReplaceUser(null,UserData.REPLACE_PASSWORD,null), UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_NAME, "замена пароля"},
                {new ReplaceUser(null, null, UserData.REPLACE_NAME), UserData.REGISTRATION_EMAIL, UserData.REPLACE_NAME, "замена имени"},
        };
    }

    @Before
    public void setUp(){
        //создание пользователя
        clientStellarBurgers = new ClientStellarBurgers();
        CreateNewUser createNewUser = new CreateNewUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD, UserData.REGISTRATION_NAME);
        clientStellarBurgers.requestCreateNewUser(createNewUser);
        //авторизация и получение токена
        AuthorizationUser authorizationUser = new AuthorizationUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD);
        Response responseGetToken = clientStellarBurgers.requestAuthorizationUser(authorizationUser);
        accessToken = UserAction.getAccessToken(responseGetToken);
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void checkReplaceWithAuthorization() {
        Response response = clientStellarBurgers.requestReplaceUserWithAuthorization(replaceUser, accessToken);
        GeneralAction.printResponseBody(response);
        GeneralAction.checkResponseCode(response, 200); //проверка кода ответа
        GeneralAction.checkIsSuccess(response, true); //проверка статуса success
        UserAction.checkUserEmail(response, expectedEmail); //вернулся email пользователя
        UserAction.checkUserName(response, expectedName); //вернулось имя пользователя
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void checkReplaceNotAuthorization() {
        Response response = clientStellarBurgers.requestReplaceUserNotAuthorization(replaceUser);
        GeneralAction.checkResponseCode(response, 401); //проверка кода ответа
        GeneralAction.checkIsSuccess(response, false); //проверка статуса success
        GeneralAction.checkErrorMessage(response, "You should be authorised"); //проверка сообщения message
    }

    @After
    public void tearDown() {
        clientStellarBurgers.deleteUser(accessToken);
    }
}