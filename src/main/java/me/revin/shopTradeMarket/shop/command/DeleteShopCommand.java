package me.revin.shopTradeMarket.shop.command;

import lombok.RequiredArgsConstructor;
import me.revin.shopTradeMarket.common.npc.NPCManager;
import me.revin.shopTradeMarket.shop.config.ShopConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class DeleteShopCommand implements CommandExecutor {

    private final ShopConfigManager configManager;
    private final NPCManager npcManager;

    /**
     * /상점삭제 [상점이름]
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

        configManager.deleteShopData(shopName);
        npcManager.deleteNPC(shopName);
        player.sendMessage("상점 " + shopName + " 이(가) 삭제되었습니다.");

        return true;
    }
}
