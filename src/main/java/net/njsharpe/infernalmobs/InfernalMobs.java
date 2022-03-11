package net.njsharpe.infernalmobs;

import net.njsharpe.infernalmobs.attribute.Attribute;
import net.njsharpe.infernalmobs.command.InfernalMobsCommand;
import net.njsharpe.infernalmobs.handler.EntityHandler;
import net.njsharpe.infernalmobs.util.ArrayHelper;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.plugin.java.JavaPlugin;

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
//        Attribute.registerAttribute(Attribute.WEAKNESS);
//        Attribute.registerAttribute(Attribute.WEBBER);
//        Attribute.registerAttribute(Attribute.WITHER);
        Attribute.stopAcceptingRegistrations();

        this.getServer().getPluginManager().registerEvents(new EntityHandler(), this);
        this.getCommand("infernalmobs").setExecutor(new InfernalMobsCommand());
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
