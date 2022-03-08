package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class QuicksandAttribute extends Attribute {

    private int ticks = 0;

    public QuicksandAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Quicksand";
    }

    @Override
    public boolean hasSpecial() {
        return false;
    }

    @Override
    public void onUpdate(InfernalEntity entity) {
        super.onUpdate(entity);
        if(!this.hasTarget() || !entity.getEntity().hasLineOfSight(this.getTarget()) || this.ticks++ != 50) return;
        this.ticks = 0;
        this.getTarget().addPotionEffect(PotionEffectType.SLOW.createEffect(45, 0));
    }
}
