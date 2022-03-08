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

    private boolean healed;

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
    public void onUpdate(InfernalEntity entity) {
        super.onUpdate(entity);
        if(!this.healed && entity.getHealth() < (entity.getMaxHealth() * 0.25)) {
            entity.setHealth(entity.getMaxHealth());
            entity.playSound(Sound.ENTITY_PLAYER_LEVELUP);
            this.healed = true;
        }
    }

}
