package me.revin.shopTradeMarket.common.gui;

import dev.lone.itemsadder.api.CustomStack;
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
     * @use: ShopEvent.class
     */
    public static Inventory createInventoryGUI(int size, String title, Shop shop) {
        Inventory inventory = createInventory(null, size, title);
        List<Category> categoryList = shop.getCategoryList();

        for (int i = 0; i < categoryList.size(); i++) {
            Category category = categoryList.get(i);
            ItemStack categoryBtn = new ItemStack(Material.CHEST);
            ItemMeta categoryMeta = categoryBtn.getItemMeta();

            Component itemName = Component.text(category.getCategoryName())
                    .color(TextColor.color(0xFFFFFF))
                    .decorate(TextDecoration.BOLD);

            categoryMeta.displayName(itemName);
            categoryBtn.setItemMeta(categoryMeta);

            inventory.setItem(i, categoryBtn);
        }

        return inventory;
    }

    /**
     * 상점관리 메뉴 생성
     * @use: ShopMenuEvent.class
     */
    public static Inventory createInventoryGUI(int size, String title) {
        Inventory inventory = createInventory(null, size, title + "-관리 메뉴");
        String[] displayName = {"카테고리 편집", "아이템 편집", "상점삭제"};
        int[] pos = {10, 13, 16};

        for (int i = 0; i < displayName.length; i++) {
            ItemStack menuBtn = new ItemStack(Material.CHEST);
            ItemMeta itemMeta = menuBtn.getItemMeta();

            Component itemName = Component.text(displayName[i])
                    .color(TextColor.color(0xFFFFFF))
                    .decorate(TextDecoration.BOLD);

            itemMeta.displayName(itemName);
            menuBtn.setItemMeta(itemMeta);

            inventory.setItem(pos[i], menuBtn);
        }

        return inventory;
    }

    /**
     * 카테고리 목록 생성,
     * 카테고리 편집 메뉴에서 특정 카테고리를 클릭했을 때 아래에 추가 버튼 출력
     * @use: ShopMenuEvent.class
     */
    public static Inventory createInventoryGUI(int size, String title, List<Category> categoryList, boolean selected) {
        Inventory inventory = createInventory(null, size, title + "-카테고리 편집");
        Iterator<Category> categoryIterator = categoryList.iterator();

        CustomStack backOrange = CustomStack.getInstance("icon_back_orange");
        ItemStack backBtn = backOrange.getItemStack();
        ItemMeta backMeta = backBtn.getItemMeta();

        Component backComponent = Component.text("뒤로가기")
            .color(TextColor.color(0xFFFFFF))
            .decorate(TextDecoration.BOLD);

        backMeta.displayName(backComponent);
        backBtn.setItemMeta(backMeta);

        inventory.setItem(18, backBtn);

        for (int i = 9; i < 18; i++) {
            if (categoryIterator.hasNext()) {
                Category category = categoryIterator.next();
                Material material = Material.getMaterial(category.getIconMaterial());

                ItemStack categoryBtn = new ItemStack(material);
                ItemMeta categoryMeta = categoryBtn.getItemMeta();

                Component itemName = Component.text(category.getCategoryName())
                        .color(TextColor.color(0xFFFFFF))
                        .decorate(TextDecoration.BOLD);

                categoryMeta.displayName(itemName);
                categoryBtn.setItemMeta(categoryMeta);

                inventory.setItem(i, categoryBtn);
            } else if(i < 17 && !categoryIterator.hasNext()) {
                ItemStack categoryAddBtn = new ItemStack(Material.FEATHER);
                ItemMeta categoryAddMeta = categoryAddBtn.getItemMeta();

                Component itemName = Component.text("새 카테고리 생성")
                        .color(TextColor.color(0xFCFC00));

                categoryAddMeta.displayName(itemName);
                categoryAddBtn.setItemMeta(categoryAddMeta);

                inventory.setItem(i, categoryAddBtn);
                break;
            }
        }

        if (selected) {
            CustomStack left = CustomStack.getInstance("icon_left_blue");
            CustomStack right = CustomStack.getInstance("icon_right_blue");

            Object[] icon = {left, Material.PAINTING, Material.OAK_SIGN, Material.BARRIER, right};
            String[] name = {"왼쪽으로 이동", "아이콘 변경", "이름 변경", "제거", "오른쪽으로 이동"};

            ItemStack itemStack = null;

            for (int i = 0; i < icon.length; i++) {
                int pos = i + 20;

                if (icon[i] instanceof CustomStack) {
                    CustomStack customStack = (CustomStack) icon[i];
                    itemStack = customStack.getItemStack();
                } else {
                    Material material = (Material) icon[i];
                    itemStack = new ItemStack(material);
                }

                ItemMeta itemMeta = itemStack.getItemMeta();

                Component itemName = Component.text(name[i])
                    .color(TextColor.color(0xFCFC00));

                itemMeta.displayName(itemName);
                itemStack.setItemMeta(itemMeta);

                inventory.setItem(pos, itemStack);
            }
        }

        return inventory;
    }
}
