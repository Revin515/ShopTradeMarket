package me.revin.shopTradeMarket.shop.event;

import lombok.RequiredArgsConstructor;
import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.entity.Shop;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

import java.util.*;

import static me.revin.shopTradeMarket.common.gui.GUIManager.*;
import static me.revin.shopTradeMarket.common.npc.NPCManager.verifyNPCbyTrait;

@RequiredArgsConstructor
public class ShopEditEvent implements Listener {

    private final ShopConfigManager configManager;
    private Map<UUID, Integer> playerNpcMap = new HashMap<>();

    /**
     * NPC 쉬프트 + 우클릭시 상점관리 메뉴 표시
     * 만약 플레이어가 OP가 아닐 경우 해당 NPC 상점이 자신의 것이 맞는지 체크, 아니면 GUI 표시 안함
     */
    @EventHandler
    public void onShopMenuOpen(NPCRightClickEvent event) {
        if (!event.getClicker().isSneaking()) return;
        if (!verifyNPCbyTrait(event.getNPC(), "shop"))  return;

        Player player = event.getClicker();
        UUID uuid = player.getUniqueId();
        NPC npc = event.getNPC();

        Shop shop = configManager.loadShopData(npc.getId());
        UUID shopOwner = shop.getShopOwner();

        if (!player.isOp()) {
            if (!shopOwner.equals(uuid)) return;
        }

        playerNpcMap.put(uuid, npc.getId());
        player.openInventory(createInventoryGUI(npc.getName(), shop, 0));
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
