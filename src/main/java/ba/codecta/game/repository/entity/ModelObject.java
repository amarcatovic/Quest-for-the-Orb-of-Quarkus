package ba.codecta.game.repository.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
public abstract class ModelObject<K> {
    public abstract K getId();

    @Column(name="CREATED_ON")
    protected LocalDateTime createdOn;
    @Column(name = "MODIFIED_ON")
    protected LocalDateTime modifiedOn;

    // TODO: Improve the method
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o.getClass() != this.getClass())) {
            return false;
        }
        ModelObject that = (ModelObject) o;
        if (getId() == null && that.getId() == null) {
            return false;
        }
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
