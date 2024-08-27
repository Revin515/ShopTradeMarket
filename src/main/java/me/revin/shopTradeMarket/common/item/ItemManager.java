package me.revin.shopTradeMarket.common.item;

import dev.lone.itemsadder.api.CustomStack;
import me.revin.shopTradeMarket.shop.entity.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private static ItemManager instance;

    private ItemManager() {}

    /**
     * config로 부터 가져온 Item 객체를 ItemStack으로 변환
     */
    public ItemStack convert(Item item) {
        ItemStack itemStack = null;

        if (CustomStack.isInRegistry(item.getItemId())) {
            CustomStack customStack = CustomStack.getInstance(item.getItemId());
            itemStack = customStack.getItemStack();
        } else {
            String[] itemIdList = item.getItemId().split(":");
            NamespacedKey key = new NamespacedKey(itemIdList[0], itemIdList[1]);
            Tag<Material> tag = Bukkit.getTag(Tag.REGISTRY_ITEMS, key, Material.class);
            Material material = tag.getValues().iterator().next();
            itemStack = new ItemStack(material);
        }

        itemStack.setAmount(item.getQuantity());
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<Component> loreList = itemMeta.lore();;
        Component lore1 = Component.text("판매가 " + item.getPrice());
        loreList.add(lore1);

        itemMeta.lore(loreList);

        return itemStack;
    }

    /**
     * ItemStack을 config에 저장하기 위해 Item 객체로 변경
     */
    public Item convert(ItemStack itemStack) {
        // 구현해야함

        return null;
    }

    /**
     * 바닐라 ItemStack 제작
     */
    public ItemStack create(String itemName, Material material, int hexCode) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        Component displayName = Component.text(itemName).color(TextColor.color(hexCode));
        itemMeta.displayName(displayName);

        return itemStack;
    }

    /**
     * 아이템에더 ItemStack 제작
     */
    public ItemStack create(String itemName, String namespaceId, int hexCode) {
        CustomStack customStack = CustomStack.getInstance(namespaceId);
        ItemStack itemStack = customStack.getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();

        Component displayName = Component.text(itemName).color(TextColor.color(hexCode));
        itemMeta.displayName(displayName);

        return itemStack;
    }

    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }

        return instance;
    }
}
