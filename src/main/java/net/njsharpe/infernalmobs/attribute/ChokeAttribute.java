package net.njsharpe.infernalmobs.attribute;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.event.InfernalEntityDeathEvent;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class ChokeAttribute extends Attribute {

    private static final int RESET_AIR_VALUE = -999;
    private static final int MAXIMUM_AIR = 300;

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
            if(this.target != null) this.send(this.air);
            this.target = this.getTarget();
        }
        if(!(this.target instanceof Player)) return;
        Player player = (Player) this.target;
        if(entity.getEntity().hasLineOfSight(player)) {
            if(this.air == RESET_AIR_VALUE) {
                this.air = player.getRemainingAir();
            } else {
                this.air = Math.min(this.air, player.getRemainingAir());
            }

            if(!player.isInvulnerable()) {
                this.air--;
                if(this.air < -19) {
                    this.air = 0;
                    player.damage(2.0F);
                }

                this.send(this.air);
            }
        }
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        if(this.target != null && event.getDamager() == this.target && this.air != RESET_AIR_VALUE) {
            this.air += 60;
            if(this.air > MAXIMUM_AIR) {
                this.air = MAXIMUM_AIR;
            }
            this.send(this.air);
        }
    }

    @Override
    public void onDeath(InfernalEntityDeathEvent event) {
        this.air = MAXIMUM_AIR;
        if(this.target != null) {
            this.send(this.air);
            this.target = null;
        }
    }

    private void send(int ticks) {
        if(!(this.target instanceof Player)) return;
        Player player = (Player) this.target;
        PacketContainer handle = ProtocolLibrary.getProtocolManager()
                .createPacket(PacketType.Play.Server.ENTITY_METADATA);
        WrappedDataWatcher watcher = new WrappedDataWatcher(player);
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(1,
                WrappedDataWatcher.Registry.get(Integer.class)), ticks);
        handle.getEntityModifier(player.getWorld()).write(0, player);
        handle.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, handle);
            player.setRemainingAir(ticks);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException("Could not send packet.", ex);
        }
    }

}
