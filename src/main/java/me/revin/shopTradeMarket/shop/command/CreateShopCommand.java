package me.revin.shopTradeMarket.shop.command;

import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.entity.Category;
import me.revin.shopTradeMarket.shop.entity.Item;
import me.revin.shopTradeMarket.shop.entity.Shop;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static me.revin.shopTradeMarket.common.npc.NPCManager.*;

public class CreateShopCommand implements CommandExecutor {

    private ShopConfigManager configManager;

    /**
     * /상점생성 [상점이름]
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 입력 가능합니다.");
            return false;
        } else if(args.length < 1) {
            return false;
        }

        configManager = new ShopConfigManager();
        Player player = (Player) sender;
        String shopName = args[0];
        int id = createNPC(player, shopName);

        List<Item> itemList = new ArrayList<>();
        Category category = new Category("기본", itemList);

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category);

        Shop shop = new Shop(id, shopName, categoryList);
        configManager.saveShopData(shop);

        player.sendMessage("상점 " + shopName + " 이(가) 생성되었습니다.");

        return true;
    }
}
