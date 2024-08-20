package me.revin.shopTradeMarket.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Shop {

    private int shopId;
    private String shopName;
    private ShopType shopType;
    private UUID shopOwner;
    private List<List<Item>> itemList;
}
