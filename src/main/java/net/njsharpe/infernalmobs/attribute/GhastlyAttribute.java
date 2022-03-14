package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.developmentutility.helper.EntityHelper;
import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

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
            Vector looking = EntityHelper.getViewVector(entity, 1.0F);
            double nx = s.getX() + looking.getX() * offset;
            double ny = s.getY() + (height / 2.0F) + 0.5D;
            double nz = s.getZ() + looking.getZ() * offset;
            fireball.teleport(new Location(source.getWorld(), nx, ny, nz));
        }
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
