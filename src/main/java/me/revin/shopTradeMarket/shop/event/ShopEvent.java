package me.revin.shopTradeMarket.shop.event;

import lombok.Getter;
import lombok.Setter;
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

public class ShopEvent implements Listener {

    private ShopConfigManager configManager;
    private Map<UUID, PlayerContext> playerNpcMap = new HashMap<>();

    /**
     * NPC 우클릭하여 상점 GUI 표시
     */
    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        NPC npc = event.getNPC();

        if (player.isSneaking()) return;
        if (!verifyNPCbyTrait(npc, "shop")) return;

        PlayerContext playerContext = new PlayerContext();
        UUID uuid = player.getUniqueId();
        playerContext.setNpcId(npc.getId());

        configManager = new ShopConfigManager();
        Shop shop = configManager.loadShopData(npc.getId());
        String shopName = npc.getName();

        playerNpcMap.put(uuid, playerContext);
        player.openInventory(createInventoryGUI(LARGE, shopName, shop));
    }

    /**
     * GUI 닫을때 playerNpcMap에서 상태 제거
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        UUID uuid = player.getUniqueId();

        playerNpcMap.remove(uuid);
    }

    /**
     * 플레이어 상태 유지 및 메소드간 상태 공유용 내부 클래스
     */
    @Getter
    @Setter
    private class PlayerContext {

        private int npcId;
        private String categoryName;
    }
}
