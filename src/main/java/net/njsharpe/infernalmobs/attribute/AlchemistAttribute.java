package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.util.EntityHelper;
import net.njsharpe.infernalmobs.util.ProjectileHelper;
import org.bukkit.Location;
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
        if(!this.hasTarget()) return;
        long time = System.currentTimeMillis();
        if(!this.canUseAbility()) return;
        this.deltaTime = time + this.getCooldown();
        this.useSpecial(entity, EntityHelper.getNearestPlayer(entity.getWorld(), entity.getEntity(), 12.0F));
    }

    @Override
    public void useSpecial(InfernalEntity source, LivingEntity target) {
        super.useSpecial(source, target);

        LivingEntity entity = source.getEntity();
        if(target == null || !entity.hasLineOfSight(target)) return;

        if(EntityHelper.distanceToSqr(entity, target) > 2.0F) {
            Location s = source.getLocation();
            Location t = target.getLocation();
            float x = (float) (t.getX() + t.getDirection().getX() - s.getX());
            float y = (float) (t.getY() + target.getEyeHeight() - 1.100000023841858D - s.getY());
            float z = (float) (t.getZ() + t.getDirection().getZ() - s.getZ());
            float distance = (float) Math.sqrt(x * x + z * z);

            Random random = new Random();
            PotionEffectType type = PotionEffectType.HARM;
            int duration = 0;
            int amplifier = 0;

            ItemStack item = new ItemStack(Material.SPLASH_POTION);
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            if(meta == null) throw new IllegalStateException("PotionMeta cannot be null?");

            if(distance >= 8.0F && !target.hasPotionEffect(PotionEffectType.SLOW)) {
                type = PotionEffectType.SLOW;
                duration = 1800;
            } else if(target.getHealth() >= 8.0F && !target.hasPotionEffect(PotionEffectType.POISON)) {
                type = PotionEffectType.POISON;
                duration = 900;
            } else if(distance <= 3.0F && !target.hasPotionEffect(PotionEffectType.WEAKNESS)
                    && random.nextFloat() < 0.25F) {
                type = PotionEffectType.WEAKNESS;
                duration = 1800;
            }

            meta.addCustomEffect(type.createEffect(duration, amplifier), true);
            item.setItemMeta(meta);

            ThrownPotion potion = entity.launchProjectile(ThrownPotion.class);
            potion.setItem(item);

            EntityHelper.setYRot(potion, EntityHelper.getXRot(potion) + 20.0F);
            ProjectileHelper.shoot(potion, random, x, y + (double)(distance * 0.2F), z, 0.75F, 8.0F);
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
