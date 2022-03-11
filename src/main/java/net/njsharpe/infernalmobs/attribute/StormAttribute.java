package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.util.EntityHelper;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StormAttribute extends Attribute implements Cooldown {

    private long deltaTime = 0L;

    public StormAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Storm";
    }

    @Override
    public boolean hasSpecial() {
        return true;
    }

    @Override
    public boolean conflictsWith(Attribute attribute) {
        return attribute.equals(Attribute.STICKY);
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
    public void useSpecial(InfernalEntity source, LivingEntity target) {
        super.useSpecial(source, target);

        LivingEntity entity = source.getEntity();
        if(target == null || target.getVehicle() != null || !entity.hasLineOfSight(target)) return;

        if(EntityHelper.distanceTo(entity, target) > 3.0F) {
            Location t = target.getLocation();
            Location pos = new Location(target.getWorld(), Math.floor(t.getX()), Math.floor(t.getY()),
                    Math.floor(t.getZ()));
            if(!this.canSeeSkyFromBelowWater(target.getWorld(), pos)) return;
            target.getWorld().strikeLightning(pos);
        }
    }

    private boolean canSeeSkyFromBelowWater(World world, Location location) {
        if(location.getY() >= world.getSeaLevel()) return this.canSeeSky(world, location);
        Location pos = new Location(world, location.getX(), world.getSeaLevel(), location.getZ());
        if(!this.canSeeSky(world, pos)) return false;
        pos = pos.add(0, -1, 0);
        while(pos.getY() > location.getY()) {
            BlockState state = world.getBlockState(pos);
            if(state.getLightLevel() > 0 && !state.getBlock().isLiquid()) return false;
            pos = pos.add(0, -1, 0);
        }
        return true;
    }

    private boolean canSeeSky(World world, Location location) {
        return world.hasSkyLight() && location.getBlock().getLightFromSky() >= 15;
    }

    @Override
    public long getCooldown() {
        return 25000L;
    }

    @Override
    public long getDeltaTime() {
        return this.deltaTime;
    }

}
