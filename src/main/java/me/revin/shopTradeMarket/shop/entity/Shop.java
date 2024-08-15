package me.revin.shopTradeMarket.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Shop {

    private int shopId;
    private String shopName;
    private List<Category> categoryList;
}
