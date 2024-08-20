package me.revin.shopTradeMarket.shop.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerContextManager {

    private static PlayerContextManager instance;
    private final Map<UUID, PlayerContext> playerContextMap;

    private PlayerContextManager() {
        this.playerContextMap = new HashMap<>();
    }

    public static PlayerContextManager getInstance() {
        if (instance == null) {
            instance = new PlayerContextManager();
        }

        return instance;
    }

    public Map<UUID, PlayerContext> getPlayerContextMap() {
        return playerContextMap;
    }
}
