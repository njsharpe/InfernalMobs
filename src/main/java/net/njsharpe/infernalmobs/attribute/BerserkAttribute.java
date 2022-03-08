package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.event.InfernalEntityAttackEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class BerserkAttribute extends Attribute {

    public BerserkAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Berserk";
    }

    @NotNull
    @Override
    public EntityType[] getBlacklist() {
        return new EntityType[] { EntityType.CREEPER };
    }

    @Override
    public boolean hasSpecial() {
        return false;
    }

    @Override
    public void onAttack(InfernalEntityAttackEvent event) {
        InfernalEntity entity = event.getInfernalDamager();
        event.setDamage(event.getFinalDamage() * 2.0D);
        entity.getEntity().damage(event.getFinalDamage() / 1.5D);
    }

}
