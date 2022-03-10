package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.event.InfernalEntityAttackEvent;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Consumer;

public class RustAttribute extends Attribute {

    public RustAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Rust";
    }

    @Override
    public boolean hasSpecial() {
        return false;
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        super.onHurt(event);
        if(!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        ItemStack hand = player.getInventory().getItemInMainHand();
        this.hurtAndBreak(hand, 4, event.getDamager(), (p) -> {
            PlayerItemBreakEvent e = new PlayerItemBreakEvent(player, hand);
            Bukkit.getServer().getPluginManager().callEvent(e);
        });
    }

    @Override
    public void onAttack(InfernalEntityAttackEvent event) {
        super.onAttack(event);
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        // No Armor to Damage
        if(player.getEquipment() == null) return;
        this.hurtArmor(player.getEquipment(), EntityDamageEvent.DamageCause.MAGIC, (float) (event.getDamage() * 3),
                new int[]{0, 1, 2, 3});
    }

    private boolean hurt(ItemStack item, int n, Random random) {
        int i;
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return false;
        if(meta.isUnbreakable()) return false;
        if(n > 0) {
            i = meta.getEnchantLevel(Enchantment.DURABILITY);
            int j = 0;
            for(int k = 0; i > 0 && k < n; k++) {
                if(!(random.nextInt(n + 1) > 0)) continue;
                j++;
            }
            if((n -= j) <= 0) return false;
        }
        Damageable damageable = (Damageable) meta;
        i = damageable.getDamage() + n;
        damageable.setDamage(i);
        return i >= item.getType().getMaxDurability();
    }

    private <T extends LivingEntity> void hurtAndBreak(ItemStack item, int n, T t, Consumer<T> consumer) {
        ItemMeta meta = item.getItemMeta();
        if(meta != null && meta.isUnbreakable()) return;
        Damageable damageable = (Damageable) meta;
        if(this.hurt(item, n, new Random())) {
            consumer.accept(t);
            item.setAmount(item.getAmount() - 1);
            if(t instanceof Player) ((Player) t).incrementStatistic(Statistic.BREAK_ITEM, item.getType());
        }
        damageable.setDamage(0);
    }

    private void hurtArmor(EntityEquipment inventory, EntityDamageEvent.DamageCause cause, float f, int[] array) {
        if(!(inventory.getHolder() instanceof Player)) return;
        Player player = (Player) inventory.getHolder();
        if(f <= 0.0F) return;
        if((f /= 4.0F) < 1.0F) f = 1.0F;
        for(int n : array) {
            ItemStack item = inventory.getArmorContents()[n];
            if(item == null) continue;
            if(this.isFire(cause) && this.isFireResistant(item)) continue;
            this.hurtAndBreak(item, (int) f, player, (p) -> {
                PlayerItemBreakEvent e = new PlayerItemBreakEvent(player, item);
                Bukkit.getServer().getPluginManager().callEvent(e);
            });
        }
    }

    private boolean isFire(EntityDamageEvent.DamageCause cause) {
        return cause.equals(EntityDamageEvent.DamageCause.FIRE) || cause.equals(EntityDamageEvent.DamageCause.FIRE_TICK);
    }

    private boolean isFireResistant(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return false;
        return meta.hasEnchant(Enchantment.PROTECTION_FIRE);
    }

}
