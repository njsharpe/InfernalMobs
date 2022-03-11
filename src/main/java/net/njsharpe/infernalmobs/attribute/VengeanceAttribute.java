package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class VengeanceAttribute extends Attribute {

    public VengeanceAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @Override
    public @NotNull String getName() {
        return "Vengeance";
    }

    @Override
    public boolean hasSpecial() {
        return false;
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        super.onHurt(event);
        LivingEntity entity = event.getInfernalEntity().getEntity();
        if(event.getDamager() == null || event.getDamager() == entity) return;
        event.getDamager().damage(Math.max(event.getDamage() / 2, 1), entity);
    }
}
