package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class CloakingAttribute extends Attribute {

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
        return false;
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        if(event.getEntity().isDead()) return;
        LivingEntity entity = event.getInfernalEntity().getEntity();
        // Invisibility for 15 seconds on hit
        if(entity.hasPotionEffect(PotionEffectType.INVISIBILITY)) return;
        entity.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(10 * 20, 0));
    }

}
