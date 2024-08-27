package me.revin.shopTradeMarket;

import me.revin.shopTradeMarket.common.npc.NPCManager;
import me.revin.shopTradeMarket.shop.ShopManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShopTradeMarket extends JavaPlugin {

    private static ShopTradeMarket instance;
    private ShopManager shopManager;
    private NPCManager npcManager;

    @Override
    public void onEnable() {
        instance = this;
        npcManager = NPCManager.getInstance();
        shopManager = new ShopManager();

        npcManager.registerTrait();
        shopManager.registerCommands();
        shopManager.registerEvents();
    }

    @Override
    public void onDisable() {
    }

    public static ShopTradeMarket getInstance() {
        return instance;
    }
}
