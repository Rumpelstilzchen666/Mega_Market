package yaroslav.controllers.schemas;

import java.util.UUID;

public record ShopUnitImport(UUID id, String name, UUID parentId, Type type, Long price) { }
