package model;

import java.util.List;

public class OrdersUser {

    // класс для десериализации из json при получении заказов конкретного пользователя

    private String success;
    private List<OrdersUserOrders> orders;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<OrdersUserOrders> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersUserOrders> orders) {
        this.orders = orders;
    }
}