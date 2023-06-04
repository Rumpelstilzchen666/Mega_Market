package yaroslav.controllers.schemas;

import yaroslav.db.Item;

import java.util.UUID;

public record ShopUnit(UUID id, String name, String date, UUID parentId, Type type, Long price,
                       Iterable<ShopUnit> children) {
    public ShopUnit(Item item, Long price, Iterable<ShopUnit> children) {
        this(item.getId(), item.getName(), Item.ISO_8601_DATE_FORMAT.format(item.getDate()), item.getParentId(),
                (item.isCategory() ? Type.CATEGORY : Type.OFFER), price, children);
    }
}
