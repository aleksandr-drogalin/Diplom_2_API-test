import action.GeneralAction;
import client.ClientStellarBurgers;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.CreateNewUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateNewUserParameterizedTest implements UserData {

    private ClientStellarBurgers clientStellarBurgers;

    //поля с тестовыми данными
    private final CreateNewUser createNewUser;
    private final String testName;

    //конструктор класса
    public CreateNewUserParameterizedTest(CreateNewUser createNewUser, String testName) {
        this.createNewUser = createNewUser;
        this.testName = testName;
    }

    //тестовые данные
    @Parameterized.Parameters (name="{1}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {new CreateNewUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD, ""), "незаполнено имя"},
                {new CreateNewUser(UserData.REGISTRATION_EMAIL, "", UserData.REGISTRATION_NAME), "незаполнен пароль"},
                {new CreateNewUser("", UserData.REGISTRATION_PASSWORD, UserData.REGISTRATION_NAME), "незаполнен email"},
                {new CreateNewUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD, null), "имя null"},
                {new CreateNewUser(UserData.REGISTRATION_EMAIL, null, UserData.REGISTRATION_NAME), "пароль null"},
                {new CreateNewUser(null, UserData.REGISTRATION_PASSWORD, UserData.REGISTRATION_NAME), "email null"},
        };
    }

    @Before
    public void setUp() {
        clientStellarBurgers = new ClientStellarBurgers();
    }

    @Test
    @DisplayName("Регистрация пользователя с незаполненными полями")
    public void checkErrorCreateUser() {
        Response response = clientStellarBurgers.requestCreateNewUser(createNewUser);
        GeneralAction.checkResponseCode(response, 403); //проверка кода ответа
        GeneralAction.checkIsSuccess(response, false); //проверка статуса success
        GeneralAction.checkErrorMessage(response, "Email, password and name are required fields"); //проверка сообщения message
    }
}