package net.njsharpe.infernalmobs.event;

import net.njsharpe.infernalmobs.entity.InfernalEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class InfernalEntityDeathEvent extends EntityDeathEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final InfernalEntity entity;

    public InfernalEntityDeathEvent(final InfernalEntity entity, final List<ItemStack> drops) {
        this(entity, drops, 0);
    }

    public InfernalEntityDeathEvent(final InfernalEntity entity, final List<ItemStack> drops, final int droppedExp) {
        super(entity.getEntity(), drops, droppedExp);
        this.entity = entity;
    }

    @NotNull
    public InfernalEntity getInfernalEntity() {
        return this.entity;
    }

    public int getDroppedExp() {
        return super.getDroppedExp() * 4;
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