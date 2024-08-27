package me.revin.shopTradeMarket.shop;

import me.revin.shopTradeMarket.ShopTradeMarket;
import me.revin.shopTradeMarket.common.gui.GUIManager;
import me.revin.shopTradeMarket.common.npc.NPCManager;
import me.revin.shopTradeMarket.shop.command.CreateShopCommand;
import me.revin.shopTradeMarket.shop.command.DeleteShopCommand;
import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.event.PlayerContextManager;
import me.revin.shopTradeMarket.shop.event.CommonEvent;
import me.revin.shopTradeMarket.shop.event.EditEvent;
import org.bukkit.Bukkit;

public class ShopManager {

    private final ShopConfigManager configManager;
    private final PlayerContextManager contextManager;
    private final GUIManager guiManager;
    private final NPCManager npcManager;

    public ShopManager() {
        this.configManager = ShopConfigManager.getInstance();
        this.contextManager = PlayerContextManager.getInstance();
        this.guiManager = GUIManager.getInstance();
        this.npcManager = NPCManager.getInstance();
    }

    public void registerCommands() {
        ShopTradeMarket.getInstance().getCommand("상점생성").setExecutor(new CreateShopCommand(configManager, npcManager));
        ShopTradeMarket.getInstance().getCommand("상점삭제").setExecutor(new DeleteShopCommand(configManager, npcManager));
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new CommonEvent(configManager, contextManager, guiManager, npcManager), ShopTradeMarket.getInstance());
        Bukkit.getPluginManager().registerEvents(new EditEvent(configManager, contextManager, guiManager, npcManager), ShopTradeMarket.getInstance());
    }
}
