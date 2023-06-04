package yaroslav.controllers.schemas;

import yaroslav.db.Item;

import java.util.ArrayList;

public record ShopUnitStatisticResponse(ArrayList<ShopUnitStatisticUnit> items) {
    public ShopUnitStatisticResponse() {
        this(new ArrayList<>());
    }

    public void add(Item item) {
        items.add(new ShopUnitStatisticUnit(item));
    }
}
