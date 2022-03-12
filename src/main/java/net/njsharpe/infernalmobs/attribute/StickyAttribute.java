package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import net.njsharpe.infernalmobs.util.EntityHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

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
        Item drop = this.drop(player, this.removeItem(player.getInventory(), player.getInventory().getHeldItemSlot(),
                1), false);
        if(drop != null) {
            drop.setPickupDelay(50);
            event.getInfernalEntity().playSound(Sound.ENTITY_SLIME_ATTACK);
        }
    }

    private Item drop(Player player, ItemStack item, boolean thrower) {
        if(this.isEmpty(item)) return null;
        double y = player.getEyeLocation().getY() - 0.30000001192092896D;
        Location pos = player.getLocation();
        Item entity = player.getWorld().dropItem(new Location(pos.getWorld(), pos.getX(), y, pos.getZ()), item);
        entity.setPickupDelay(40);
        if(thrower) entity.setOwner(player.getUniqueId());
        Random random = new Random();
        double sinX = Math.sin(EntityHelper.getXRot(entity) * 0.017453292F);
        double cosX = Math.cos(EntityHelper.getXRot(entity) * 0.017453292F);
        double sinY = Math.sin(EntityHelper.getYRot(entity) * 0.017453292F);
        double cosY = Math.cos(EntityHelper.getYRot(entity) * 0.017453292F);
        float rx = random.nextFloat() * 6.2831855F;
        float ry = random.nextFloat() * 0.02F;
        entity.setVelocity(new Vector((-sinY * cosX * 0.3F) + Math.cos(rx) * ry, -sinX * 0.3F + 0.1F +
                (random.nextFloat() - random.nextFloat()) * 0.1F, (cosY * cosX * 0.3F) + Math.sin(rx) * ry));
        return entity;
    }

    private ItemStack removeItem(PlayerInventory inventory, int slot, int amount) {
        ItemStack[][] compartments = new ItemStack[][] { inventory.getContents(), inventory.getArmorContents(),
                inventory.getExtraContents() };
        ItemStack[] contents = new ItemStack[0];
        for(ItemStack[] items : compartments) {
            if(slot < items.length) {
                contents = items;
                break;
            }
            slot -= items.length;
        }
        if(contents.length > 0 && !this.isEmpty(contents[slot])) {
            return this.removeItem(contents, slot, amount);
        }
        return new ItemStack(Material.AIR);
    }

    private ItemStack removeItem(ItemStack[] items, int index, int amount) {
        if(index < 0 || index >= items.length || this.isEmpty(items[index]) || amount <= 0) {
            return new ItemStack(Material.AIR);
        }
        return this.split(items[index], amount);
    }

    private ItemStack split(ItemStack item, int amount) {
        int min = Math.min(amount, item.getAmount());
        ItemStack copy = item.clone();
        copy.setAmount(min);
        item.setAmount(item.getAmount() - min);
        return copy;
    }

    private boolean isEmpty(ItemStack item) {
        return item == null || item.getType().isAir();
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
