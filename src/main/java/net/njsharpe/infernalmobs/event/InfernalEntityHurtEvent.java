package net.njsharpe.infernalmobs.event;

import net.njsharpe.infernalmobs.entity.InfernalEntity;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InfernalEntityHurtEvent extends EntityDamageEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final InfernalEntity damagee;
    private LivingEntity damager;

    public InfernalEntityHurtEvent(@NotNull InfernalEntity damagee, @Nullable LivingEntity damager, @NotNull EntityDamageEvent.DamageCause cause, double damage) {
        super(damagee.getEntity(), cause, damage);
        this.damagee = damagee;
        this.damager = damager;
    }

    @NotNull
    public InfernalEntity getInfernalEntity() {
        return this.damagee;
    }

    @Nullable
    public LivingEntity getDamager() {
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
