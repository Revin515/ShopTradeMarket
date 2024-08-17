package me.revin.shopTradeMarket.shop.config;

import me.revin.shopTradeMarket.shop.entity.Category;
import me.revin.shopTradeMarket.shop.entity.Item;
import me.revin.shopTradeMarket.shop.entity.Shop;

import java.util.*;

public class ShopConfigSerializer {

    public static Map<String, Object> serialize(Shop shop) {
        Map<String, Object> shopData = new LinkedHashMap<>();

        List<Map<String, Object>> categoryDataList = new ArrayList<>();
        for (Category category : shop.getCategoryList()) {
            Map<String, Object> categoryData = new LinkedHashMap<>();

            List<Map<String, Object>> itemDataList = new ArrayList<>();
            for (Item item : category.getItemList()) {
                Map<String, Object> itemData = new LinkedHashMap<>();
                itemData.put("item-id", item.getItemId());
                itemData.put("price", item.getPrice());
                itemData.put("pos", item.getPos());
                itemDataList.add(itemData);
            }

            categoryData.put("category-id", category.getCategoryId());
            categoryData.put("category-name", category.getCategoryName());
            categoryData.put("icon-material", category.getIconMaterial());
            categoryData.put("item-list", itemDataList);
            categoryDataList.add(categoryData);
        }

        shopData.put("shop-id", shop.getShopId());
        shopData.put("shop-name", shop.getShopName());
        shopData.put("shop-type", shop.getShopType());
        shopData.put("shop-owner", shop.getShopOwner());
        shopData.put("last-category-id", shop.getLastCategoryId());
        shopData.put("category-list", categoryDataList);

        return shopData;
    }

    public static Shop deserialize(Map<String, Object> shopData) {
        int shopId = (Integer) shopData.get("shop-id");
        String shopName = (String) shopData.get("shop-name");
        ShopType shopType = ShopType.valueOf((String) shopData.get("shop-type"));
        UUID shopOwner = UUID.fromString((String) shopData.get("shop-owner"));
        int lastCategoryId = (Integer) shopData.get("last-category-id");
        List<Category> categoryList = new ArrayList<>();

        List<Map<String, Object>> categoryDataList = (List<Map<String, Object>>) shopData.get("category-list");
        for (Map<String, Object> categoryData : categoryDataList) {
            int categoryId = (Integer) categoryData.get("category-id");
            String categoryName = (String) categoryData.get("category-name");
            String iconMaterial = (String) categoryData.get("icon-material");
            List<Item> itemList = new ArrayList<>();

            List<Map<String, Object>> itemDataList = (List<Map<String, Object>>) categoryData.get("item-list");
            for (Map<String, Object> itemData : itemDataList) {
                String itemName = (String) itemData.get("item-name");
                Integer price = (Integer) itemData.get("price");
                Integer pos = (Integer) itemData.get("pos");

                Item item = new Item(itemName, price, pos);
                itemList.add(item);
            }

            Category category = new Category(categoryId, categoryName, iconMaterial, itemList);
            categoryList.add(category);
        }

        Shop shop = new Shop(shopId, shopName, shopType, shopOwner, lastCategoryId, categoryList);

        return shop;
    }
}
