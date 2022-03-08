package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class OneUpAttribute extends Attribute {

    public OneUpAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "1UP";
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
    public void onHurt(InfernalEntityHurtEvent event) {
        InfernalEntity entity = event.getInfernalEntity();
        if(!entity.has("healed", PersistentDataType.INTEGER)) {
            entity.set("healed", PersistentDataType.INTEGER, 0);
        }
        // Already Healed
        if(entity.get("healed", PersistentDataType.INTEGER).orElse(0) != 0) return;
        double health = entity.getHealth() - event.getFinalDamage();
        if(health < (entity.getMaxHealth() * 0.25D)) {
            entity.setHealth(entity.getMaxHealth());
            entity.playSound(Sound.ENTITY_PLAYER_LEVELUP);
            entity.set("healed", PersistentDataType.INTEGER, 1);
        }
    }

}
