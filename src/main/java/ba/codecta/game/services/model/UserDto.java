package ba.codecta.game.services.model;

import java.util.List;

public class UserDto {
    private String email;
    private List<HeroDto> heroes;
    private String token;

    public UserDto() {
    }

    public UserDto(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<HeroDto> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<HeroDto> heroes) {
        this.heroes = heroes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
