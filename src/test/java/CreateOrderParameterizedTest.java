import action.GeneralAction;
import action.CreateOrderAction;
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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateOrderParameterizedTest {

    private ClientStellarBurgers clientStellarBurgers;
    private static final ClientStellarBurgers CLIENT_STELLAR_BURGERS = new ClientStellarBurgers();//создали клиент для корректного хеша
    private static final String[] CORRECT_HASH = CreateOrderAction.getHashIngredients(CLIENT_STELLAR_BURGERS);
    private static String accessToken;

    //поля с тестовыми данными
    private final String[] testHash;
    private final String testName;

    //конструктор класса
    public CreateOrderParameterizedTest(String[] testHash, String testName) {
        this.testHash = testHash;
        this.testName = testName;
    }

    //тестовые данные
    @Parameterized.Parameters (name="{1}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {CreateOrderAction.getLongHash(CORRECT_HASH, 1), "хеш длиннее валидного значения на один символ"},
                {CreateOrderAction.getLongHash(CORRECT_HASH, 2), "хеш длиннее валидного значения на два символа"},
                {CreateOrderAction.getLongHash(CORRECT_HASH, 5), "хеш длиннее валидного значения на пять символов"},
                {CreateOrderAction.getShortHash(CORRECT_HASH, 1), "хеш короче валидного значения на один символ"},
                {CreateOrderAction.getShortHash(CORRECT_HASH, 2), "хеш короче валидного значения на два символа"},
                {CreateOrderAction.getShortHash(CORRECT_HASH, 5), "хеш короче валидного значения на пять символов"},
                {CreateOrderAction.getNonExistHash(CORRECT_HASH), "несуществующий хеш валидной длины"},
        };
    }

    @Before
    public void setUp() {
        clientStellarBurgers = new ClientStellarBurgers();
        //создание пользователся
        CreateNewUser createNewUser = new CreateNewUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD, UserData.REGISTRATION_NAME);
        clientStellarBurgers.requestCreateNewUser(createNewUser);
        //авторизация и получение токена
        AuthorizationUser authorizationUser = new AuthorizationUser(UserData.REGISTRATION_EMAIL, UserData.REGISTRATION_PASSWORD);
        Response responseGetToken = clientStellarBurgers.requestAuthorizationUser(authorizationUser);
        accessToken = UserAction.getAccessToken(responseGetToken);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void checkCreateOrderWithUncorrectHash() {
        CreateOrder createOrder = new CreateOrder(testHash);
        Response response = clientStellarBurgers.requestCreateOrderWithAuthorization(createOrder, accessToken);
        GeneralAction.printResponseBody(response);
        GeneralAction.checkResponseCode(response, 500); //проверка кода ответа
    }

    @After
    public void tearDown() {
        clientStellarBurgers.deleteUser(accessToken);
    }
}