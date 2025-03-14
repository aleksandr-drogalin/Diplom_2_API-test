package model;

public class CreateNewUser {

    // класс для сериализации в json при запросе создания нового пользователя

    private String email;
    private String password;
    private String name;

    public CreateNewUser(String email, String password, String name) {
        this.email=email;
        this.password=password;
        this.name=name;
    }

    public CreateNewUser(){
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
    }
}