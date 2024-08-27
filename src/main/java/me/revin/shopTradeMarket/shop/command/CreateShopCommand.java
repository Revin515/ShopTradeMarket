package me.revin.shopTradeMarket.shop.command;

import lombok.RequiredArgsConstructor;
import me.revin.shopTradeMarket.common.npc.NPCManager;
import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import me.revin.shopTradeMarket.shop.entity.ShopType;
import me.revin.shopTradeMarket.shop.entity.Item;
import me.revin.shopTradeMarket.shop.entity.Shop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class CreateShopCommand implements CommandExecutor {

    private final ShopConfigManager configManager;
    private final NPCManager npcManager;

    /**
     * /상점생성 [상점이름]
     * 명령어를 사용한 플레이어가 OP면 Server 상점, 아니면 User 상점
     * User 상점일 경우 config 파일에 플레이어 UUID로 상점이 존재하는지 확인한 후 존재하면 return
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("플레이어만 입력 가능합니다.");
            return false;
        } else if(args.length < 1) {
            return false;
        }

        Player player = (Player) sender;
        String shopName = args[0];
        int shopId = npcManager.createNPC(player, shopName);
        UUID shopOwner = player.getUniqueId();
        ShopType shopType;

        if (player.isOp()) {
            shopType = ShopType.SERVER;
        } else {
            if (configManager.containsShopData(shopOwner)) {
                player.sendMessage("유저 상점은 1개만 보유할 수 있습니다.");
                return false;
            }

            shopType = ShopType.USER;
        }

        List<List<Item>> itemPageList = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();

        itemPageList.add(itemList);

        Shop shop = new Shop(shopId, shopName, shopType, shopOwner, itemPageList);
        configManager.saveShopData(shop);

        player.sendMessage("상점 " + shopName + " 이(가) 생성되었습니다.");

        return true;
    }
}
