package yaroslav.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yaroslav.controllers.schemas.ShopUnitImport;
import yaroslav.controllers.schemas.ShopUnitImportRequest;
import yaroslav.controllers.schemas.Type;
import yaroslav.db.Item;
import yaroslav.db.ItemRepository;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/imports")
public class ImportsController {
    private final ItemRepository itemRepository;

    public ImportsController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @PostMapping(produces = {"application/json"})
    public ResponseEntity<String> imports(@RequestBody ShopUnitImportRequest shopUnitImportRequest) {
        if(shopUnitImportRequest.items() == null || shopUnitImportRequest.updateDate() == null) {
            return get400("Validation Failed");
        }

        Date updateDate;
        // Перебирал другие способы конвертации дат
        //OffsetDateTime updateDate = OffsetDateTime.parse(shopUnitImportRequest.updateDate());  catch(DateTimeParseException e)
        //DateTimeFormatter.ISO_INSTANT.parse(shopUnitImportRequest.updateDate());  catch(DateTimeParseException e)
        //Calendar updateDate = javax.xml.bind.DatatypeConverter.parseDateTime(shopUnitImportRequest.updateDate());
        try {
            updateDate = Item.ISO_8601_DATE_FORMAT.parse(shopUnitImportRequest.updateDate());
        } catch(ParseException e) {
            return get400("Invalid updateDate format");
        }

        for(ShopUnitImport item : shopUnitImportRequest.items()) {
            if(item.id() == null || item.name() == null || item.type() == null ||
                    (item.type() == Type.OFFER && (item.price() == null || item.price() < 0))) {
                return get400("Validation Failed");
            }
            if(item.type() == Type.CATEGORY && item.price() != null) {
                return get400("Category price must be null");
            }
            if(item.parentId() != null) {
                Optional<Item> parentOptional = itemRepository.findById(item.parentId());
                if(parentOptional.isEmpty()) {
                    return get400("Parent not found");
                }
                if(!parentOptional.get().isCategory()) {
                    return get400("Item parent must be category");
                }
                // Обновляю время всех родительских категорий
                Item parent;
                do {
                    parent = parentOptional.get();
                    // Если время категории обновлено, то и время всех её надкатегорий тоже обновлено
                    if(parent.getDate().before(updateDate)) {
                        parent.setDate(updateDate);
                        itemRepository.save(parent);
                    } else {
                        break;
                    }
                    if(parent.getParentId() == null) {
                        break;
                    }
                    parentOptional = itemRepository.findById(parent.getParentId());
                    if(parentOptional.isEmpty()) {
                        throw new IllegalStateException("Inconsistent data: item's parent not found");
                    }
                } while(true);
            }
            itemRepository.save(new Item(item, updateDate));
        }
        return ResponseEntity.ok().build();
    }

    private static ResponseEntity<String> get400(String description) {
        // System.out.println("400: " + description);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(description);
    }
}
