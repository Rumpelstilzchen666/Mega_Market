package yaroslav.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yaroslav.controllers.schemas.ShopUnit;
import yaroslav.db.Item;
import yaroslav.db.ItemRepository;

import java.util.*;

@RestController
@RequestMapping("/nodes")
public class NodesController {
    private final ItemRepository itemRepository;

    public NodesController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    // Отладочный метод
    public ResponseEntity<Long> printAll() {
        for(Item item : itemRepository.findAll()) {
            System.out.println(item);
        }
        return ResponseEntity.ok(itemRepository.count());
    }

    @GetMapping(path = "/{id}", produces = {"application/json"})
    // Можно сразу получать UUID, но тогда при неверном формате нет поясняющего сообщения,
    // и нет возможности проверить другие форматы
    public ResponseEntity getById(@PathVariable String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
            //TODO проверить другие форматы
        } catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        Optional<Item> itemOptional = itemRepository.findById(uuid);
        if(itemOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
        }
        return ResponseEntity.ok(getShopUnit(itemOptional.get()).shopUnit());
    }

    private ShopUnitWithNOffers getShopUnit(Item item) {
        if(item.isCategory()) {
            final Collection<Item> childrenItems = (Collection<Item>) itemRepository.findAllByParentId(item.getId());
            final ArrayList<ShopUnit> children = new ArrayList<>(childrenItems.size());
            long nOffers = 0, sumPrice = 0;
            for(Item child : childrenItems) {
                ShopUnitWithNOffers shopUnitWithNOffers = getShopUnit(child);
                children.add(shopUnitWithNOffers.shopUnit());
                nOffers += shopUnitWithNOffers.nOffers();
                sumPrice += shopUnitWithNOffers.shopUnit().price() * shopUnitWithNOffers.nOffers();
            }
            return new ShopUnitWithNOffers(new ShopUnit(item, (nOffers == 0 ? 0 : sumPrice / nOffers), children),
                    nOffers);
        } else {
            return new ShopUnitWithNOffers(new ShopUnit(item, item.getPrice(), null), 1);
        }
    }

    private record ShopUnitWithNOffers(ShopUnit shopUnit, long nOffers) { }
}
