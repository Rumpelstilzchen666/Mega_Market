package yaroslav.controllers.schemas;

import java.util.ArrayList;

public record ShopUnitImportRequest(ArrayList<ShopUnitImport> items, String updateDate) { }
