package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.developmentutility.helper.EntityHelper;
import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class BlastoffAttribute extends Attribute implements Cooldown {

    private long deltaTime = 0L;

    public BlastoffAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Blastoff";
    }

    @Override
    public boolean hasSpecial() {
        return true;
    }

    @Override
    public void onUpdate(InfernalEntity entity) {
        super.onUpdate(entity);
        this.useSpecial(entity, EntityHelper.getNearestPlayer(entity.getWorld(), entity.getEntity(), 12.0F));
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        super.onHurt(event);
        if(event.getDamager() != null) this.useSpecial(event.getInfernalEntity(), event.getDamager());
    }

    @Override
    public void useSpecial(InfernalEntity source, LivingEntity target) {
        super.useSpecial(source, target);
        if(target == null || !source.getEntity().hasLineOfSight(target)) return;
        long time = System.currentTimeMillis();
        if(!this.canUseAbility()) return;
        this.deltaTime = time + this.getCooldown();
        source.playSound(Sound.ENTITY_SLIME_JUMP);
        target.setVelocity(target.getVelocity().setY(1.1D));
    }

    @Override
    public boolean conflictsWith(Attribute attribute) {
        return attribute.equals(Attribute.WEBBER);
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
