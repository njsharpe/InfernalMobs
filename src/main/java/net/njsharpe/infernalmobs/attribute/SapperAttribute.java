package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.event.InfernalEntityAttackEvent;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class SapperAttribute extends Attribute {

    public SapperAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Sapper";
    }

    @Override
    public boolean hasSpecial() {
        return false;
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        super.onHurt(event);
        if(event.getDamager() == null) return;
        LivingEntity entity = event.getDamager();
        if(!entity.hasPotionEffect(PotionEffectType.HUNGER)) {
            entity.addPotionEffect(PotionEffectType.HUNGER.createEffect(120, 0));
        }
    }

    @Override
    public void onAttack(InfernalEntityAttackEvent event) {
        super.onAttack(event);
        if(!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        if(!entity.hasPotionEffect(PotionEffectType.HUNGER)) {
            entity.addPotionEffect(PotionEffectType.HUNGER.createEffect(120, 0));
        }
    }

}
