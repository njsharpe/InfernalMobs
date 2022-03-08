package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import net.njsharpe.infernalmobs.util.EntityHelper;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class NinjaAttribute extends Attribute implements Cooldown {

    private long deltaTime = 0L;

    public NinjaAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Ninja";
    }

    @Override
    public boolean hasSpecial() {
        return false;
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        long time = System.currentTimeMillis();
        Random random = new Random();
        LivingEntity entity = event.getInfernalEntity().getEntity();
        if(event.getDamager() == null || event.getDamager() == entity) return;
        if(!this.canUseAbility() || !this.teleport(random, entity)) return;
        this.deltaTime = time + this.getCooldown();
        entity.damage(event.getDamage());
    }

    private boolean teleport(Random random, LivingEntity entity) {
        Location pos = entity.getLocation();
        double x = pos.getX() + (random.nextDouble() - 0.5D) * 4.0D;
        double y = pos.getY() + (double)(random.nextInt(16) - 4);
        double z = pos.getZ() + (random.nextDouble() - 0.5D) * 4.0D;
        return this.teleport(random, entity, x, y, z);
    }

    private boolean teleport(Random random, LivingEntity entity, double x, double y, double z) {
        World world = entity.getWorld();
        Location pos = new Location(world, x, y, z);
        while(pos.getY() > world.getMinHeight() && !world.getBlockAt(pos).isPassable()) {
            pos.add(0, -1, 0);
        }
        Block block = world.getBlockAt(pos);
        if(!block.isPassable() || block.isLiquid()) {
            return false;
        }
        Location old = entity.getLocation();
        boolean teleport = EntityHelper.randomTeleport(entity, x, y, z);
        if(teleport) {
            EntityTeleportEvent event = new EntityTeleportEvent(entity, old, pos);
            Bukkit.getServer().getPluginManager().callEvent(event);
            world.playSound(pos, Sound.ENTITY_GENERIC_EXPLODE, 1.0F + random.nextFloat(),
                    random.nextFloat() * 0.75F + 0.3F);
            world.spawnParticle(Particle.EXPLOSION_NORMAL, old, 1, 0D, 0D,0D);
        }
        return teleport;
    }

    @Override
    public long getCooldown() {
        return 15000L;
    }

    @Override
    public long getDeltaTime() {
        return this.deltaTime;
    }

}