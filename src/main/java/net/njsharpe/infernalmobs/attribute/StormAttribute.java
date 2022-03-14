package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.developmentutility.helper.EntityHelper;
import net.njsharpe.developmentutility.helper.WorldHelper;
import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StormAttribute extends Attribute implements Cooldown {

    private long deltaTime = 0L;

    public StormAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Storm";
    }

    @Override
    public boolean hasSpecial() {
        return true;
    }

    @Override
    public boolean conflictsWith(Attribute attribute) {
        return attribute.equals(Attribute.STICKY);
    }

    @Override
    public void onUpdate(InfernalEntity entity) {
        super.onUpdate(entity);
        if(!this.hasTarget() || !(this.getTarget() instanceof Player)) return;
        long time = System.currentTimeMillis();
        if(!this.canUseAbility()) return;
        this.deltaTime = time + this.getCooldown();
        this.useSpecial(entity, this.getTarget());
    }

    @Override
    public void useSpecial(InfernalEntity source, LivingEntity target) {
        super.useSpecial(source, target);

        LivingEntity entity = source.getEntity();
        if(target == null || target.getVehicle() != null || !entity.hasLineOfSight(target)) return;

        if(EntityHelper.distanceTo(entity, target) > 3.0F) {
            Location t = target.getLocation();
            Location pos = new Location(target.getWorld(), Math.floor(t.getX()), Math.floor(t.getY()),
                    Math.floor(t.getZ()));
            if(!WorldHelper.canSeeSkyFromBelowWater(target.getWorld(), pos)) return;
            target.getWorld().strikeLightning(pos);
        }
    }

    @Override
    public long getCooldown() {
        return 25000L;
    }

    @Override
    public long getDeltaTime() {
        return this.deltaTime;
    }

}
