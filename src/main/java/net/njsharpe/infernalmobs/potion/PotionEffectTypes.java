package net.njsharpe.infernalmobs.potion;

import net.njsharpe.infernalmobs.util.ArrayHelper;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectTypes {

    public static final PotionEffectType[] DEBUFF = new PotionEffectType[] {
            PotionEffectType.BLINDNESS,
            PotionEffectType.CONFUSION,
            PotionEffectType.HARM,
            PotionEffectType.HUNGER,
            PotionEffectType.LEVITATION,
            PotionEffectType.POISON,
            PotionEffectType.SLOW,
            PotionEffectType.SLOW_DIGGING,
            PotionEffectType.WEAKNESS,
            PotionEffectType.WITHER
    };

    public static final PotionEffectType[] BUFF = new PotionEffectType[] {
            PotionEffectType.ABSORPTION,
            PotionEffectType.DAMAGE_RESISTANCE,
            PotionEffectType.DOLPHINS_GRACE,
            PotionEffectType.FAST_DIGGING,
            PotionEffectType.FIRE_RESISTANCE,
            PotionEffectType.HEAL,
            PotionEffectType.HEALTH_BOOST,
            PotionEffectType.INCREASE_DAMAGE,
            PotionEffectType.REGENERATION,
            PotionEffectType.SATURATION,
            PotionEffectType.SPEED,
            PotionEffectType.WATER_BREATHING
    };

    public static final PotionEffectType[] OTHER = new PotionEffectType[] {
            PotionEffectType.BAD_OMEN,
            PotionEffectType.GLOWING,
            PotionEffectType.HERO_OF_THE_VILLAGE,
            PotionEffectType.INVISIBILITY,
            PotionEffectType.JUMP,
            PotionEffectType.LUCK,
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.SLOW_FALLING,
            PotionEffectType.UNLUCK
    };

    public static final PotionEffectType[] ALL = ArrayHelper.concat(DEBUFF, BUFF, OTHER);

}
