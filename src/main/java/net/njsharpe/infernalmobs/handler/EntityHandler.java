package net.njsharpe.infernalmobs.handler;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.njsharpe.developmentutility.Format;
import net.njsharpe.developmentutility.data.RandomSet;
import net.njsharpe.developmentutility.helper.ArrayHelper;
import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.attribute.Attribute;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.event.*;
import net.njsharpe.infernalmobs.file.ConfigurationFile;
import org.bukkit.*;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.loot.LootTable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

import java.util.*;
import java.util.stream.Collectors;

public class EntityHandler implements Listener {

    public InfernalMobs getPlugin() {
        return InfernalMobs.get().orElseThrow(IllegalStateException::new);
    }

    @EventHandler
    public void onPlayerLook(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(ConfigurationFile.get().isHealthBarDisabled()) return;
        List<Entity> entities = player.getNearbyEntities(32, 32, 32);
        entities.forEach(entity -> {
            if(!(entity instanceof LivingEntity)) return;
            LivingEntity living = (LivingEntity) entity;
            if(!InfernalEntity.isInfernal(living)) return;
            InfernalEntity infernal = InfernalEntity.from(living);
            KeyedBossBar bar = infernal.getOrCreateBossBar();
            if(isLookingAt(player, living)) {
                bar.addPlayer(player);
            } else {
                bar.removePlayer(player);
            }
        });
    }

    private boolean isLookingAt(Player player, LivingEntity entity) {
        Location eye = player.getEyeLocation();
        RayTraceResult result = player.getWorld().rayTraceEntities(eye, eye.getDirection(), 32, 0.5F,
                (e) -> e.getUniqueId().equals(entity.getUniqueId()));
        if(result == null || result.getHitEntity() == null) return false;
        return player.hasLineOfSight(result.getHitEntity());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Iterator<KeyedBossBar> bars = Bukkit.getServer().getBossBars();
        while(bars.hasNext()) {
            KeyedBossBar bar = bars.next();
            if(bar.getKey().getNamespace().equalsIgnoreCase(this.getPlugin().getName()))
                bar.removePlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Iterator<KeyedBossBar> bars = Bukkit.getServer().getBossBars();
        while(bars.hasNext()) {
            KeyedBossBar bar = bars.next();
            if(bar.getKey().getNamespace().equalsIgnoreCase(this.getPlugin().getName()))
                bar.removePlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if(ConfigurationFile.get().getBlacklistedWorlds().contains(event.getLocation().getWorld())) return;
        if(!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        if(!(entity instanceof Monster)) return;
        Random random = new Random();
        ConfigurationFile config = ConfigurationFile.get();
        int count = 2 + random.nextInt(3);
        if(random.nextInt(config.getUltraRarity()) == 0) {
            count += 3 + random.nextInt(2);
            if(random.nextInt(config.getInfernalRarity()) == 0) {
                count += 3 + random.nextInt(2);
            }
        }
        if(random.nextInt(config.getEliteRarity()) == 0) {
            RandomSet<Attribute> set = new RandomSet<>(Arrays.asList(Attribute.values()));
            List<Attribute> attributes = new ArrayList<>();
            for(int i = 0; i < count; i++) {
                Attribute attribute = set.random(random);
                if(attribute == null) continue;
                if(ArrayHelper.contains(entity.getType(), attribute.getBlacklist())) continue;
                if(attributes.stream().anyMatch(a -> a.conflictsWith(attribute))) continue;
                attributes.add(attribute);
            }
            attributes = attributes.stream().filter(Objects::nonNull).collect(Collectors.toList());
            if(attributes.size() <= 0) event.setCancelled(true);
            InfernalEntity infernal = new InfernalEntity(entity, attributes);
            ((Mob) entity).setLootTable(this.getLootTable(infernal));
            InfernalEntitySpawnEvent e = new InfernalEntitySpawnEvent(infernal);
            Bukkit.getServer().getPluginManager().callEvent(e);
            event.setCancelled(e.isCancelled());
        }
    }

    @EventHandler
    public void onInfernalEntitySpawn(InfernalEntitySpawnEvent event) {
        InfernalEntity entity = event.getInfernalEntity();
        entity.onSpawn().run();
        entity.getAttributes().forEach(attribute -> attribute.onSpawn(event));
        Location location = event.getEntity().getLocation();
        Bukkit.getServer().getOnlinePlayers().forEach(p -> {
            TextComponent component = new TextComponent();
            component.setText(Format.colorize("&rMob spawned at: &a[%s, %s, %s]",
                    location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tp @s %s %s %s",
                    location.getBlockX(), location.getBlockY(), location.getBlockZ())));
            p.spigot().sendMessage(component);
        });
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(!InfernalEntity.isInfernal(event.getEntity())) return;
        InfernalEntity entity = InfernalEntity.from(event.getEntity());
        InfernalEntityDeathEvent e = new InfernalEntityDeathEvent(entity, event.getDrops(), event.getDroppedExp());
        Bukkit.getServer().getPluginManager().callEvent(e);
        event.setDroppedExp(e.getDroppedExp());
    }

    @EventHandler
    public void onInfernalEntityDeath(InfernalEntityDeathEvent event) {
        InfernalEntity entity = event.getInfernalEntity();

        KeyedBossBar bar = entity.getOrCreateBossBar();
        bar.removeAll();
        Bukkit.getServer().removeBossBar(bar.getKey());

        entity.getAttributes().forEach(attribute -> attribute.onDeath(event));
        entity.onDeath().run();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        if(!InfernalEntity.isInfernal(entity)) return;
        InfernalEntity infernal = InfernalEntity.from(entity);
        InfernalEntityHurtEvent e = new InfernalEntityHurtEvent(infernal, null, event.getCause(),
                event.getDamage());
        Bukkit.getServer().getPluginManager().callEvent(e);
        event.setCancelled(e.isCancelled());
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity damagee = (LivingEntity) event.getEntity();
        if(!InfernalEntity.isInfernal(damagee)) return;
        InfernalEntity infernal = InfernalEntity.from(damagee);
        if(!(event.getDamager() instanceof LivingEntity)) return;
        LivingEntity damager = (LivingEntity) event.getDamager();
        InfernalEntityHurtEvent e = new InfernalEntityHurtEvent(infernal, damager, event.getCause(),
                event.getDamage());
        Bukkit.getServer().getPluginManager().callEvent(e);
        event.setCancelled(e.isCancelled());
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if(!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        if(!InfernalEntity.isInfernal(entity)) return;
        InfernalEntity infernal = InfernalEntity.from(entity);
        // It killed itself, no drops -- don't handle if not dead.
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!entity.isDead()) return;
                InfernalEntityDeathEvent e = new InfernalEntityDeathEvent(infernal, new ArrayList<>(), 0);
                Bukkit.getServer().getPluginManager().callEvent(e);
            }
        }.runTaskLater(this.getPlugin(), 1L);
    }

