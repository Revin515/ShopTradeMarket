package me.revin.shopTradeMarket.shop.config;

import me.revin.shopTradeMarket.shop.entity.Item;
import me.revin.shopTradeMarket.shop.entity.Shop;
import me.revin.shopTradeMarket.shop.entity.ShopType;

import java.util.*;

public class ShopConfigSerializer {

    public static Map<String, Object> serialize(Shop shop) {
        Map<String, Object> shopData = new LinkedHashMap<>();

        List<List<Map<String, Object>>> itemPageDataList = new ArrayList<>();
        for (List<Item> pageList : shop.getItemList()) {

            List<Map<String, Object>> itemDataList = new ArrayList<>();
            for (Item item : pageList) {
                Map<String, Object> itemData = new LinkedHashMap<>();
                itemData.put("item-id", item.getItemId());
                itemData.put("quantity", item.getQuantity());
                itemData.put("price", item.getPrice());
                itemData.put("pos", item.getPos());
                itemDataList.add(itemData);
            }

            itemPageDataList.add(itemDataList);
        }

        shopData.put("shop-id", shop.getShopId());
        shopData.put("shop-name", shop.getShopName());
        shopData.put("shop-type", shop.getShopType());
        shopData.put("shop-owner", shop.getShopOwner());
        shopData.put("item-list", itemPageDataList);

        return shopData;
    }

    public static Shop deserialize(Map<String, Object> shopData) {
        int shopId = (Integer) shopData.get("shop-id");
        String shopName = (String) shopData.get("shop-name");
        ShopType shopType = ShopType.valueOf((String) shopData.get("shop-type"));
        UUID shopOwner = UUID.fromString((String) shopData.get("shop-owner"));
        List<List<Item>> itemList = new ArrayList<>();

        List<List<Map<String, Object>>> itemPageDataList = (List<List<Map<String, Object>>>) shopData.get("item-list");
        for (List<Map<String, Object>> itemDataList : itemPageDataList) {

            List<Item> items = new ArrayList<>();
            for (Map<String, Object> itemData : itemDataList) {
                String itemName = (String) itemData.get("item-id");
                Integer quantity = (Integer) itemData.get("quantity");
                Integer price = (Integer) itemData.get("price");
                Integer pos = (Integer) itemData.get("pos");

                Item item = new Item(itemName, quantity, price, pos);
                items.add(item);
            }

            itemList.add(items);
        }

        Shop shop = new Shop(shopId, shopName, shopType, shopOwner, itemList);

        return shop;
    }
}
