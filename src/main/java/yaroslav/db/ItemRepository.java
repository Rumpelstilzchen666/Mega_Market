package yaroslav.db;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ItemRepository extends CrudRepository<Item, UUID> {
    Iterable<Item> findAllByParentId(UUID parentId);

    Iterable<Item> findAllByCategory(boolean category);
}
