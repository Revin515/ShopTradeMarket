package me.revin.shopTradeMarket.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Category {

    private String categoryName;
    private List<Item> itemList;
}