    @EventHandler
    public void onInfernalEntityDamage(InfernalEntityHurtEvent event) {
        InfernalEntity entity = event.getInfernalEntity();
        entity.getAttributes().forEach(attribute -> attribute.onHurt(event));
    }

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getDamager();
        if(!InfernalEntity.isInfernal(entity)) return;
        InfernalEntity infernal = InfernalEntity.from(entity);
        InfernalEntityAttackEvent e = new InfernalEntityAttackEvent(infernal, event.getEntity(),
                event.getCause(), event.getDamage());
        Bukkit.getServer().getPluginManager().callEvent(e);
        event.setCancelled(e.isCancelled());
    }

    @EventHandler
    public void onEntityAttackWithProjectile(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if(!(event.getHitEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getHitEntity();
        if(!(projectile.getShooter() instanceof LivingEntity)) return;
        LivingEntity shooter = (LivingEntity) projectile.getShooter();
        if(!InfernalEntity.isInfernal(shooter)) return;
        InfernalEntity infernal = InfernalEntity.from(shooter);
        InfernalEntityAttackEvent e = new InfernalEntityAttackEvent(infernal, entity,
                EntityDamageEvent.DamageCause.PROJECTILE, entity.getLastDamage());
        Bukkit.getServer().getPluginManager().callEvent(e);
        event.setCancelled(e.isCancelled());
    }

    @EventHandler
    public void onInfernalEntityAttack(InfernalEntityAttackEvent event) {
        InfernalEntity entity = event.getInfernalDamager();
        entity.getAttributes().forEach(attribute -> attribute.onAttack(event));
    }

    @EventHandler
    public void onEntityTargetEntity(EntityTargetLivingEntityEvent event) {
        if(!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        if(!InfernalEntity.isInfernal(entity)) return;
        InfernalEntity infernal = InfernalEntity.from(entity);
        LivingEntity target = event.getTarget();
        if(target == null) return;
        InfernalEntityTargetEvent e = new InfernalEntityTargetEvent(infernal, target, event.getReason());
        Bukkit.getServer().getPluginManager().callEvent(e);
        event.setCancelled(e.isCancelled());
    }

    @EventHandler
    public void onInfernalEntityTarget(InfernalEntityTargetEvent event) {
        InfernalEntity entity = event.getInfernalEntity();
        entity.getAttributes().forEach(attribute -> attribute.onTarget(event));
    }

    @EventHandler
    public void onEntityJump(EntityChangeBlockEvent event) {
        Location ePos = event.getEntity().getLocation();
        Location bPos = event.getBlock().getLocation();
        if(ePos.getY() <= bPos.getY()) return;
        if(!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        if(!InfernalEntity.isInfernal(entity)) return;
        InfernalEntity infernal = InfernalEntity.from(entity);
        InfernalEntityJumpEvent e = new InfernalEntityJumpEvent(infernal, ePos, bPos);
        Bukkit.getServer().getPluginManager().callEvent(e);
    }

    @EventHandler
    public void onInfernalEntityJump(InfernalEntityJumpEvent event) {
        InfernalEntity entity = event.getInfernalEntity();
        entity.getAttributes().forEach(attribute -> attribute.onJump(event));
    }

    @EventHandler
    public void onEntityFall(EntityChangeBlockEvent event) {
        if(event.getEntity().getLocation().getY() >= event.getBlock().getLocation().getY()) return;
        if(!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        if(!InfernalEntity.isInfernal(entity)) return;
        InfernalEntity infernal = InfernalEntity.from(entity);
        InfernalEntityFallEvent e = new InfernalEntityFallEvent(infernal, entity.getFallDistance());
        Bukkit.getServer().getPluginManager().callEvent(e);
    }

    @EventHandler
    public void onInfernalEntityFall(InfernalEntityFallEvent event) {
        InfernalEntity entity = event.getInfernalEntity();
        entity.getAttributes().forEach(attribute -> attribute.onFall(event));
    }

    private LootTable getLootTable(InfernalEntity entity) {
        int size = entity.getAttributes().size();
        LootTable table = (size <= 5) ? this.getPlugin().getEliteLootTable() : (size <= 10)
                ? this.getPlugin().getUltraLootTable() : this.getPlugin().getInfernalLootTable();
        if(table == null) throw new RuntimeException("No loot table to drop from!");
        return table;
    }

}
