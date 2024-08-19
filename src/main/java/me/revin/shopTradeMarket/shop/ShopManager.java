package me.revin.shopTradeMarket.shop;

import me.revin.shopTradeMarket.shop.command.CreateShopCommand;
import me.revin.shopTradeMarket.shop.command.DeleteShopCommand;
import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.event.ShopEvent;
import me.revin.shopTradeMarket.shop.event.ShopEditEvent;
import org.bukkit.Bukkit;

import static me.revin.shopTradeMarket.ShopTradeMarket.*;

public class ShopManager {

    private final ShopConfigManager configManager;

    public ShopManager() {
        this.configManager = ShopConfigManager.getInstance();
    }

    public void registerCommands() {
        getInstance().getCommand("상점생성").setExecutor(new CreateShopCommand(configManager));
        getInstance().getCommand("상점삭제").setExecutor(new DeleteShopCommand(configManager));
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new ShopEvent(configManager), getInstance());
        Bukkit.getPluginManager().registerEvents(new ShopEditEvent(configManager), getInstance());
    }
}
