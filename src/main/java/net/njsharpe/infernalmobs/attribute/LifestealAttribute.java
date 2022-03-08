package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.event.InfernalEntityAttackEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class LifestealAttribute extends Attribute {

    public LifestealAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Lifesteal";
    }

    @Override
    public boolean hasSpecial() {
        return false;
    }

    @Override
    public @NotNull EntityType[] getBlacklist() {
        return new EntityType[] { EntityType.CREEPER };
    }

    @Override
    public void onAttack(InfernalEntityAttackEvent event) {
        InfernalEntity damager = event.getInfernalDamager();
        double health = damager.getHealth() + event.getDamage();
        event.getInfernalDamager().setHealth(Math.min(health, damager.getMaxHealth()));
    }
}
