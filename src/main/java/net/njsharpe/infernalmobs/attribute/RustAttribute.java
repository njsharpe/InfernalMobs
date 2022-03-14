package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.developmentutility.helper.ItemHelper;
import net.njsharpe.developmentutility.item.DamageSource;
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
        ItemHelper.hurtAndBreak(hand, 4, event.getDamager(), (p) -> {
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
        ItemHelper.hurtArmor(player.getEquipment(), DamageSource.MAGIC, (float) (event.getDamage() * 3),
                new int[]{0, 1, 2, 3});
    }

}
