package yaroslav.db;

import yaroslav.controllers.schemas.ShopUnitImport;
import yaroslav.controllers.schemas.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "items", indexes = {@Index(name = "id", columnList = "id", unique = true),
                                  @Index(name = "parent_id", columnList = "parent_id")})
public class Item implements Serializable {
    public static final SimpleDateFormat ISO_8601_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    @Id
    @NotNull
    private UUID id;

    @NotNull
    private String name;

    @Column(name = "parent_id")
    private UUID parentId;

    @NotNull
    private Boolean category;

    private Long price;

    @NotNull
    private Date date;

    protected Item() { }

    public Item(final ShopUnitImport item, final Date date) {
        this.id = item.id();
        this.parentId = item.parentId();
        this.name = item.name();
        this.price = item.price();
        this.category = (item.type() == Type.CATEGORY);
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(final UUID parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(final Long price) {
        this.price = price;
    }

    public Boolean isCategory() {
        return category;
    }

    public void setCategory(final Boolean category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", parentId=" + parentId + ", name='" + name + '\'' + ", price=" + price +
                ", category=" + category + ", date=" + ISO_8601_DATE_FORMAT.format(date) + '}';
    }
}
