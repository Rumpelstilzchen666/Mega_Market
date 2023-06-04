package yaroslav.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yaroslav.controllers.schemas.ShopUnitStatisticResponse;
import yaroslav.db.Item;
import yaroslav.db.ItemRepository;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RestController
@RequestMapping("/sales")
public class SalesController {
    private final ItemRepository itemRepository;

    public SalesController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping(params = {"date"}, produces = {"application/json"})
    public ResponseEntity sales(@RequestParam(value = "date") String date) {
        final Date now;
        try {
            now = Item.ISO_8601_DATE_FORMAT.parse(date);
        } catch(ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid now format");
        }
        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY, -24);
        final Date now24 = calendar.getTime();

        final ShopUnitStatisticResponse response = new ShopUnitStatisticResponse();
        for(Item item : itemRepository.findAllByCategory(false)) {
            if(!item.getDate().before(now24) && !item.getDate().after(now)) {
                response.add(item);
            }
        }
        return ResponseEntity.ok(response);
    }
}
