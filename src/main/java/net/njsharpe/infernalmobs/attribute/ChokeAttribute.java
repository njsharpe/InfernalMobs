package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.AirTickPacketWrapper;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.event.InfernalEntityAttackEvent;
import net.njsharpe.infernalmobs.event.InfernalEntityDeathEvent;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;

public class ChokeAttribute extends Attribute {

    private static final int RESET_AIR_VALUE = -999;
    private LivingEntity target;
    private int air;

    public ChokeAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Choke";
    }

    @Override
    public boolean hasSpecial() {
        return true;
    }

    @Override
    public void onUpdate(InfernalEntity entity) {
        if(!this.hasTarget()) return;
        if(this.getTarget() != this.target) {
            if(this.target != null) this.updateAir();
            this.target = this.getTarget();
        }
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {

    }

    @Override
    public void onDeath(InfernalEntityDeathEvent event) {

    }

    private void updateAir() {

    }

}
