package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
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
        if(event.getDamager() == null) return;
        LivingEntity damager = event.getDamager();
        // Blindness for 15 seconds on hit
        if(damager.hasPotionEffect(PotionEffectType.BLINDNESS)) return;
        damager.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(15 * 20, 0));
    }

}
