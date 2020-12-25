package ba.codecta.game.services.model;

import javax.validation.constraints.NotBlank;

public class NewGameDto {
    @NotBlank(message = "Hero Name must be set")
    private String heroName;
    @NotBlank(message = "You need to give your hero a back story")
    private String heroDescription;

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public String getHeroDescription() {
        return heroDescription;
    }

    public void setHeroDescription(String heroDescription) {
        this.heroDescription = heroDescription;
    }
}
