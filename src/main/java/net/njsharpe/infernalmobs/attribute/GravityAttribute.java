package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.developmentutility.helper.EntityHelper;
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
        return attribute.equals(Attribute.WEBBER);
    }

    @Override
    public void onUpdate(InfernalEntity entity) {
        super.onUpdate(entity);
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
        EntityHelper.push(target, x, z);
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
