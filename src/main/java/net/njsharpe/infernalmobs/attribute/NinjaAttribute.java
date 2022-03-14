package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.developmentutility.helper.EntityHelper;
import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
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
        super.onHurt(event);
        long time = System.currentTimeMillis();
        Random random = new Random();
        LivingEntity entity = event.getInfernalEntity().getEntity();
        if(event.getDamager() == null || event.getDamager() == entity) return;
        boolean teleport = EntityHelper.teleport(entity, random, 4, (world, old, pos) -> {
            world.playSound(pos, Sound.ENTITY_GENERIC_EXPLODE, 1.0F + random.nextFloat(),
                    random.nextFloat() * 0.75F + 0.3F);
            world.spawnParticle(Particle.EXPLOSION_NORMAL, old, 1, 0D, 0D,0D);
        });
        if(!this.canUseAbility() || !teleport) return;
        this.deltaTime = time + this.getCooldown();
        entity.damage(event.getDamage());
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
