package me.revin.shopTradeMarket.shop.event;

import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.entity.Category;
import me.revin.shopTradeMarket.shop.entity.Shop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;

import static me.revin.shopTradeMarket.common.gui.GUIManager.MEDIUM;
import static me.revin.shopTradeMarket.common.gui.GUIManager.createInventoryGUI;

public class ShopCategoryEditEvent implements Listener {

    private ShopConfigManager configManager;
    private String categoryName;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String shopName = event.getView().getTitle().split(" ")[0];

        if (event.getSlotType() != InventoryType.SlotType.CONTAINER) return;
        if (event.getCurrentItem() == null) return;

        configManager = new ShopConfigManager();
        String btnName = event.getCurrentItem().getItemMeta().getDisplayName();

        event.setCancelled(true);
        Shop shop = configManager.loadShopData(shopName);
        List<Category> categoryList = shop.getCategoryList();
        player.openInventory(createInventoryGUI(MEDIUM, shopName, categoryList, true));
    }
}
