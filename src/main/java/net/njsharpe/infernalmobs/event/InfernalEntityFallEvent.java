package net.njsharpe.infernalmobs.event;

import net.njsharpe.infernalmobs.entity.InfernalEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public class InfernalEntityFallEvent extends EntityEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final InfernalEntity entity;
    private final float distance;

    public InfernalEntityFallEvent(@NotNull InfernalEntity entity, float distance) {
        super(entity.getEntity());
        this.entity = entity;
        this.distance = distance;
    }

    @NotNull
    public InfernalEntity getInfernalEntity() {
        return this.entity;
    }

    @NotNull
    public float getDistance() {
        return this.distance;
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
