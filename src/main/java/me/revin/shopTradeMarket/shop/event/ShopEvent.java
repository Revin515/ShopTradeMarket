package me.revin.shopTradeMarket.shop.event;

import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.entity.Shop;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.revin.shopTradeMarket.common.gui.GUIManager.*;
import static me.revin.shopTradeMarket.common.npc.NPCManager.*;

public class ShopEvent implements Listener {

    private ShopConfigManager configManager;

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        NPC npc = event.getNPC();

        if (player.isSneaking()) return;
        if (!verifyNPCbyTrait(npc, "shop")) return;

        configManager = new ShopConfigManager();
        Shop shop = configManager.loadShopData(npc.getName());
        String shopName = npc.getName();

        player.openInventory(createInventoryGUI(LARGE, shopName, shop));
    }
}
