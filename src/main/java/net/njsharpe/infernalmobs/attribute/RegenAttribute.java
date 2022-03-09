package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class RegenAttribute extends Attribute implements Cooldown {

    private long deltaTime = 0L;

    public RegenAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Regen";
    }

    @Override
    public boolean hasSpecial() {
        return false;
    }

    @Override
    public void onUpdate(InfernalEntity entity) {
        super.onUpdate(entity);
        double health = entity.getHealth();
        double max = entity.getMaxHealth();
        if(health < max) {
            long time = System.currentTimeMillis();
            if(!this.canUseAbility()) return;
            this.deltaTime = time + this.getCooldown();
            if(entity.getEntity().getFireTicks() > 0) return;
            entity.setHealth(Math.min(health + 1, max));
        }
    }

    @Override
    public long getCooldown() {
        return 1000L;
    }

    @Override
    public long getDeltaTime() {
        return this.deltaTime;
    }

}
