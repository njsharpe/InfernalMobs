package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.event.InfernalEntityHurtEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WebberAttribute extends Attribute implements Cooldown {

    private long deltaTime = 0L;

    public WebberAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Webber";
    }

    @Override
    public boolean hasSpecial() {
        return true;
    }

    @Override
    public boolean conflictsWith(Attribute attribute) {
        return attribute.equals(Attribute.GRAVITY) || attribute.equals(Attribute.BLASTOFF);
    }

    @Override
    public void onUpdate(InfernalEntity entity) {
        super.onUpdate(entity);
        if(!this.hasTarget() || !(this.getTarget() instanceof Player)) return;
        long time = System.currentTimeMillis();
        if(!this.canUseAbility()) return;
        this.deltaTime = time + this.getCooldown();
        this.useSpecial(entity, this.getTarget());
    }

    @Override
    public void onHurt(InfernalEntityHurtEvent event) {
        super.onHurt(event);
       if(event.getDamager() == null) return;
       this.useSpecial(event.getInfernalEntity(), event.getDamager());
    }

    @Override
    public void useSpecial(InfernalEntity source, LivingEntity target) {
        super.useSpecial(source, target);

        LivingEntity entity = source.getEntity();
        if(target == null || !entity.hasLineOfSight(target)) return;

        Location t = target.getLocation();
        int x = t.getBlockX();
        int y = t.getBlockY();
        int z = t.getBlockZ();

        int offset;
        if(target.getWorld().getBlockAt(x, y - 1, z).getType().isAir()) {
            offset = -1;
        } else if(target.getWorld().getBlockAt(x, y, z).getType().isAir()) {
            offset = 0;
        } else {
            return;
        }

        target.getWorld().getBlockAt(x, y + offset, z).setType(Material.COBWEB);
        source.playSound(Sound.ENTITY_SPIDER_AMBIENT);
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
