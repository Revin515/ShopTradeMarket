package me.revin.shopTradeMarket.common.gui;

import me.revin.shopTradeMarket.shop.entity.Item;
import me.revin.shopTradeMarket.shop.entity.Shop;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.createInventory;

public class GUIManager {

    /**
     * 상점 GUI 생성
     * @use: ShopEvent.class
     */
    public static Inventory createInventoryGUI(String title, Shop shop, int page) {
        Inventory inventory = createInventory(null, 54, title);

        for (Item item : shop.getItemList().get(0)) {
            NamespacedKey key = NamespacedKey.minecraft(item.getItemId());
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
}
