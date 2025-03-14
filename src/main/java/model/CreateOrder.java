package model;

public class CreateOrder {

    // класс для сериализации в json при запросе создания заказа

    private String[] ingredients;

    public CreateOrder(String[] ingredients) {
        this.ingredients=ingredients;
    }

    public CreateOrder() {
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
}