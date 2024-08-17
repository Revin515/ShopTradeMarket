package me.revin.shopTradeMarket.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.revin.shopTradeMarket.shop.config.ShopType;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Shop {

    private int shopId;
    private String shopName;
    private ShopType shopType;
    private UUID shopOwner;
    private int lastCategoryId;
    private List<Category> categoryList;
}
