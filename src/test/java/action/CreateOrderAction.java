package action;

import client.ClientStellarBurgers;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Ingredients;
import model.IngredientsData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderAction {

    @Step("При успешном запросе возвращается номер заказа")
    public static void checkNumberOrder(Response response) {
        response.then().assertThat().body("order.number", notNullValue());
    }

    @Step("При успешном запросе возвращается название ингредиентов")
    public static void checkNameIngredient(Response response) {
        response.then().assertThat().body("name", notNullValue());
    }

    //получить массив с валидным хешем
    public static String[] getHashIngredients(ClientStellarBurgers clientStellarBurgers) {
        Ingredients ingredients = clientStellarBurgers.getIngredients().as(Ingredients.class); //ответ как объект класса
        List<IngredientsData> data = ingredients.getData(); //получение списка ингредиентов
        List<String> hashIngredients = new ArrayList<>();//создание списка для валидных хешей
        //заполняем список валидных хешей
        for(IngredientsData element:data) {
            hashIngredients.add(element.get_id());
        }
        //возвращаем массив с валидным хешем случайного ингредиента
        Random random = new Random();
        int index = random.nextInt(hashIngredients.size()); //случайный индекс элемента в списке валидных хешей
        String id = hashIngredients.get(index); //получение хеша по случайному индексу
        System.out.println(id);
        return new String[] {id}; //возвращение массива с одним случайным валидным хешем
    }

    //получить массив с невалидным более длинным хешем
    public static String[] getLongHash(String[] correctHashArray, int countSymbol) {
        String correctHash = correctHashArray[0]; //из списка валидных хешей получили хеш с нулевым индексом
        String dataForRandomSymbol = "abcdefghijklmnopqrstuvwxyz0123456789"; //строка с допустимыми символами для увеличения длины хеша случайными значениями
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(correctHash); //добавление валидного хэша в stringBuilder
        for(int i=1; i<=countSymbol; i++) {
            int randomCharNumber = random.nextInt(dataForRandomSymbol.length()); //случайный номер символа в строке с допустимыми символами
            char randomChar = dataForRandomSymbol.charAt(randomCharNumber); //возвращение символа по его номеру в строке
            stringBuilder.append(randomChar); //добавление полученного символа в конец string builder
        }
        String longHash = stringBuilder.toString(); //преобразование string builder в строку
        System.out.println(longHash);
        return new String[] {longHash};
    }

    //получить массив с невалидным более коротким хешем (короче на количество countSymbol)
    public static String[] getShortHash(String[] correctHashArray, int countSymbol) {
        String correctHash = correctHashArray[0]; //из списка валидных хешей получили хеш с нулевым индексом
        if (countSymbol >= correctHash.length()) throw new IllegalArgumentException(); //исключение, если хотим сократить хеш на большее число символов чем в нем есть
        String shortHash = correctHash.substring(0, correctHash.length()-countSymbol); //получили хеш короче на countSymbol
        System.out.println(shortHash);
        return new String[] {shortHash};
    }

    //получить массив с несуществующим хешем валидной длинны
    public static String[] getNonExistHash(String[] correctHashArray) {
        String correctHash = correctHashArray[0]; //из списка валидных хешей получили хеш с нулевым индексом
        int length = correctHash.length(); //определение валидной длины хеша
        String dataForRandomString = "abcdefghijklmnopqrstuvwxyz0123456789"; //строка с допустимыми символами для генерации случайного хеша
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<length; i++){
            int randomCharNumber = random.nextInt(dataForRandomString.length()); //случайный номер символа в строке с допустимыми символами
            char randomChar = dataForRandomString.charAt(randomCharNumber); //возвращение символа по его номеру в строке
            stringBuilder.append(randomChar); //добавление полученного символа в конец string builder
        }
        String nonExistHash = stringBuilder.toString(); //преобразование string builder в строку
        System.out.println(nonExistHash);
        return new String[] {nonExistHash};
    }
}