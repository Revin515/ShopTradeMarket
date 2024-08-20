package me.revin.shopTradeMarket.shop.event;

import lombok.Getter;
import lombok.Setter;
import me.revin.shopTradeMarket.shop.entity.Item;
import me.revin.shopTradeMarket.shop.entity.Shop;
import org.bukkit.inventory.Inventory;

@Getter
@Setter
public class PlayerContext {

    private int npcId;
    private int page;
    private Shop shop;
    private Item item;
    private Inventory inventory;
}
