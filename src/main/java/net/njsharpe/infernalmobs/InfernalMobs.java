package net.njsharpe.infernalmobs;

import net.njsharpe.developmentutility.helper.ArrayHelper;
import net.njsharpe.infernalmobs.attribute.Attribute;
import net.njsharpe.infernalmobs.command.InfernalMobsCommand;
import net.njsharpe.infernalmobs.file.ConfigurationFile;
import net.njsharpe.infernalmobs.handler.EntityHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class InfernalMobs extends JavaPlugin {

    private static InfernalMobs instance;

    @Override
    public void onEnable() {
        instance = this;

        // Attributes
        Attribute.registerAttribute(Attribute.ONE_UP);
        Attribute.registerAttribute(Attribute.ALCHEMIST);
        Attribute.registerAttribute(Attribute.BERSERK);
        Attribute.registerAttribute(Attribute.BLASTOFF);
        Attribute.registerAttribute(Attribute.BULWARK);
        Attribute.registerAttribute(Attribute.CHOKE);
        Attribute.registerAttribute(Attribute.CLOAKING);
        Attribute.registerAttribute(Attribute.DARKNESS);
        Attribute.registerAttribute(Attribute.ENDER);
        Attribute.registerAttribute(Attribute.EXHAUST);
        Attribute.registerAttribute(Attribute.FIERY);
        Attribute.registerAttribute(Attribute.GHASTLY);
        Attribute.registerAttribute(Attribute.GRAVITY);
        Attribute.registerAttribute(Attribute.LIFESTEAL);
        Attribute.registerAttribute(Attribute.NINJA);
        Attribute.registerAttribute(Attribute.POISONOUS);
        Attribute.registerAttribute(Attribute.QUICKSAND);
        Attribute.registerAttribute(Attribute.REGEN);
        Attribute.registerAttribute(Attribute.RUST);
        Attribute.registerAttribute(Attribute.SAPPER);
        Attribute.registerAttribute(Attribute.SPRINT);
        Attribute.registerAttribute(Attribute.STICKY);
        Attribute.registerAttribute(Attribute.STORM);
        Attribute.registerAttribute(Attribute.VENGEANCE);
        Attribute.registerAttribute(Attribute.WEAKNESS);
        Attribute.registerAttribute(Attribute.WEBBER);
        Attribute.registerAttribute(Attribute.WITHER);
        Attribute.stopAcceptingRegistrations();

        // Initialize Configuration
        ConfigurationFile config = ConfigurationFile.get();

        for(World world : this.getServer().getWorlds()) {
            if(config.getBlacklistedWorlds().contains(world)) continue;
            this.copyLootTables(world);
        }

        this.getServer().reloadData();

        this.getServer().getPluginManager().registerEvents(new EntityHandler(), this);
        this.getCommand("infernalmobs").setExecutor(new InfernalMobsCommand());
    }

    private File getEliteFile(World world) {
        String prefix = "datapacks/bukkit/data/infernalmobs/loot_tables/entities";
        return new File(world.getWorldFolder(), String.format("%s/elite.json", prefix));
    }

    private File getUltraFile(World world) {
        String prefix = "datapacks/bukkit/data/infernalmobs/loot_tables/entities";
        return new File(world.getWorldFolder(), String.format("%s/ultra.json", prefix));
    }

    private File getInfernalFile(World world) {
        String prefix = "datapacks/bukkit/data/infernalmobs/loot_tables/entities";
        return new File(world.getWorldFolder(), String.format("%s/infernal.json", prefix));
    }

    private void copyLootTables(World world) {
        String prefix = "datapacks/bukkit/data/infernalmobs/loot_tables/entities";
        try {
            File folder = new File(world.getWorldFolder(), prefix);
            if(!folder.exists()) {
                folder.mkdirs();
            }

            File elite = this.getEliteFile(world);
            if(!elite.exists()) {
                InputStream stream = this.getResource("loot_tables/elite.json");
                Files.copy(stream, elite.toPath());
            }

            File ultra = this.getUltraFile(world);
            if(!ultra.exists()) {
                InputStream stream = this.getResource("loot_tables/ultra.json");
                Files.copy(stream, ultra.toPath());
            }

            File infernal = this.getInfernalFile(world);
            if(!infernal.exists()) {
                InputStream stream = this.getResource("loot_tables/infernal.json");
                Files.copy(stream, infernal.toPath());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Nullable
    public LootTable getEliteLootTable() {
        NamespacedKey key = new NamespacedKey(this, "entities/elite");
        return this.getServer().getLootTable(key);
    }

    @Nullable
    public LootTable getUltraLootTable() {
        NamespacedKey key = new NamespacedKey(this, "entities/ultra");
        return this.getServer().getLootTable(key);
    }

    @Nullable
    public LootTable getInfernalLootTable() {
        NamespacedKey key = new NamespacedKey(this, "entities/infernal");
        return this.getServer().getLootTable(key);
    }

    @Override
    public void onDisable() {
        int i = 0;
        List<KeyedBossBar> bars = ArrayHelper.fromIterator(this.getServer().getBossBars()).stream()
                .filter(bar -> bar.getKey().getNamespace().equals(this.getName().toLowerCase()))
                .collect(Collectors.toList());
        for(; i < bars.size(); i++) {
            KeyedBossBar bar = bars.get(i);
            bar.removeAll();
            this.getServer().removeBossBar(bar.getKey());
        }
        System.out.printf("Removed %s boss bars!\n", i);
        instance = null;
    }

    public static Optional<InfernalMobs> get() {
        return Optional.ofNullable(instance);
    }

}
