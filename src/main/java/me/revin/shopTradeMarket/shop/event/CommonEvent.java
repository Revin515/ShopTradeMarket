package me.revin.shopTradeMarket.shop.event;

import lombok.RequiredArgsConstructor;
import me.revin.shopTradeMarket.common.gui.GUIManager;
import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.entity.Shop;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import static me.revin.shopTradeMarket.common.npc.NPCManager.*;

@RequiredArgsConstructor
public class CommonEvent implements Listener {

    private final ShopConfigManager configManager;
    private final PlayerContextManager contextManager;
    private final GUIManager guiManager;

    /**
     * NPC 우클릭하여 상점 GUI 표시
     */
    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        NPC npc = event.getNPC();

        if (player.isSneaking()) return;
        if (!verifyNPCbyTrait(npc, "shop")) return;

        Shop shop = configManager.loadShopData(npc.getId());
        String shopName = npc.getName();

        contextManager.getPlayerContextMap().put(player.getUniqueId(), npc.getId());
        Inventory inventoryGUI = guiManager.createShopGUI(shopName, shop, 0);
        player.openInventory(inventoryGUI);
    }
}
