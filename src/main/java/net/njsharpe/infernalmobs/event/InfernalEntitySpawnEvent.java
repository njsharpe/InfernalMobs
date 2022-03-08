package net.njsharpe.infernalmobs.event;

import net.njsharpe.infernalmobs.entity.InfernalEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.jetbrains.annotations.NotNull;

public class InfernalEntitySpawnEvent extends EntitySpawnEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final InfernalEntity entity;

    public InfernalEntitySpawnEvent(InfernalEntity entity) {
        super(entity.getEntity());
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
