package net.njsharpe.infernalmobs.entity;

import net.njsharpe.developmentutility.Format;
import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.attribute.Attribute;
import net.njsharpe.infernalmobs.util.RandomHelper;
import org.bukkit.*;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class InfernalEntity {

    private static final Map<NamespacedKey, InfernalEntity> byKey = new HashMap<>();

    private final LivingEntity entity;
    private final List<Attribute> attributes;
    private final NamespacedKey key;

    private InfernalEntity(@NotNull LivingEntity entity) {
        this(entity, new ArrayList<>());
    }

    public InfernalEntity(@NotNull LivingEntity entity, @NotNull List<Attribute> attributes) {
        AttributeInstance instance = entity.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH);
        if(instance == null) throw new IllegalStateException();
        instance.setBaseValue(100.0D);
        entity.setHealth(instance.getBaseValue());
        this.entity = entity;
        this.attributes = attributes;
        this.key = new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalStateException::new),
                this.entity.getUniqueId().toString());
    }

    @NotNull
    public LivingEntity getEntity() {
        return this.entity;
    }

    @NotNull
    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    public UUID getUniqueId() {
        return this.entity.getUniqueId();
    }

    public Location getLocation() {
        return this.entity.getLocation();
    }

    public World getWorld() {
        return this.entity.getWorld();
    }

    public boolean isDead() {
        return this.entity.isDead();
    }

    public double getHealth() {
        return this.entity.getHealth();
    }

    public void setHealth(double health) {
        this.entity.setHealth(health);
    }

    public double getMaxHealth() {
        AttributeInstance instance = this.entity.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH);
        return instance == null ? 20.0D : instance.getBaseValue();
    }

    public void playSound(Sound sound) {
        this.playSound(sound, this.getLocation());
    }

    public void playSound(Sound sound, Location location) {
        Random random = new Random();
        this.getWorld().playSound(location, sound, SoundCategory.HOSTILE, 1.0F + random.nextFloat(),
                random.nextFloat() * 0.7F + 0.3F);
    }

    public Runnable onSpawn() {
        return () -> {
            this.getOrCreateBossBar();
            this.playParticles();
            this.entity.setPersistent(true);
            if(!byKey.containsKey(this.key)) byKey.put(this.key, this);
            InfernalEntity entity = this;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(entity.isDead()) {
                        this.cancel();
                        return;
                    }
                    getAttributes().forEach(attribute -> attribute.onUpdate(entity));
                }
            }.runTaskTimer(InfernalMobs.get().orElseThrow(IllegalStateException::new),
                    0L, 1L);
        };
    }

    public Runnable onDeath() {
        return () -> {
            InfernalEntity.sync();
            if(!byKey.containsKey(this.key)) return;
            byKey.remove(this.key);
        };
    }

    @NotNull
    public KeyedBossBar getOrCreateBossBar() {
        KeyedBossBar bar = Bukkit.getServer().getBossBar(this.key);
        if(bar == null) {
            KeyedBossBar b = Bukkit.createBossBar(this.key, Format.colorize("&b%s &r(%s)", this.entity.getName(),
                            this.attributes.stream().map(Attribute::getName)
                                    .collect(Collectors.joining(" "))),
                    BarColor.PINK, BarStyle.SOLID);
            b.setProgress(1.0D);
            return b;
        }
        return bar;
    }

    public void playParticles() {
        Plugin plugin = InfernalMobs.get().orElseThrow(IllegalStateException::new);
        Random random = new Random();
        World world = this.entity.getWorld();
        new BukkitRunnable() {
            @Override
            public void run() {
                if(entity.isDead()) {
                    this.cancel();
                    return;
                }
                for(int i = 0; i < 4; i++) {
                    double red = (RandomHelper.nextIntInclusive(random, 0, 255) / 255.0D);
                    double blue = (RandomHelper.nextIntInclusive(random, 0, 255) / 255.0D);
                    double green = (RandomHelper.nextIntInclusive(random, 0, 255) / 255.0D);
                    world.spawnParticle(Particle.SPELL_MOB, entity.getLocation(), 0, red, blue, green, 1);
                }
            }
        }
        .runTaskTimerAsynchronously(plugin, 0L, 5L);
    }

    public <T, K> boolean has(@NotNull String id, @NotNull PersistentDataType<T, K> type) {
        PersistentDataContainer container = this.entity.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalStateException::new), id);
        return container.has(key, type);
    }

    public <T, K> void set(@NotNull String id, @NotNull PersistentDataType<T, K> type, @NotNull K value) {
        PersistentDataContainer container = this.entity.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalStateException::new), id);
        container.set(key, type, value);
    }

    public <T, K> Optional<K> get(@NotNull String id, @NotNull PersistentDataType<T, K> type) {
        PersistentDataContainer container = this.entity.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalStateException::new), id);
        return Optional.ofNullable(container.get(key, type));
    }

    public boolean hasAttribute(Attribute attribute) {
        for(Attribute a : this.getAttributes()) {
            if(a.getKey().equals(attribute.getKey())) return true;
        }
        return false;
    }

    @NotNull
    public static ItemStack getRandomDrop() {
        return new ItemStack(Material.STONE, 1);
    }

    public static boolean isInfernal(LivingEntity entity) {
        NamespacedKey key = new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalStateException::new),
                entity.getUniqueId().toString());
        return byKey.containsKey(key);
    }

    @NotNull
    public static InfernalEntity from(@NotNull final LivingEntity entity) {
        NamespacedKey key = new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalStateException::new),
                entity.getUniqueId().toString());
        if(byKey.containsKey(key)) return byKey.get(key);
        return new InfernalEntity(entity);
    }

    public static void sync() {
        byKey.values().forEach(entity -> {
            LivingEntity e = entity.getEntity();
            if(e.isDead() || Bukkit.getServer().getEntity(e.getUniqueId()) == null) {
                KeyedBossBar bar = entity.getOrCreateBossBar();
                bar.removeAll();
                Bukkit.getServer().removeBossBar(bar.getKey());
            }
        });
    }

}