package yaroslav.controllers.schemas;

import yaroslav.db.Item;

import java.util.UUID;

public record ShopUnitStatisticUnit(UUID id, String name, UUID parentId, Type type, Long price, String date) {
    public ShopUnitStatisticUnit(Item item) {
        this(item.getId(), item.getName(), item.getParentId(), (item.isCategory() ? Type.CATEGORY : Type.OFFER),
                item.getPrice(), Item.ISO_8601_DATE_FORMAT.format(item.getDate()));
    }
}
