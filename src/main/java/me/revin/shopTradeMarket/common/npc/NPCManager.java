package me.revin.shopTradeMarket.common.npc;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class NPCManager {

    private static NPCManager instance;

    private NPCManager() {}

    public int createNPC(Player player, String name) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        npc.spawn(player.getLocation());
        npc.addTrait(ShopTrait.class);

        return npc.getId();
    }

    public boolean verifyNPCbyTrait(NPC npc, String traitName) {
        for (Trait trait : npc.getTraits()) {
            if (trait.getName().equals("shop")) return true;
        }

        return false;
    }

    public void deleteNPC(String name) {
        NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();

        for (NPC npc : npcRegistry) {
            if (npc.getName().equals(name)) {
                npc.destroy();
                break;
            }
        }
    }

    public void registerTrait() {
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(ShopTrait.class));
    }

    public static NPCManager getInstance() {
        if (instance == null) {
            instance = new NPCManager();
        }

        return instance;
    }
}
