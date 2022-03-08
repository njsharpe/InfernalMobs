package net.njsharpe.infernalmobs.util;

import net.njsharpe.infernalmobs.entity.Direction;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
        return EntityHelper.distanceToSqr(entity, pos.getX(), pos.getY(), pos.getZ());
    }

    public static double distanceTo(Entity entity, Entity target) {
        Location pos = target.getLocation();
        return distanceToSqr(entity, pos.getX(), pos.getY(), pos.getZ());
    }

    public static Direction getDirectionBetween(LivingEntity source, LivingEntity target) {
        Location s = source.getLocation();
        Location t = target.getLocation();
        float x = (float) (t.getX() + t.getDirection().getX() - s.getX());
        float y = (float) (t.getY() + target.getEyeHeight() - 1.100000023841858D - s.getY());
        float z = (float) (t.getZ() + t.getDirection().getZ() - s.getZ());
        float distance = (float) Math.sqrt(x * x + z * z);
        return new Direction(x, y, z, distance);
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

}
