package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class EnderAttribute extends Attribute {

    public EnderAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Ender";
    }

    @Override
    public boolean hasSpecial() {
        return false;
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        Random random = new Random();
        LivingEntity entity = event.getInfernalEntity().getEntity();
        if(event.getDamager() == null) return;
        float choice = random.nextFloat();
        if(choice >= 0.8F) {
            if(this.teleport(random, entity)) {
                event.setCancelled(true);
                event.setDamage(0.0D);
            }
        }
    }

    private boolean teleport(Random random, LivingEntity entity) {
        Location pos = entity.getLocation();
        double x = pos.getX() + (random.nextDouble() - 0.5D) * 16.0D;
        double y = pos.getY() + (double)(random.nextInt(16) - 8);
        double z = pos.getZ() + (random.nextDouble() - 0.5D) * 16.0D;
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
        boolean teleport = this.randomTeleport(entity, x, y, z);
        if(teleport) {
            EntityTeleportEvent event = new EntityTeleportEvent(entity, old, pos);
            Bukkit.getServer().getPluginManager().callEvent(event);
            world.playSound(old, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F + random.nextFloat(),
                    random.nextFloat() * 0.75F + 0.3F);
            world.playSound(pos, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F + random.nextFloat(),
                    random.nextFloat() * 0.75F + 0.3F);
        }
        return teleport;
    }

    private boolean randomTeleport(Entity entity, double x, double y, double z) {
        Location previous = entity.getLocation();
        double ox = previous.getX();
        double oy = previous.getY();
        double oz = previous.getZ();
        double yy = y;
        boolean safe = false;
        World world = entity.getWorld();
        Location location = new Location(world, x, yy, z);
        if(world.isChunkLoaded(location.getChunk())) {
            boolean ground = false;
            while(!ground && location.getY() > 0) {
                Location pos = location.getBlock().getRelative(BlockFace.DOWN).getLocation();
                BlockState state = world.getBlockState(pos);
                if(!state.getBlock().isPassable()) {
                    ground = true;
                    continue;
                }
                yy -= 1.0D;
                location = pos;
            }
            if(ground) {
                entity.teleport(location);
                if(this.noCollision(world, entity) && !this.containsAnyLiquid(world, entity.getBoundingBox())) {
                    safe = true;
                }
            }
        }
        if(!safe) {
            entity.teleport(new Location(world, ox, oy, oz));
            return false;
        }
        return true;
    }

    private boolean noCollision(World world, Entity entity) {
        Location pos = entity.getLocation();
        for(int h = (int) Math.floor(pos.getY()); h < (int)(pos.getY() + entity.getHeight()); h++) {
            Block block = world.getBlockAt(pos.getBlockX(), h, pos.getBlockZ());
            if(!block.isPassable()) return false;
        }
        return true;
    }

    private boolean containsAnyLiquid(World world, BoundingBox box) {
        int minX = (int) Math.floor(box.getMinX());
        int maxX = (int) Math.ceil(box.getMaxX());
        int minY = (int) Math.floor(box.getMinY());
        int maxY = (int) Math.ceil(box.getMaxY());
        int minZ = (int) Math.floor(box.getMinZ());
        int maxZ = (int) Math.floor(box.getMaxZ());
        for(int x = minX; x < maxX; x++) {
            for(int y = minY; y < maxY; y++) {
                for(int z = minZ; z < maxZ; z++) {
                    Location pos = new Location(world, x, y, z);
                    BlockState state = world.getBlockState(pos);
                    if(state.getBlock().isLiquid()) return true;
                }
            }
        }
        return false;
    }

}
