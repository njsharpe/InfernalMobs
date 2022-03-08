package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.event.InfernalEntityAttackEvent;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ExhaustAttribute extends Attribute {

    public ExhaustAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Exhaust";
    }

    @Override
    public boolean hasSpecial() {
        return false;
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        if(!(event.getDamager() instanceof Player)) return;
        ((Player) event.getDamager()).setExhaustion(1F);
    }

    @Override
    public void onAttack(InfernalEntityAttackEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        ((Player) event.getEntity()).setExhaustion(1F);
    }

}
