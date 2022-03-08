package net.njsharpe.infernalmobs.util;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class EntityHelper {

    public static Player getNearestPlayer(World world, double x, double y, double z, double r,
                                          @Nullable Predicate<Entity> predicate) {
        double d = -1.0D;
        Player player = null;
        for(Player p : world.getPlayers()) {
            if(predicate != null && !predicate.test(p)) continue;;
            double dist = EntityHelper.distanceToSqr(p, x, y, z);
            if(!(r < 0.0D) && !(dist < r * r) || d != -1.0D && !(dist < d)) continue;
            d = dist;
            player = p;
        }
        return player;
    }

    public static Player getNearestPlayer(World world, Entity entity, double r) {
        Location pos = entity.getLocation();
        return EntityHelper.getNearestPlayer(world, pos.getX(), pos.getY(), pos.getZ(), r, false);
    }

    public static Player getNearestPlayer(World world, double x, double y, double z, double r, boolean b) {
        Predicate<Entity> predicate = b
                ? entity -> (!(entity instanceof Player)) || !EntityHelper.isSpectator(entity)
                    && !((Player) entity).getGameMode().equals(GameMode.CREATIVE)
                : entity -> !EntityHelper.isSpectator(entity);
        return EntityHelper.getNearestPlayer(world, x, y, z, r, predicate);
    }

    public static boolean isSpectator(Entity entity) {
        if(!(entity instanceof Player)) return false;
        Player player = (Player) entity;
        return player.getGameMode().equals(GameMode.SPECTATOR);
    }

    public static double distanceToSqr(Entity entity, double x, double y, double z) {
        Location pos = entity.getLocation();
        double deltaX = pos.getX() - x;
        double deltaY = pos.getY() - y;
        double deltaZ = pos.getZ() - z;
        return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
    }

    public static double distanceToSqr(Entity entity, Entity target) {
        Location pos = target.getLocation();
        return distanceToSqr(entity, pos.getX(), pos.getY(), pos.getZ());
    }

    public static double distanceTo(Entity entity, Entity target) {
        Location pos = target.getLocation();
        return distanceToSqr(entity, pos.getX(), pos.getY(), pos.getZ());
    }

    public static float getXRot(Entity entity) {
        return entity.getLocation().getYaw();
    }

    public static void setXRot(Entity entity, float yaw) {
        entity.setRotation(yaw, getYRot(entity));
    }

    public static float getYRot(Entity entity) {
        return entity.getLocation().getPitch();
    }

    public static void setYRot(Entity entity, float pitch) {
        entity.setRotation(getXRot(entity), pitch);
    }

    public static boolean randomTeleport(Entity entity, double x, double y, double z) {
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
                if(noCollision(world, entity) && !containsAnyLiquid(world, entity.getBoundingBox())) {
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

    public static boolean noCollision(World world, Entity entity) {
        Location pos = entity.getLocation();
        for(int h = (int) Math.floor(pos.getY()); h < (int)(pos.getY() + entity.getHeight()); h++) {
            Block block = world.getBlockAt(pos.getBlockX(), h, pos.getBlockZ());
            if(!block.isPassable()) return false;
        }
        return true;
    }

    public static boolean containsAnyLiquid(World world, BoundingBox box) {
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
