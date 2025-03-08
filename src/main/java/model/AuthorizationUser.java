package model;

public class AuthorizationUser {

    //класс для сериализации в json при запросе авторизации пользователя

    private String email;
    private String password;

    public AuthorizationUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AuthorizationUser() {
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
}