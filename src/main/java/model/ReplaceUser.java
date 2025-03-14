package model;

public class ReplaceUser {

    // класс для сериализации в json при запросе изменения пользовательских данных

    private String email;
    private String password;
    private String name;

    public ReplaceUser(String email, String password, String name) {
        this.email=email;
        this.password=password;
        this.name=name;
    }

    public ReplaceUser(){
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