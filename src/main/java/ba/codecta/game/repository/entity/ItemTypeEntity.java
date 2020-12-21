package ba.codecta.game.repository.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(schema = "game", name = "ITEM_TYPE")
public class ItemTypeEntity extends ModelObject<Integer>{
    @SequenceGenerator(
            name = "itemtypeSeq",
            sequenceName = "ITEM_TYPE_SEQ",
            schema = "game",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemtypeSeq")

    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "itemType", fetch = FetchType.EAGER)
    private List<ItemEntity> items = new ArrayList<>();

    @Override
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }
}
