package me.revin.shopTradeMarket.shop;

import me.revin.shopTradeMarket.ShopTradeMarket;
import me.revin.shopTradeMarket.common.gui.GUIManager;
import me.revin.shopTradeMarket.shop.command.CreateShopCommand;
import me.revin.shopTradeMarket.shop.command.DeleteShopCommand;
import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.event.PlayerContextManager;
import me.revin.shopTradeMarket.shop.event.CloseEvent;
import me.revin.shopTradeMarket.shop.event.CommonEvent;
import me.revin.shopTradeMarket.shop.event.EdiEvent;
import org.bukkit.Bukkit;

public class ShopManager {

    private final ShopConfigManager configManager;
    private final PlayerContextManager contextManager;
    private final GUIManager guiManager;

    public ShopManager() {
        this.configManager = ShopConfigManager.getInstance();
        this.contextManager = PlayerContextManager.getInstance();
        this.guiManager = GUIManager.getInstance();
    }

    public void registerCommands() {
        ShopTradeMarket.getInstance().getCommand("상점생성").setExecutor(new CreateShopCommand(configManager));
        ShopTradeMarket.getInstance().getCommand("상점삭제").setExecutor(new DeleteShopCommand(configManager));
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new CommonEvent(configManager, contextManager, guiManager), ShopTradeMarket.getInstance());
        Bukkit.getPluginManager().registerEvents(new EdiEvent(configManager, contextManager, guiManager), ShopTradeMarket.getInstance());
        Bukkit.getPluginManager().registerEvents(new CloseEvent(contextManager), ShopTradeMarket.getInstance());
    }
}
