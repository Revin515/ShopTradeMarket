package me.revin.shopTradeMarket.common.gui;

import dev.lone.itemsadder.api.CustomStack;
import me.revin.shopTradeMarket.shop.entity.Item;
import me.revin.shopTradeMarket.shop.entity.Shop;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.createInventory;

public class GUIManager {

    private static GUIManager instance;

    private GUIManager() {}

    public Inventory createShopGUI(String title, Shop shop, int page) {
        Inventory inventory = createInventory(null, 54, title);

        for (Item item : shop.getItemList().get(page)) {
            String[] itemIdList = item.getItemId().split(":");
            NamespacedKey key = new NamespacedKey(itemIdList[0], itemIdList[1]);
            Tag<Material> tag = Bukkit.getTag(Tag.REGISTRY_ITEMS, key, Material.class);
            Material material = tag.getValues().iterator().next();
            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();

            List<Component> loreList = new ArrayList<>();
            Component lore1 = Component.text("판매가 " + item.getPrice());
            loreList.add(lore1);

            itemMeta.lore(loreList);

            inventory.setItem(item.getPos(), itemStack);
        }

        return inventory;
    }

    public Inventory createSetPriceGUI() {
        Inventory inventory = createInventory(null, InventoryType.ANVIL, "가격 설정");

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
