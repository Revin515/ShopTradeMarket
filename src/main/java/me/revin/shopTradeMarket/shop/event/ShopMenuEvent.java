package me.revin.shopTradeMarket.shop.event;

import lombok.Getter;
import lombok.Setter;
import me.revin.shopTradeMarket.common.npc.NPCManager;
import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.config.ShopType;
import me.revin.shopTradeMarket.shop.entity.Category;
import me.revin.shopTradeMarket.shop.entity.Item;
import me.revin.shopTradeMarket.shop.entity.Shop;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.*;

import static me.revin.shopTradeMarket.common.gui.GUIManager.*;
import static me.revin.shopTradeMarket.common.npc.NPCManager.verifyNPCbyTrait;
import static net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.*;

public class ShopMenuEvent implements Listener {

    /**
     * 플레이어 상태 저장 및 상태 공유 내부 클래스
     */
    @Getter
    @Setter
    private class PlayerContext {

        private int npcId;
        private String categoryName;
    }

    private ShopConfigManager configManager;
    private Map<UUID, PlayerContext> playerNpcMap = new HashMap<>();

    /**
     * NPC 쉬프트 + 우클릭시 상점관리 메뉴 표시
     * 만약 플레이어가 OP가 아닐 경우 해당 NPC 상점이 자신의 것이 맞는지 체크, 아니면 GUI 표시 안함
     */
    @EventHandler
    public void onNPCSneakRightClick(NPCRightClickEvent event) {
        if (!event.getClicker().isSneaking()) return;
        if (!verifyNPCbyTrait(event.getNPC(), "shop"))  return;

        configManager = new ShopConfigManager();
        Player player = event.getClicker();
        UUID uuid = player.getUniqueId();
        NPC npc = event.getNPC();

        if (!player.isOp()) {
            Shop shop = configManager.loadShopData(npc.getId());
            UUID shopOwner = shop.getShopOwner();

            if (!shopOwner.equals(uuid)) return;
        }

        PlayerContext playerContext = new PlayerContext();
        playerContext.setNpcId(npc.getId());
        playerNpcMap.put(uuid, playerContext);

        player.openInventory(createInventoryGUI(MEDIUM, npc.getName()));
    }

    /**
     * 버튼 클릭시 각 메뉴 표시
     */
    @EventHandler
    public void onMenuButtonClick(InventoryClickEvent event) {
        if (verifyInventoryEventException(event, "관리 메뉴")) return;

        configManager = new ShopConfigManager();
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        String shopName = event.getView().getTitle().split("-")[0];
        PlayerContext playerContext = playerNpcMap.get(uuid);

        Component component = event.getCurrentItem().getItemMeta().displayName();
        String btnName = plainText().serialize(component);
        int npcId = playerContext.getNpcId();

        if (btnName.equals("카테고리 편집")) {
            List<Category> categoryList = configManager.loadShopData(npcId).getCategoryList();
            player.openInventory(createInventoryGUI(MEDIUM, shopName, categoryList, false));
        } else if (btnName.equals("아이템 편집")) {
            Shop shop = configManager.loadShopData(npcId);
            player.openInventory(createInventoryGUI(LARGE, shopName, shop));
        } else if (btnName.equals("상점 삭제")) {
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
    public void onCategoryIconClick(InventoryClickEvent event) {
        if (verifyInventoryEventException(event, "카테고리 편집")) return;

        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        String shopName = event.getView().getTitle().split("-")[0];
        PlayerContext playerContext = playerNpcMap.get(uuid);

        Component component = event.getCurrentItem().getItemMeta().displayName();
        String btnName = plainText().serialize(component);
        int npcId = playerContext.getNpcId();

        if (!btnName.equals("새 카테고리 생성") && !btnName.equals("뒤로가기")) {
            configManager = new ShopConfigManager();
            List<Category> categoryList = configManager.loadShopData(npcId).getCategoryList();

            playerContext.setCategoryName(btnName);
            playerNpcMap.put(uuid, playerContext);

            player.openInventory(createInventoryGUI(MEDIUM, shopName, categoryList, true));
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onCategoryButtonClick(InventoryClickEvent event) {
        if (verifyInventoryEventException(event, "카테고리 편집")) return;

        configManager = new ShopConfigManager();
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        String shopName = event.getView().getTitle().split("-")[0];
        PlayerContext playerContext = playerNpcMap.get(uuid);

        Component component = event.getCurrentItem().getItemMeta().displayName();
        String btnName = plainText().serialize(component);
        int npcId = playerContext.getNpcId();

        if (btnName.equals("뒤로가기")) {
            player.openInventory(createInventoryGUI(MEDIUM, shopName));
        } else if(btnName.equals("새 카테고리 생성")) {
            Shop shop = configManager.loadShopData(npcId);
            List<Category> categoryList = shop.getCategoryList();
            int newCategoryId = shop.getLastCategoryId() + 1;
            List<Item> itemList = new ArrayList<>();

            Category newCategory = new Category(newCategoryId, "새 카테고리", "CHEST", itemList);
            categoryList.add(newCategory);
            configManager.saveShopData(shop);

            player.openInventory(createInventoryGUI(MEDIUM, shopName, categoryList, false));
        } else if (btnName.equals("아이콘 변경")) {
            // 27칸 gui 출력
            // 가운데 원래 아이콘 생성
            // 아이템창에서 아이콘 드래그&드랍 하면 아이콘 변경
            // 확인 클릭시 저장
            // 취소 클릭시 원래창으로 이동
        } else if (btnName.equals("이름 변경")) {
            // 모루 gui 출력
            // 모루 아이템에 원래 아이콘 생성
            // 이름 변경하고 오른쪽 변경된 아이콘 클릭하면 이름 변경
        } else if (btnName.equals("제거")) {
            // 27칸 gui 출력
            // 가운데 원래 아이콘 생성
            // 확인 클릭시 삭제
            // 취소 클릭시 원래창으로 이동
            // 만약 카테고리가 한개만 있다면 삭제불가
        } else if (btnName.equals("왼쪽으로 이동")) {
            // 카테고리 List 순서를 앞쪽으로 변경
            // 만약 제일 첫번째 순서라면 이동불가
        } else if (btnName.equals("오른쪽으로 이동")) {
            // 카테고리 List 순서를 뒤쪽으로 변경
            // 만약 제일 마지막 순서라면 이동불가
        }
    }

    /**
     * GUI 닫을때 playerNpcMap에서 상태 제거
     * 플레이어가 직접 닫았을 때만 작동
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getReason() != InventoryCloseEvent.Reason.PLAYER) return;

        Player player = (Player) event.getPlayer();
        UUID uuid = player.getUniqueId();

        playerNpcMap.remove(uuid);
    }

    /**
     * 이벤트 핸들러에서 공통으로 사용되는 예외처리
     * 조건문에 일치하면 true 반환 -> 예외 있음, 일치하지 않으면 false 반환 -> 예외 없음
     */
    private boolean verifyInventoryEventException(InventoryClickEvent event, String menuName) {
        if (event.getSlotType() != InventoryType.SlotType.CONTAINER) return true;
        else if (event.getCurrentItem() == null) return true;
        else if (event.getView().getTitle().split("-").length != 2) return true;
        else if (!event.getView().getTitle().split("-")[1].equals(menuName)) return true;
        else return false;
    }
}
