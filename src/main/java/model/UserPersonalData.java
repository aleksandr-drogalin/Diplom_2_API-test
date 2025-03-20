package model;

public class UserPersonalData {

    //класс для десериализации из json, при получении пользовательских данных после создания пользователя или его авторизации, находится внутри user класса User

    private String email;
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}