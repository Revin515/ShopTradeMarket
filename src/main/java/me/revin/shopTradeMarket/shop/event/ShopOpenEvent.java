package me.revin.shopTradeMarket.shop.event;

import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.entity.Category;
import me.revin.shopTradeMarket.shop.entity.Shop;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static me.revin.shopTradeMarket.common.npc.NPCManager.*;
import static org.bukkit.Bukkit.*;

public class ShopOpenEvent implements Listener {

    private ShopConfigManager configManager;

    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        NPC npc = event.getNPC();

        if (verifyNPCbyTrait(npc, "shop")) {
            configManager = new ShopConfigManager();
            Shop shop = configManager.loadShopData(npc.getName());
            Inventory inventory = createInventory(null, 54, npc.getName());
            List<Category> categoryList = shop.getCategoryList();

            for (int i = 0; i < categoryList.size(); i++) {
                Category category = categoryList.get(i);

                ItemStack categoryBtn = new ItemStack(Material.CHEST);
                ItemMeta itemMeta = categoryBtn.getItemMeta();
                itemMeta.setDisplayName(category.getCategoryName());
                categoryBtn.setItemMeta(itemMeta);

                inventory.setItem(i, categoryBtn);
            }

            player.openInventory(inventory);
        }
    }
}
