package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.developmentutility.helper.ItemHelper;
import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StickyAttribute extends Attribute implements Cooldown {

    private long deltaTime = 0L;

    public StickyAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Sticky";
    }

    @Override
    public boolean hasSpecial() {
        return false;
    }

    @Override
    public @NotNull EntityType[] getBlacklist() {
        return new EntityType[] { EntityType.CREEPER };
    }

    @Override
    public boolean conflictsWith(Attribute attribute) {
        return attribute.equals(Attribute.STORM);
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        super.onHurt(event);
        if(!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        long time = System.currentTimeMillis();
        if(!this.canUseAbility()) return;
        this.deltaTime = time + this.getCooldown();
        Item drop = ItemHelper.drop(player, ItemHelper.removeItem(player.getInventory(),
                player.getInventory().getHeldItemSlot(), 1), false);
        if(drop != null) {
            drop.setPickupDelay(50);
            event.getInfernalEntity().playSound(Sound.ENTITY_SLIME_ATTACK);
        }
    }

    @Override
    public long getCooldown() {
        return 15000L;
    }

    @Override
    public long getDeltaTime() {
        return this.deltaTime;
    }

}
