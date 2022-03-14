package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class CloakingAttribute extends Attribute implements Cooldown {

    private long deltaTime = 0L;

    public CloakingAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Cloaking";
    }

    @NotNull
    @Override
    public EntityType[] getBlacklist() {
        return new EntityType[] { EntityType.SPIDER };
    }

    @Override
    public boolean hasSpecial() {
        return true;
    }

    @Override
    public void onUpdate(InfernalEntity entity) {
        super.onUpdate(entity);
        if(!this.hasTarget()) return;
        this.useSpecial(entity, null);
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        super.onHurt(event);
        this.useSpecial(event.getInfernalEntity(), null);
    }

    @Override
    public void useSpecial(InfernalEntity source, LivingEntity target) {
        super.useSpecial(source, target);
        long time = System.currentTimeMillis();
        if(!this.canUseAbility()) return;
        this.deltaTime = time + this.getCooldown();
        source.getEntity().addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(200, 0));
    }

    @Override
    public long getCooldown() {
        return 10000L;
    }

    @Override
    public long getDeltaTime() {
        return this.deltaTime;
    }

}
