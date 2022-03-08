package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.event.InfernalEntityAttackEvent;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class DarknessAttribute extends Attribute {

    public DarknessAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Darkness";
    }

    @Override
    public boolean hasSpecial() {
        return false;
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        super.onHurt(event);
        if(event.getDamager() == null) return;
        event.getDamager().addPotionEffect(PotionEffectType.BLINDNESS.createEffect(120, 0));
    }

    @Override
    public void onAttack(InfernalEntityAttackEvent event) {
        super.onAttack(event);
        if(!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        entity.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(120, 0));
    }
}
