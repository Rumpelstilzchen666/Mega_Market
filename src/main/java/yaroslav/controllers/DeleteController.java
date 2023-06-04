package yaroslav.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yaroslav.db.Item;
import yaroslav.db.ItemRepository;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/delete")
public class DeleteController {
    private final ItemRepository itemRepository;

    public DeleteController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public ResponseEntity<Long> deleteAll() {
        final long count = itemRepository.count();
        itemRepository.deleteAll();
        return ResponseEntity.ok(count);
    }

    @DeleteMapping(path = "/{id}", produces = {"application/json"})
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if(itemOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
        }
        deleteWithChildren(itemOptional.get());
        return ResponseEntity.ok().build();
    }

    private void deleteWithChildren(Item item) {
        if(item.isCategory()) {
            Iterable<Item> children = itemRepository.findAllByParentId(item.getId());
            for(Item child : children) {
                deleteWithChildren(child);
            }
        }
        itemRepository.delete(item);
    }
}
