package net.njsharpe.infernalmobs.attribute;

public interface Cooldown {

    long getCooldown();
    long getDeltaTime();

    default boolean canUseAbility() {
        return (System.currentTimeMillis() > this.getDeltaTime());
    }

}
