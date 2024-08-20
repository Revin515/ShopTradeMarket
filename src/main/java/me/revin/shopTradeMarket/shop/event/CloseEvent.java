package me.revin.shopTradeMarket.shop.event;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;

@RequiredArgsConstructor
public class CloseEvent implements Listener {

    private final PlayerContextManager contextManager;

    /**
     * GUI 닫을때 playerNpcMap에서 상태 제거
     * 플레이어가 직접 닫았을 때만 작동
     */
    @EventHandler
    public void onShopMenuClose(InventoryCloseEvent event) {
        if (event.getReason() != InventoryCloseEvent.Reason.PLAYER) return;

        Player player = (Player) event.getPlayer();
        UUID uuid = player.getUniqueId();

        contextManager.getPlayerContextMap().remove(uuid);
    }
}
