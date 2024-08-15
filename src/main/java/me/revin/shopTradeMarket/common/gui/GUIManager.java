package me.revin.shopTradeMarket.common.gui;

import me.revin.shopTradeMarket.shop.entity.Category;
import me.revin.shopTradeMarket.shop.entity.Shop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;
import java.util.List;

import static org.bukkit.Bukkit.createInventory;

public class GUIManager {

    public static final int SMALL = 9;
    public static final int MEDIUM = 27;
    public static final int LARGE = 54;

    /**
     * 상점 생성
     * use: 상점, 상점관리 메뉴
     */
    public static Inventory createInventoryGUI(int size, String title, Shop shop) {
        List<Category> categoryList = shop.getCategoryList();
        Inventory inventory = createInventory(null, size, title);

        for (int i = 0; i < categoryList.size(); i++) {
            Category category = categoryList.get(i);
            ItemStack categoryBtn = new ItemStack(Material.CHEST);
            ItemMeta itemMeta = categoryBtn.getItemMeta();

            Component itemName = Component.text(category.getCategoryName())
                    .color(TextColor.color(0xFFFFFF))
                    .decorate(TextDecoration.BOLD);

            itemMeta.displayName(itemName);
            categoryBtn.setItemMeta(itemMeta);

            inventory.setItem(i, categoryBtn);
        }

        return inventory;
    }

    /**
     * 카테고리 목록 생성
     * use: 상점관리 메뉴
     */
    public static Inventory createInventoryGUI(int size, String title, List<Category> categoryList, boolean selected) {
        Iterator<Category> categoryIterator = categoryList.iterator();
        Inventory inventory = createInventory(null, size, title + " 카테고리 편집");

        for (int i = 9; i < 18; i++) {
            if (categoryIterator.hasNext()) {
                Category category = categoryIterator.next();

                ItemStack categoryBtn = new ItemStack(Material.CHEST);
                ItemMeta itemMeta = categoryBtn.getItemMeta();

                Component itemName = Component.text(category.getCategoryName())
                        .color(TextColor.color(0xFFFFFF))
                        .decorate(TextDecoration.BOLD);

                itemMeta.displayName(itemName);
                categoryBtn.setItemMeta(itemMeta);

                inventory.setItem(i, categoryBtn);
            }
        }

        if (selected) {

        }

        return inventory;
    }

    /**
     * 상점관리 메뉴 생성
     * use: 상점관리 메뉴
     */
    public static Inventory createInventoryGUI(int size, String title) {
        String[] displayName = {"카테고리 편집", "아이템 편집", "상점삭제"};
        int[] pos = {10, 13, 16};

        Inventory inventory = createInventory(null, size, title + " 메뉴");

        for (int i = 0; i < displayName.length; i++) {
            ItemStack item = new ItemStack(Material.CHEST);
            ItemMeta itemMeta = item.getItemMeta();

            Component itemName = Component.text(displayName[i])
                    .color(TextColor.color(0xFFFFFF))
                    .decorate(TextDecoration.BOLD);

            itemMeta.displayName(itemName);
            item.setItemMeta(itemMeta);

            inventory.setItem(pos[i], item);
        }

        return inventory;
    }
}
