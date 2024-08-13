package me.revin.shopTradeMarket.shop;

import me.revin.shopTradeMarket.shop.command.CreateShopCommand;
import me.revin.shopTradeMarket.shop.command.DeleteShopCommand;
import me.revin.shopTradeMarket.shop.event.ShopOpenEvent;
import org.bukkit.Bukkit;

import static me.revin.shopTradeMarket.ShopTradeMarket.*;

public class ShopManager {

    public void registerCommands() {
        getInstance().getCommand("상점생성").setExecutor(new CreateShopCommand());
        getInstance().getCommand("상점삭제").setExecutor(new DeleteShopCommand());
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new ShopOpenEvent(), getInstance());
    }
}
