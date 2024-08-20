package me.revin.shopTradeMarket.shop.config;

import lombok.Getter;
import me.revin.shopTradeMarket.ShopTradeMarket;
import me.revin.shopTradeMarket.shop.entity.Shop;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static me.revin.shopTradeMarket.shop.config.ShopConfigSerializer.*;

public class ShopConfigManager {

    private static ShopConfigManager instance;
    private File file;
    private YamlConfiguration config;

    private static final String BASE_PATH = ShopTradeMarket.getInstance().getDataFolder().getAbsolutePath();
    private static final String FILE_NAME = "shoplist.yml";

    private ShopConfigManager() {
        this.file = new File(BASE_PATH, FILE_NAME);
        this.config = YamlConfiguration.loadConfiguration(file);
        save();
    }

    public static ShopConfigManager getInstance() {
        if (instance == null) {
            instance = new ShopConfigManager();
        }

        return instance;
    }

    public void saveShopData(Shop shop) {
        String shopId = Integer.toString(shop.getShopId());

        config.set(shopId, serialize(shop));
        save();
    }

    public Shop loadShopData(int id) {
        String shopId = Integer.toString(id);

        Map<String, Object> values = config.getConfigurationSection(shopId).getValues(false);
        Shop shop = deserialize(values);

        return shop;
    }

    public void deleteShopData(String shopName) {
        config.set(shopName, null);
        save();
        reload();
    }

    public boolean containsShopData(UUID uuid) {
        return config.contains(uuid.toString());
    }

    public void save() {
        if (config == null) return;

        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        if (file == null && file.exists()) return;
        config = YamlConfiguration.loadConfiguration(file);
    }
}