package me.revin.shopTradeMarket.common.gui;

import dev.lone.itemsadder.api.CustomStack;
import me.revin.shopTradeMarket.common.item.ItemManager;
import me.revin.shopTradeMarket.shop.entity.Item;
import me.revin.shopTradeMarket.shop.entity.Shop;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.createInventory;

public class GUIManager {

    private static GUIManager instance;
    private final ItemManager itemManager;

    private GUIManager() {
        this.itemManager = ItemManager.getInstance();
    }

    public Inventory createShopGUI(String title, Shop shop, int page) {
        Component titleComponent = Component.text(title);
        Inventory inventory = createInventory(null, 54, titleComponent);

        for (Item item : shop.getItemList().get(page)) {
            inventory.setItem(item.getPos(), itemManager.convert(item));
        }

        inventory.setItem(49, itemManager.create((page + 1) + " / " + shop.getItemList().size(), Material.PAPER, 0xFFFFFF));

        if (page > 0) {
            inventory.setItem(48, itemManager.create("이전 페이지", "_iainternal:icon_left_blue", 0xFFFFFF));
            inventory.setItem(53, itemManager.create("페이지 삭제", Material.BARRIER, 0xFFFFFF));
        } else if (page < shop.getItemList().size() - 1) {
            inventory.setItem(50, itemManager.create("다음 페이지", "_iainternal:icon_right_blue", 0xFFFFFF));
        } else if (page == shop.getItemList().size() - 1) {
            inventory.setItem(50, itemManager.create("새 페이지 추가", Material.FEATHER, 0xFFFFFF));
        }

        return inventory;
    }

    public Inventory createSetPriceGUI() {
        Component titleComponent = Component.text("가격 설정");
        Inventory inventory = createInventory(null, InventoryType.ANVIL, titleComponent);

        CustomStack customStack = CustomStack.getInstance("shoptrademarket:money");
        ItemStack itemStack = customStack.getItemStack();
        inventory.setItem(0, itemStack);

        return inventory;
    }

    public static GUIManager getInstance() {
        if (instance == null) {
            instance = new GUIManager();
        }

        return instance;
    }
}
