package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.util.EntityHelper;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class GhastlyAttribute extends Attribute implements Cooldown {

    private long deltaTime = 0L;

    public GhastlyAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Ghastly";
    }

    @Override
    public boolean hasSpecial() {
        return true;
    }

    @Override
    public void onUpdate(InfernalEntity entity) {
        super.onUpdate(entity);
        if(!this.hasTarget()) return;
        long time = System.currentTimeMillis();
        if(!this.canUseAbility()) return;
        this.deltaTime = time + this.getCooldown();
        this.useSpecial(entity, EntityHelper.getNearestPlayer(entity.getWorld(), entity.getEntity(), 12.0F));
    }

    @Override
    public void useSpecial(InfernalEntity source, LivingEntity target) {
        super.useSpecial(source, target);

        LivingEntity entity = source.getEntity();
        if(target == null || !entity.hasLineOfSight(target)) return;

        if(EntityHelper.distanceTo(entity, target) > 3.0F) {
            Location s = source.getLocation();
            Location t = target.getLocation();
            double height = target.getBoundingBox().getHeight();
            double x = t.getX() - s.getX();
            double y = target.getBoundingBox().getMinY() + (height / 2.0F) - (s.getY() + (height / 2.0F));
            double z = t.getZ() - s.getZ();
            EntityHelper.setYRot(entity, -((float) Math.atan2(x, z)) * 180.0F / (float) Math.PI);
            LargeFireball fireball = source.getWorld().spawn(s, LargeFireball.class);
            fireball.setVelocity(new Vector(x, y, z));
            fireball.setYield(1.0F);
            double offset = 2.0D;
            Vector looking = this.getViewVector(entity, 1.0F);
            double nx = s.getX() + looking.getX() * offset;
            double ny = s.getY() + (height / 2.0F) + 0.5D;
            double nz = s.getZ() + looking.getZ() * offset;
            fireball.teleport(new Location(source.getWorld(), nx, ny, nz));
        }
    }

    private Vector getViewVector(Entity entity, float f) {
        return this.calculateViewVector(this.getViewXRot(entity, f), this.getViewYRot(entity, f));
    }

    private float getViewXRot(Entity entity, float f) {
        Location old = entity.getLocation();
        if(f == 1.0F) return EntityHelper.getXRot(entity);
        return this.lerp(f, old.getYaw(), EntityHelper.getXRot(entity));
    }

    private float getViewYRot(Entity entity, float f) {
        Location old = entity.getLocation();
        if(f == 1.0F) return EntityHelper.getYRot(entity);
        return this.lerp(f, old.getPitch(), EntityHelper.getYRot(entity));
    }

    private Vector calculateViewVector(float x, float y) {
        float px = x * 0.017453292F;
        float ny = -y * 0.017453292F;
        double cosY = Math.cos(ny);
        double sinY = Math.sin(ny);
        double cosX = Math.cos(px);
        double sinX = Math.sin(px);
        return new Vector(sinY * cosX, -sinX, cosY * cosX);
    }

    private float lerp(float i, float j, float k) {
        return j + i * (k - j);
    }

    @Override
    public long getCooldown() {
        return 6000L;
    }

    @Override
    public long getDeltaTime() {
        return this.deltaTime;
    }

}
