package me.revin.shopTradeMarket.common.config;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class ConfigManager {

    private File file;

    @Getter
    private YamlConfiguration config;

    public ConfigManager(String basePath, String fileName) {
        this.file = new File(basePath, fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
        save();
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
