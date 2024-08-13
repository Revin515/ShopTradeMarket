package me.revin.shopTradeMarket.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Item {

    private String itemId;
    private int price;
    private int pos;
}
