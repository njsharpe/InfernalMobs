package net.njsharpe.infernalmobs.event;

import net.njsharpe.infernalmobs.entity.InfernalEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InfernalEntityTargetEvent extends EntityTargetLivingEntityEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final InfernalEntity entity;

    public InfernalEntityTargetEvent(@NotNull InfernalEntity entity, @NotNull LivingEntity target, @Nullable EntityTargetEvent.TargetReason reason) {
        super(entity.getEntity(), target, reason);
        this.entity = entity;
    }

    @NotNull
    public InfernalEntity getInfernalEntity() {
        return this.entity;
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
