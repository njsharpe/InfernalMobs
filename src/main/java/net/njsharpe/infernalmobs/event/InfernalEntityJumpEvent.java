package net.njsharpe.infernalmobs.event;

import net.njsharpe.infernalmobs.entity.InfernalEntity;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public class InfernalEntityJumpEvent extends EntityEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final InfernalEntity entity;
    private final Location from;
    private final Location to;

    public InfernalEntityJumpEvent(@NotNull InfernalEntity entity, @NotNull Location from, @NotNull Location to) {
        super(entity.getEntity());
        this.entity = entity;
        this.from = from;
        this.to = to;
    }

    @NotNull
    public InfernalEntity getInfernalEntity() {
        return this.entity;
    }

    @NotNull
    public Location getFrom() {
        return this.from;
    }

    @NotNull
    public Location getTo() {
        return this.to;
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
