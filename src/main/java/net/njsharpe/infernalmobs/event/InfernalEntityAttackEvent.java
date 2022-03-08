package net.njsharpe.infernalmobs.event;

import net.njsharpe.infernalmobs.entity.InfernalEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class InfernalEntityAttackEvent extends EntityDamageByEntityEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final InfernalEntity damager;

    public InfernalEntityAttackEvent(@NotNull InfernalEntity damager, @NotNull Entity damagee, @NotNull EntityDamageEvent.DamageCause cause, double damage) {
        super(damager.getEntity(), damagee, cause, damage);
        this.damager = damager;
    }

    @NotNull
    public InfernalEntity getInfernalDamager() {
        return this.damager;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
