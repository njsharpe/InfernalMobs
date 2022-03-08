package net.njsharpe.infernalmobs.entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import net.njsharpe.infernalmobs.InfernalMobs;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AirTickPacketWrapper extends BukkitRunnable {

    private static final Map<UUID, AirTickPacketWrapper> byId = new HashMap<>();

    private final Player target;
    private int ticks;
    private int damageId;

    public AirTickPacketWrapper(@NotNull Player target) {
        this.target = target;
        this.damageId = -1;
        byId.put(target.getUniqueId(), this);
        this.runTaskTimer(this.getPlugin(), 0L, 1L);
    }

    public Player getTarget() {
        return this.target;
    }

    public int getTicks() {
        return this.ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public Plugin getPlugin() {
        return InfernalMobs.get().orElseThrow(IllegalStateException::new);
    }

    @Override
    @Deprecated
    protected void finalize() throws Throwable {
        this.destroy();
    }

    public void destroy() {
        byId.remove(this.target.getUniqueId());
        if(!this.isCancelled()) this.cancel();
    }

    @Override
    public void run() {
        this.send(this.ticks);
        this.ticks--;
        if(!this.target.isOnline()) {
            this.cancel();
            this.destroy();
            return;
        }
        if(this.ticks <= 0) {
            if(this.damageId != -1) return;
            this.damageId = new BukkitRunnable() {
                @Override
                public void run() {
                    if(target.isDead()) return;
                    target.damage(1.0D);
                    damageId = -1;
                }
            }.runTaskLater(this.getPlugin(), 20L).getTaskId();
        }
    }

    private void send(int ticks) {
        PacketContainer handle = ProtocolLibrary.getProtocolManager()
                .createPacket(PacketType.Play.Server.ENTITY_METADATA);
        WrappedDataWatcher watcher = new WrappedDataWatcher(this.target);
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(1,
                WrappedDataWatcher.Registry.get(Integer.class)), ticks);
        handle.getEntityModifier(this.target.getWorld()).write(0, this.target);
        handle.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(this.target, handle);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException("Could not send packet.", ex);
        }
    }

    public static AirTickPacketWrapper from(@NotNull Player target) {
        if(byId.containsKey(target.getUniqueId())) return byId.get(target.getUniqueId());
        return new AirTickPacketWrapper(target);
    }

}
