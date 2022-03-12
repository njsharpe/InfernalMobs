package net.njsharpe.infernalmobs.command;

import net.njsharpe.developmentutility.AdvancedCommandExecutor;
import net.njsharpe.developmentutility.CommandTree;
import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.file.ConfigurationFile;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class InfernalMobsCommand extends AdvancedCommandExecutor {

    public InfernalMobsCommand() {
        super(InfernalMobs.get().orElseThrow(IllegalStateException::new), "infernalmobs");
        CommandTree tree = this.getTree();
        tree.addCommand("reload", (sender) -> {
            ConfigurationFile config = ConfigurationFile.get();
            config.reload();
            config.save();
            sender.sendMessage("Reloaded!");
            return true;
        }, (sender) -> {});
        tree.addCommand("god", (sender) -> {
            if(!(sender instanceof Player)) return true;
            Player player = (Player) sender;
            player.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(99999, 255));
            player.addPotionEffect(PotionEffectType.SATURATION.createEffect(99999, 255));
            player.addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(99999, 255));
            player.setAllowFlight(true);
            player.setFlying(true);
            ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);
            meta.setUnbreakable(true);
            item.setItemMeta(meta);
            player.getInventory().addItem(item);
            return true;
        }, (sender) -> {});
    }

}
