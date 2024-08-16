package me.revin.shopTradeMarket.shop.event;

import lombok.Getter;
import lombok.Setter;
import me.revin.shopTradeMarket.common.npc.NPCManager;
import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.entity.Category;
import me.revin.shopTradeMarket.shop.entity.Shop;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static me.revin.shopTradeMarket.common.gui.GUIManager.*;
import static me.revin.shopTradeMarket.common.npc.NPCManager.verifyNPCbyTrait;
import static net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.*;

public class ShopMenuEvent implements Listener {

    private ShopConfigManager configManager;
    private Map<UUID, PlayerContext> playerNpcMap = new HashMap<>();

    /**
     * NPC 쉬프트 + 우클릭시 상점관리 메뉴 표시
     */
    @EventHandler
    public void onSneakRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        NPC npc = event.getNPC();

        if (!player.isSneaking()) return;
        if (!verifyNPCbyTrait(npc, "shop"))  return;

        PlayerContext playerContext = new PlayerContext();
        UUID uuid = player.getUniqueId();
        playerContext.setNpcId(npc.getId());

        playerNpcMap.put(uuid, playerContext);
        player.openInventory(createInventoryGUI(MEDIUM, npc.getName()));
    }

    /**
     * 버튼 클릭시 각 메뉴 표시
     */
    @EventHandler
    public void onMenuInventoryClick(InventoryClickEvent event) {
        if (event.getSlotType() != InventoryType.SlotType.CONTAINER) return;
        if (event.getCurrentItem() == null) return;

        String[] titleArr = event.getView().getTitle().split("-");
        if (titleArr.length != 2) return;

        String shopName = titleArr[0];
        String menuName = titleArr[1];

        if (!menuName.equals("관리 메뉴")) return;

        configManager = new ShopConfigManager();

        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        PlayerContext playerContext = playerNpcMap.get(uuid);

        Component component = event.getCurrentItem().getItemMeta().displayName();
        String btnName = plainText().serialize(component);
        int npcId = playerContext.getNpcId();

        if (btnName.equals("카테고리 편집")) {
            Shop shop = configManager.loadShopData(npcId);
            List<Category> categoryList = shop.getCategoryList();
            player.openInventory(createInventoryGUI(MEDIUM, shopName, categoryList, false));
        }
        else if (btnName.equals("아이템 편집")) {
            Shop shop = configManager.loadShopData(npcId);
            player.openInventory(createInventoryGUI(LARGE, shopName, shop));
        }
        else if (btnName.equals("상점 삭제")) {
            configManager.deleteShopData(shopName);
            NPCManager.deleteNPC(shopName);
            player.sendMessage("상점 " + shopName + " 이(가) 삭제되었습니다.");
        }

        event.setCancelled(true);
    }

    /**
     * 카테고리 편집 메뉴에서 특정 카테고리를 클릭했을 때 아래에 추가 버튼 출력
     */
    @EventHandler
    public void onCategoryInventoryClick(InventoryClickEvent event) {
        if (event.getSlotType() != InventoryType.SlotType.CONTAINER) return;
        if (event.getCurrentItem() == null) return;

        String[] titleArr = event.getView().getTitle().split("-");
        if (titleArr.length < 2) return;

        String shopName = titleArr[0];
        String menuName = titleArr[1];

        if (!menuName.equals("카테고리 편집")) return;

        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        PlayerContext playerContext = playerNpcMap.get(uuid);

        Component component = event.getCurrentItem().getItemMeta().displayName();
        String btnName = plainText().serialize(component);
        int npcId = playerContext.getNpcId();

        if (!btnName.equals("새 카테고리 생성")) {
            configManager = new ShopConfigManager();

            playerContext.setCategoryName(btnName);
            playerNpcMap.put(uuid, playerContext);

            Shop shop = configManager.loadShopData(npcId);
            List<Category> categoryList = shop.getCategoryList();
            player.openInventory(createInventoryGUI(MEDIUM, shopName, categoryList, true));
        }

        event.setCancelled(true);
    }

    /**
     * GUI 닫을때 playerNpcMap에서 상태 제거
     */
//    @EventHandler
//    public void onInventoryClose(InventoryCloseEvent event) {
//        Player player = (Player) event.getPlayer();
//        UUID uuid = player.getUniqueId();
//
//        playerNpcMap.remove(uuid);
//    }

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
