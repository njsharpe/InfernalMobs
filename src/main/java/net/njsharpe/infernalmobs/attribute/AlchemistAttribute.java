package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.Direction;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.util.EntityHelper;
import net.njsharpe.infernalmobs.util.ProjectileHelper;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class AlchemistAttribute extends Attribute implements Cooldown {

    private long deltaTime = 0L;

    public AlchemistAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Alchemist";
    }

    @Override
    public boolean hasSpecial() {
        return true;
    }

    @Override
    public void onUpdate(InfernalEntity entity) {
        long time = System.currentTimeMillis();
        if(!this.canUseAbility()) return;
        this.deltaTime = time + this.getCooldown();
        this.useSpecial(entity, EntityHelper.getNearestPlayer(entity.getWorld(), entity.getEntity(), 12.0F));
    }

    @Override
    public void useSpecial(InfernalEntity source, LivingEntity target) {
        super.useSpecial(source, target);

        if(target == null || !source.getEntity().hasLineOfSight(target)) return;

        if(EntityHelper.distanceToSqr(source.getEntity(), target) > 2.0F) {
            Direction direction = EntityHelper.getDirectionBetween(source.getEntity(), target);

            Random random = new Random();
            PotionEffectType type = PotionEffectType.HARM;
            int duration = 0;
            int amplifier = 0;

            ItemStack item = new ItemStack(Material.SPLASH_POTION);
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            if(meta == null) throw new IllegalStateException("PotionMeta cannot be null?");

            if(direction.getDistance() >= 8.0F && !target.hasPotionEffect(PotionEffectType.SLOW)) {
                type = PotionEffectType.SLOW;
                duration = 1800;
            } else if(target.getHealth() >= 8.0F && !target.hasPotionEffect(PotionEffectType.POISON)) {
                type = PotionEffectType.POISON;
                duration = 900;
            } else if(direction.getDistance() <= 3.0F && !target.hasPotionEffect(PotionEffectType.WEAKNESS)
                    && random.nextFloat() < 0.25F) {
                type = PotionEffectType.WEAKNESS;
                duration = 1800;
            }

            meta.addCustomEffect(type.createEffect(duration, amplifier), true);
            item.setItemMeta(meta);

            ThrownPotion potion = source.getEntity().launchProjectile(ThrownPotion.class);
            potion.setItem(item);

            EntityHelper.setYRot(potion, EntityHelper.getXRot(potion) + 20.0F);
            ProjectileHelper.shoot(potion, random, direction.getDeltaX(), direction.getDeltaY() +
                    (double)(direction.getDistance() * 0.2F), direction.getDeltaZ(), 0.75F, 8.0F);
        }
    }

    @Override
    public long getCooldown() {
        return 6000L;
    }

    @Override
    public long getDeltaTime() {
        return this.deltaTime;
    }

}
