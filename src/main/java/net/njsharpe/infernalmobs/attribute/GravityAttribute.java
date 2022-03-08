package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.infernalmobs.InfernalMobs;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class GravityAttribute extends Attribute {

    public GravityAttribute(@NotNull String name) {
        super(new NamespacedKey(InfernalMobs.get().orElseThrow(IllegalArgumentException::new), name));
    }

    @NotNull
    @Override
    public String getName() {
        return "Gravity";
    }

    @Override
    public boolean hasSpecial() {
        return true;
    }

    @Override
    public boolean conflictsWith(Attribute attribute) {
//        return attribute.equals(Attribute.WEBBER);
        return false;
    }

    @Override
    public void useSpecial(InfernalEntity source, LivingEntity target) {
        super.useSpecial(source, target);
        Random random = new Random();
        float chance = random.nextFloat();
        if(chance >= 0.5F) {
            Vector velocity = target.getVelocity();
            target.setVelocity(velocity.setY(velocity.getY() * 1.7D).setX(velocity.getX() * 1.3D));
            source.playSound(Sound.ENTITY_IRON_GOLEM_ATTACK);
        }
    }

}
