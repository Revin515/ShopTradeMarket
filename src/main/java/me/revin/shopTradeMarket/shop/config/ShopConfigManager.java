package me.revin.shopTradeMarket.shop.config;

import me.revin.shopTradeMarket.ShopTradeMarket;
import me.revin.shopTradeMarket.common.config.ConfigManager;
import me.revin.shopTradeMarket.shop.entity.Shop;

import java.util.Map;

import static me.revin.shopTradeMarket.shop.config.ShopConfigSerializer.*;

public class ShopConfigManager extends ConfigManager {

    private static final String BASE_PATH = ShopTradeMarket.getInstance().getDataFolder().getAbsolutePath();
    private static final String FILE_NAME = "shoplist.yml";

    public ShopConfigManager() {
        super(BASE_PATH, FILE_NAME);
    }

    public void saveShopData(Shop shop) {
        getConfig().set(shop.getShopName(), serialize(shop));
        save();
    }

    public Shop loadShopData(String shopName) {
        Map<String, Object> values = getConfig().getConfigurationSection(shopName).getValues(false);
        Shop shop = deserialize(values);

        return shop;
    }

    public void deleteShopData(String shopName) {
        getConfig().set(shopName, null);
        save();
        reload();
    }
}
