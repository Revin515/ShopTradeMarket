package me.revin.shopTradeMarket.shop.event;

import lombok.Getter;
import lombok.Setter;
import me.revin.shopTradeMarket.shop.entity.Item;
import me.revin.shopTradeMarket.shop.entity.Shop;

@Getter
@Setter
public class PlayerContext {

    private int npcId;
    private Shop shop;
    private int page;
    private Item item;
}
