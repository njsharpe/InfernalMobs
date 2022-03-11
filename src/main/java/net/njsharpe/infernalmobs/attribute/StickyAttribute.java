package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
//        return attribute.equals(Attribute.STORM);
        return false;
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        super.onHurt(event);
        if(!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();
        long time = System.currentTimeMillis();
        if(!this.canUseAbility()) return;
        this.deltaTime = time + this.getCooldown();
        if(player.dropItem(false)) {
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
