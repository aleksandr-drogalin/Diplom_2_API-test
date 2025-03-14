package action;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.OrdersUser;
import model.OrdersUserOrders;
import org.junit.Assert;

import java.util.*;

import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrdersAction {

    @Step ("При успешном запросе возвращается непустой список заказов")
    public static void checkOrdersListNotNull(Response response) {
        response.then().assertThat().body("orders", notNullValue());
    }

    @Step ("При успешном запросе возвращаются не более 50 заказов")
    public static void checkReturnOrders50(Response response, int countOrders) {
        OrdersUser ordersUser = response.as(OrdersUser.class);
        List<OrdersUserOrders> ordersUserOrders = ordersUser.getOrders();
        Assert.assertTrue("Количество заказов в списке не соответствует ожидаемому", ordersUserOrders.size()==countOrders);
    }

    @Step ("При успешном запросе заказы отсортированы по времени обновления")
    public static void checkSortOrdersTimeUpdate(Response response) {
        OrdersUser ordersUser = response.as(OrdersUser.class);
        List<OrdersUserOrders> ordersUserOrders = ordersUser.getOrders();
        List<String> updatedAtList = new ArrayList<>(); //создание списка времени изменения заказов
        // заполнение списка времени изменения заказов
        for(OrdersUserOrders element:ordersUserOrders) {
            updatedAtList.add(element.getUpdatedAt());
        }

        //Хеш-таблица несортированного списка
        HashMap<Integer, String> updatedAtUnsortedMap = new HashMap<Integer, String>();
        for(String element: updatedAtList){
            updatedAtUnsortedMap.put(updatedAtList.indexOf(element), element);
        }
        System.out.println(updatedAtUnsortedMap);

        //сортируем список
        updatedAtList.sort(String.CASE_INSENSITIVE_ORDER);

        //Хеш-таблица сортированного списка
        HashMap<Integer, String> updatedAtSortedMap = new HashMap<Integer, String>();
        for(String element: updatedAtList){
            updatedAtSortedMap.put(updatedAtList.indexOf(element), element);
        }
        System.out.println(updatedAtSortedMap);

        Assert.assertEquals("Заказы отсортированы не по времени изменения", updatedAtUnsortedMap, updatedAtSortedMap);
    }
}