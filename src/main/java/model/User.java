package model;

public class User {

    //класс для десериализации из json, при получении пользовательских данных после создания пользователя или его авторизации

    private boolean success;
    private UserPersonalData user;
    private String accessToken;
    private String refreshToken;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UserPersonalData getUser() {
        return user;
    }

    public void setUser(UserPersonalData user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}