package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.util.EntityHelper;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class SprintAttribute extends Attribute implements Cooldown {

    private long deltaTime = 0L;
    private boolean sprinting;
    private double x;
    private double z;

    public SprintAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Sprint";
    }

    @Override
    public boolean hasSpecial() {
        return false;
    }

    @Override
    public void onUpdate(InfernalEntity entity) {
        super.onUpdate(entity);
        if(!this.hasTarget()) return;
        long time = System.currentTimeMillis();
        if(!this.canUseAbility()) return;
        this.deltaTime = time + this.getCooldown();
        this.sprinting = !this.sprinting;
        if(this.sprinting) this.sprint(entity);
    }

    public void sprint(InfernalEntity entity) {
        LivingEntity e = entity.getEntity();
        float rotMove = (float)((Math.atan2(e.getVelocity().getX(), e.getVelocity().getZ()) * 180D) / 3.1415D);
        float rotLook = EntityHelper.getYRot(e);

        if(rotLook > 360F) {
            rotLook -= (rotLook % 360F) * 360F;
        } else if(rotLook < 0F) {
            rotLook += ((rotLook * -1) % 360F) * 360F;
        }

        if(Math.abs(rotMove + rotLook) > 10F) {
            rotLook -= 360F;
        }

        double speed = this.getAbsSpeed(e);

        if(Math.abs(rotMove + rotLook) > 10F) {
            this.x = e.getVelocity().getX();
            this.z = e.getVelocity().getZ();
        }

        if(speed < 0.3D) {
            if(this.getAbsSpeed() > 0.6D || !(e.isOnGround())) {
                this.x /= 1.55D;
                this.z /= 1.55D;
            }

            this.x *= 1.5D;
            this.z *= 1.5D;
            e.setVelocity(new Vector(this.x, e.getVelocity().getY(), this.z));
        }
    }

    private double getAbsSpeed(LivingEntity entity) {
        Vector velocity = entity.getVelocity();
        return Math.sqrt(velocity.getX() * velocity.getX() + velocity.getZ() * velocity.getZ());
    }

    private double getAbsSpeed() {
        return Math.sqrt(this.x * this.x + this.z * this.z);
    }

    @Override
    public long getCooldown() {
        return 5000L;
    }

    @Override
    public long getDeltaTime() {
        return this.deltaTime;
    }

}
