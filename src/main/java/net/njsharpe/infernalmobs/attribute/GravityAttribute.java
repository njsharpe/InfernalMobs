package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class GravityAttribute extends Attribute implements Cooldown {

    private long deltaTime = 0L;

    public GravityAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Gravity";
    }

    @Override
    public boolean hasSpecial() {
        return true;
    }

    @Override
    public boolean conflictsWith(Attribute attribute) {
//        return attribute.equals(Attribute.WEBBER);
        return false;
    }

    @Override
    public void onUpdate(InfernalEntity entity) {
        if(this.hasTarget() && this.getTarget() instanceof Player) this.useSpecial(entity, this.getTarget());
    }

    @Override
    public void useSpecial(InfernalEntity source, LivingEntity target) {
        super.useSpecial(source, target);

        LivingEntity entity = source.getEntity();
        if(target == null || !entity.hasLineOfSight(target)) return;

        long time = System.currentTimeMillis();
        if(!this.canUseAbility()) return;
        this.deltaTime = time + this.getCooldown();

        Location s = source.getLocation();
        Location t = target.getLocation();
        double x = t.getX() - s.getX();
        double z;
        for(z = t.getZ() - s.getZ(); x * x + z * z < 1.0E-4D; z = (Math.random() - Math.random()) * 0.01D) {
            x = (Math.random() - Math.random()) * 0.01D;
        }

        source.playSound(Sound.ENTITY_IRON_GOLEM_ATTACK);
        this.push(target, x, z);
    }

    private void push(LivingEntity entity, double x, double z) {
        float np = (float) Math.sqrt(x * x + z * z);
        float kp = 0.8F;

        Vector velocity = entity.getVelocity();
        double dx = velocity.getX();
        double dy = velocity.getX();
        double dz = velocity.getX();

        dx /= 2.0D;
        dy /= 2.0D;
        dz /= 2.0D;
        dx -= x / np * kp;
        dy += kp;
        dz -= z / np * kp;

        if(dy > 0.4000000059604645D) {
            dy = 0.4000000059604645D;
        }
        entity.setVelocity(new Vector(dx, dy, dz));
    }

    @Override
    public long getCooldown() {
        return 5000L;
    }

    @Override
    public long getDeltaTime() {
        return this.deltaTime;
    }

}
