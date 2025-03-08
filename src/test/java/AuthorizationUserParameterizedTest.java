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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class AuthorizationUserParameterizedTest implements UserData {

    private ClientStellarBurgers clientStellarBurgers;

    //поля с тестовыми данными
    private final AuthorizationUser authorizationUser;
    private final String testName;

    //конструктор класса
    public AuthorizationUserParameterizedTest(AuthorizationUser authorizationUser, String testName) {
        this.authorizationUser = authorizationUser;
        this.testName = testName;
    }

    //тестовые данные
    @Parameterized.Parameters (name="{1}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {new AuthorizationUser(UserData.REGISTRATION_EMAIL,UserData.UNCORRECT_PASSWORD), "пароль неправильный"},
                {new AuthorizationUser(UserData.REGISTRATION_EMAIL, ""), "пароль незаполнен"},
                {new AuthorizationUser(UserData.REGISTRATION_EMAIL, null), "пароль null"},
                {new AuthorizationUser(UserData.UNCORRECT_EMAIL, UserData.REGISTRATION_PASSWORD), "email неправильный"},
                {new AuthorizationUser("", UserData.REGISTRATION_PASSWORD), "email незаполнен"},
                {new AuthorizationUser(null, UserData.REGISTRATION_PASSWORD), "email null"},
        };
    }

    @Before
    public void setUp() {
        clientStellarBurgers = new ClientStellarBurgers();
        //создание пользователя
        CreateNewUser createNewUser = new CreateNewUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD, UserData.REGISTRATION_NAME);
        clientStellarBurgers.requestCreateNewUser(createNewUser);
    }

    @Test
    @DisplayName("Авторизация пользователя с некорректно заполненными полями")
    public void checkErrorAuthorizationUser() {
        Response response = clientStellarBurgers.requestAuthorizationUser(authorizationUser);
        GeneralAction.checkResponseCode(response, 401); //проверка кода ответа
        GeneralAction.checkIsSuccess(response, false); //проверка статуса success
        GeneralAction.checkErrorMessage(response, "email or password are incorrect"); //проверка сообщения message
    }


    @After
    public void tearDown() {
        AuthorizationUser authorizationCorrectUser = new AuthorizationUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD);
        Response response = clientStellarBurgers.requestAuthorizationUser(authorizationCorrectUser); //запрос авторизации с корректными данными
        String accessToken = UserAction.getAccessToken(response); //получить accessToken
        clientStellarBurgers.deleteUser(accessToken); //удалить учетную запись
    }
}