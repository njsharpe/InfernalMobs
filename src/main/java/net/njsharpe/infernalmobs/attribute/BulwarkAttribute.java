package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class BulwarkAttribute extends Attribute {

    public BulwarkAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Bulwark";
    }

    @Override
    public boolean hasSpecial() {
        return false;
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        super.onHurt(event);
        event.setDamage(event.getFinalDamage() / 2.0D);
    }

}
