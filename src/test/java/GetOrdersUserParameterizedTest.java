import action.CreateOrderAction;
import action.GeneralAction;
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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class GetOrdersUserParameterizedTest {

    private ClientStellarBurgers clientStellarBurgers;
    private static String accessToken;

    // поля с тестовыми данными
    private final int countCreatedOrders;
    private final int expectedReturnedOrders;
    private final String testName;

    //конструктор класса
    public GetOrdersUserParameterizedTest(int countCreatedOrders, int expectedReturnedOrders, String testName) {
        this.countCreatedOrders = countCreatedOrders;
        this.expectedReturnedOrders = expectedReturnedOrders;
        this.testName = testName;
    }

    //тестовые данные
    @Parameterized.Parameters (name="{2}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {15, 15, "15 заказов"}, // 15 создано 15 должно вернуться
                {48, 48, "48 заказов"}, // 48 создано 48 должно вернуться
                {49, 49, "49 заказов"}, // 49 создано 49 должно вернуться
                {50, 50, "50 заказов"}, // 50 создано 50 должно вернуться
                {51, 50, "51 заказ"}, // 51 создан 50 должно вернуться
                {52, 50, "52 заказа"}, // 52 создано 50 должно вернуться
                {54, 50, "54 заказа"}, // 54 создано 50 должно вернуться
        };
    }

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
        //создание заказов в количестве countCreatedOrders
        for (int i = 0; i < countCreatedOrders; i++) {
            String[] hashIngredient = CreateOrderAction.getHashIngredients(clientStellarBurgers);
            CreateOrder createOrder = new CreateOrder(hashIngredient);
            clientStellarBurgers.requestCreateOrderWithAuthorization(createOrder, accessToken);
        }
    }

    @Test
    @DisplayName("Возвращается максимум 50 отсортированных по времени обновления заказов")
    public void checkGetOrdersAuthorizationUser() {
        Response response = clientStellarBurgers.getOrdersAuthorizationUser(accessToken);
        GeneralAction.printResponseBody(response);
        GetOrdersAction.checkReturnOrders50(response, expectedReturnedOrders); //вернулся список заказов, с количеством заказов не более expectedReturnedOrders
        GetOrdersAction.checkSortOrdersTimeUpdate(response);//заказы отсортированы по времени обновления
    }

    @After
    public void tearDown() {
        clientStellarBurgers.deleteUser(accessToken);
    }
}