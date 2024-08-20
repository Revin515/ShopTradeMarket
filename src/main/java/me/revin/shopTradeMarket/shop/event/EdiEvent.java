package me.revin.shopTradeMarket.shop.event;

import lombok.RequiredArgsConstructor;
import me.revin.shopTradeMarket.common.gui.GUIManager;
import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.entity.Item;
import me.revin.shopTradeMarket.shop.entity.Shop;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static me.revin.shopTradeMarket.common.npc.NPCManager.verifyNPCbyTrait;
import static net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText;

@RequiredArgsConstructor
public class EdiEvent implements Listener {

    private final ShopConfigManager configManager;
    private final PlayerContextManager contextManager;
    private final GUIManager guiManager;

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
        int npcId = npc.getId();
        int page = 0;

        Shop shop = configManager.loadShopData(npcId);
        UUID shopOwner = shop.getShopOwner();

        if (!player.isOp()) {
            if (!shopOwner.equals(uuid)) return;
        }

        PlayerContext playerContext = new PlayerContext();
        playerContext.setNpcId(npcId);
        playerContext.setPage(page);
        playerContext.setShop(shop);
        contextManager.getPlayerContextMap().put(uuid, playerContext);

        Inventory shopGUI = guiManager.createShopGUI(shop.getShopName() + " 편집", shop, page);
        player.openInventory(shopGUI);
    }

    /**
     * 플레이어 인벤토리에서 상점 인벤토리에 아이템을 놓으면 가격 설정 GUI 오픈
     */
    @EventHandler
    public void onPlaceItem(InventoryClickEvent event) {
        if (!event.getClickedInventory().equals(InventoryType.CHEST)) {
            return;
        } else if (!event.getAction().equals(InventoryAction.PLACE_ALL) ||
                   !event.getAction().equals(InventoryAction.PLACE_SOME) ||
                   !event.getAction().equals(InventoryAction.PLACE_ONE)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        PlayerContext playerContext = contextManager.getPlayerContextMap().get(uuid);

        ItemStack currentItem = event.getCurrentItem();
        String itemId = currentItem.getType().getKey().toString();
        int quantity = currentItem.getAmount();
        int pos = event.getSlot();

        playerContext.setItem(new Item(itemId, quantity, 0, pos));
        playerContext.setInventory(event.getClickedInventory());

        Inventory setPriceGUI = guiManager.createSetPriceGUI();
        player.openInventory(setPriceGUI);
    }

    /**
     * 모루의 이름변경 기능을 이용해 가격 설정
     * 가격 설정이 완료된 아이템을 클릭하면 가격 정보가 저장되고 원래 인벤토리로 복귀
     */
    @EventHandler
    public void onSetItemPrice(InventoryClickEvent event) {
        if (!event.getClickedInventory().equals(InventoryType.ANVIL)) {
            return;
        } else if (!plainText().serialize(event.getView().title()).equals("가격 설정")) {
            return;
        } else if (!event.getAction().equals(InventoryAction.PICKUP_ONE)) {
            return;
        } else if (event.getSlot() != 2) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        PlayerContext playerContext = contextManager.getPlayerContextMap().get(uuid);

        int page = playerContext.getPage();
        Shop shop = playerContext.getShop();
        List<Item> itemList = shop.getItemList().get(page);
        Item newItem = playerContext.getItem();

        String setPrice = plainText().serialize(event.getCurrentItem().getItemMeta().displayName());

        try {
            int parsePrice = Integer.parseInt(setPrice);
            int price = Math.abs(parsePrice);
            newItem.setPrice(price);
            itemList.add(newItem);
        } catch (NumberFormatException e) {
            player.sendMessage("아이템 가격은 0 ~ 2,147,483,648 까지의 정수만 입력 가능합니다.");
        }

        event.setCancelled(true);

        Inventory shopGUI = guiManager.createShopGUI(shop.getShopName() + " 편집", shop, page);
        player.openInventory(shopGUI);
    }
}
