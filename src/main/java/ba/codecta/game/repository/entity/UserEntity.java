package ba.codecta.game.repository.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "game", name = "USER")
public class UserEntity extends ModelObject<Integer>{
    @SequenceGenerator(
            name = "userSeq",
            sequenceName = "USER_SEQ",
            schema = "game",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    private String email;
    private String passwordHash;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<HeroEntity> heroes = new ArrayList<>();

    @Override
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<HeroEntity> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<HeroEntity> heroes) {
        this.heroes = heroes;
    }
}
