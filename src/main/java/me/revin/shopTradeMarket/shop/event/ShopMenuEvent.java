package me.revin.shopTradeMarket.shop.event;

import me.revin.shopTradeMarket.common.npc.NPCManager;
import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.entity.Category;
import me.revin.shopTradeMarket.shop.entity.Shop;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;

import static me.revin.shopTradeMarket.common.gui.GUIManager.*;
import static me.revin.shopTradeMarket.common.npc.NPCManager.verifyNPCbyTrait;

public class ShopMenuEvent implements Listener {

    private ShopConfigManager configManager;

    /**
     * NPC 쉬프트 + 우클릭시 상점관리 메뉴 표시
     */
    @EventHandler
    public void onSneakRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        NPC npc = event.getNPC();

        if (!player.isSneaking()) return;
        if (!verifyNPCbyTrait(npc, "shop"))  return;

        player.openInventory(createInventoryGUI(MEDIUM, npc.getName()));
    }

    /**
     * 버튼 클릭시 각 메뉴 표시
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String shopName = event.getView().getTitle().split(" ")[0];

        if (event.getSlotType() != InventoryType.SlotType.CONTAINER) return;
        if (event.getCurrentItem() == null) return;

        configManager = new ShopConfigManager();
        String btnName = event.getCurrentItem().getItemMeta().getDisplayName();

        if (btnName.equals("카테고리 편집")) {
            event.setCancelled(true);
            Shop shop = configManager.loadShopData(shopName);
            List<Category> categoryList = shop.getCategoryList();
            player.openInventory(createInventoryGUI(MEDIUM, shopName, categoryList, false));
        }
        else if (btnName.equals("아이템 편집")) {
            event.setCancelled(true);
            Shop shop = configManager.loadShopData(shopName);
            player.openInventory(createInventoryGUI(LARGE, shopName, shop));
        }
        else if (btnName.equals("상점삭제")) {
            event.setCancelled(true);
            configManager.deleteShopData(shopName);
            NPCManager.deleteNPC(shopName);
            player.sendMessage("상점 " + shopName + " 이(가) 삭제되었습니다.");
        }
    }
}
