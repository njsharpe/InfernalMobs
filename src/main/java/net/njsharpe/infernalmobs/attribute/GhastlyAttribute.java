package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class GhastlyAttribute extends Attribute {

    public GhastlyAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Ghastly";
    }

    @Override
    public boolean hasSpecial() {
        return true;
    }

    @Override
    public void useSpecial(InfernalEntity source, LivingEntity target) {
        super.useSpecial(source, target);
        Random random = new Random();
        float chance = random.nextFloat();
        if(chance >= 0.5F) {
            Fireball fireball = source.getEntity().launchProjectile(Fireball.class);
            Vector direction = target.getBoundingBox().getCenter()
                    .subtract(source.getEntity().getBoundingBox().getCenter())
                    .normalize();
            fireball.setDirection(direction);
        }
    }

}
