package me.revin.shopTradeMarket.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Category {

    private int categoryId;
    private String categoryName;
    private String iconMaterial;
    private List<Item> itemList;
}
