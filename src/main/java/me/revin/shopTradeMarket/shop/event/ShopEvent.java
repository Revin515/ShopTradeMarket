package me.revin.shopTradeMarket.shop.event;

import lombok.RequiredArgsConstructor;
import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.entity.Shop;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.revin.shopTradeMarket.common.gui.GUIManager.*;
import static me.revin.shopTradeMarket.common.npc.NPCManager.*;

@RequiredArgsConstructor
public class ShopEvent implements Listener {

    private final ShopConfigManager configManager;
    private Map<UUID, Integer> playerNpcMap = new HashMap<>();

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

        playerNpcMap.put(player.getUniqueId(), npc.getId());
        player.openInventory(createInventoryGUI(shopName, shop, 0));
    }

    /**
     * GUI 닫을때 playerNpcMap에서 상태 제거
     * 플레이어가 직접 닫았을 때만 작동
     */
    @EventHandler
    public void onShopMenuClose(InventoryCloseEvent event) {
        if (event.getReason() != InventoryCloseEvent.Reason.PLAYER) return;

        Player player = (Player) event.getPlayer();
        UUID uuid = player.getUniqueId();

        playerNpcMap.remove(uuid);
    }
}
