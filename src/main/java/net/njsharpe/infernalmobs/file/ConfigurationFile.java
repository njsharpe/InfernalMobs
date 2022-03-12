package net.njsharpe.infernalmobs.file;

import net.njsharpe.infernalmobs.InfernalMobs;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationFile {

    private static ConfigurationFile instance;

    public static ConfigurationFile get() {
        instance = (instance == null) ? new ConfigurationFile() : instance;
        return instance;
    }

    private final File file;
    private FileConfiguration config;

    private ConfigurationFile() {
        Plugin plugin = this.getPlugin();
        if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
        this.file = new File(plugin.getDataFolder(), "config.yml");
        if(!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException ex) {
                Bukkit.getServer().getPluginManager().disablePlugin(plugin);
                throw new RuntimeException("Could not create configuration file!", ex);
            }
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
        this.loadDefaultConfiguration();
    }

    private void loadDefaultConfiguration() {
        this.setEliteRarity(15);
        this.setUltraRarity(7);
        this.setInfernalRarity(7);
        this.setHealthBarDisabled(false);
        this.setBlacklistedWorlds(new ArrayList<>());

        this.config.options().copyDefaults(true);
        this.save();
    }

    public FileConfiguration getConfig() {
        if(this.config == null) {
            this.reload();
        }
        return this.config;
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public int getInfernalRarity() {
        return this.config.getInt("rarity.infernal");
    }

    public void setInfernalRarity(int rarity) {
        this.config.set("rarity.infernal", rarity);
    }

    public int getUltraRarity() {
        return this.config.getInt("rarity.ultra");
    }

    public void setUltraRarity(int rarity) {
        this.config.set("rarity.ultra", rarity);
    }

    public int getEliteRarity() {
        return this.config.getInt("rarity.elite");
    }

    public void setEliteRarity(int rarity) {
        this.config.set("rarity.elite", rarity);
    }

    public boolean isHealthBarDisabled() {
        return this.config.getBoolean("disable_healthbar");
    }

    public void setHealthBarDisabled(boolean disabled) {
        this.config.set("disable_healthbar", disabled);
    }

    public List<World> getBlacklistedWorlds() {
        return this.config.getStringList("blacklisted_worlds").stream().map(Bukkit::getWorld)
                .collect(Collectors.toList());
    }

    public void setBlacklistedWorlds(List<World> worlds) {
        this.config.set("blacklisted_worlds", worlds.stream().map(World::getName).collect(Collectors.toList()));
    }

    public Plugin getPlugin() {
        return InfernalMobs.get().orElseThrow(IllegalStateException::new);
    }

}
