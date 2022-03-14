package net.njsharpe.infernalmobs.attribute;

import net.njsharpe.developmentutility.helper.EntityHelper;
import net.njsharpe.infernalmobs.entity.InfernalEntity;
import net.njsharpe.infernalmobs.event.*;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class Attribute implements Keyed {

    public static final Attribute ONE_UP = new OneUpAttribute("one_up");
    public static final Attribute ALCHEMIST = new AlchemistAttribute("alchemist");
    public static final Attribute BERSERK = new BerserkAttribute("berserk");
    public static final Attribute BLASTOFF = new BlastoffAttribute("blastoff");
    public static final Attribute BULWARK = new BulwarkAttribute("bulwark");
    public static final Attribute CHOKE = new ChokeAttribute("choke");
    public static final Attribute CLOAKING = new CloakingAttribute("cloaking");
    public static final Attribute DARKNESS = new DarknessAttribute("darkness");
    public static final Attribute ENDER = new EnderAttribute("ender");
    public static final Attribute EXHAUST = new ExhaustAttribute("exhaust");
    public static final Attribute FIERY = new FieryAttribute("fiery");
    public static final Attribute GHASTLY = new GhastlyAttribute("ghastly");
    public static final Attribute GRAVITY = new GravityAttribute("gravity");
    public static final Attribute LIFESTEAL = new LifestealAttribute("lifesteal");
    public static final Attribute NINJA = new NinjaAttribute("ninja");
    public static final Attribute POISONOUS = new PoisonousAttribute("poisonous");
    public static final Attribute QUICKSAND = new QuicksandAttribute("quicksand");
    public static final Attribute REGEN = new RegenAttribute("regen");
    public static final Attribute RUST = new RustAttribute("rust");
    public static final Attribute SAPPER = new SapperAttribute("sapper");
    public static final Attribute SPRINT = new SprintAttribute("sprint");
    public static final Attribute STICKY = new StickyAttribute("sticky");
    public static final Attribute STORM = new StormAttribute("storm");
    public static final Attribute VENGEANCE = new VengeanceAttribute("vengeance");
    public static final Attribute WEAKNESS = new WeaknessAttribute("weakness");
    public static final Attribute WEBBER = new WebberAttribute("webber");
    public static final Attribute WITHER = new WitherAttribute("wither");

    private static final int TICKS_BEFORE_ATTACK = 30;

    private static final Map<NamespacedKey, Attribute> byKey = new HashMap<>();
    private static boolean acceptingNew = true;
    private final NamespacedKey key;

    private LivingEntity previous;
    private LivingEntity target;

    private int ticks = 0;

    public Attribute(@NotNull NamespacedKey key) {
        this.key = key;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @NotNull
    public abstract String getName();

    @NotNull
    public EntityType[] getBlacklist() {
        return new EntityType[0];
    }

    public boolean conflictsWith(Attribute attribute) {
        return false;
    }

    public void onSpawn(InfernalEntitySpawnEvent event) {}
    public void onUpdate(InfernalEntity entity) {
        if(this.target == null) {
            this.target = EntityHelper.getNearestPlayer(entity.getWorld(), entity.getEntity(), 7.5F);
        }
        if(this.target != null) {
            if(this.target.isDead() || EntityHelper.distanceTo(this.target, entity.getEntity()) > 15.0D) {
                this.target = null;
            }
        }
        KeyedBossBar bar = entity.getOrCreateBossBar();
        bar.setProgress(entity.getHealth() / entity.getMaxHealth());
    }
    public void onHurt(InfernalEntityHurtEvent event) {}
    public void onAttack(InfernalEntityAttackEvent event) {}
    public void onTarget(InfernalEntityTargetEvent event) {
        this.previous = this.target;
        this.target = event.getTarget();
        if(this.previous != this.target) this.ticks = 0;
    }
    public void onFall(InfernalEntityFallEvent event) {}
    public void onJump(InfernalEntityJumpEvent event) {}
    public void onDeath(InfernalEntityDeathEvent event) {}
    public abstract boolean hasSpecial();
    public void useSpecial(InfernalEntity source, LivingEntity target) {
        if(!hasSpecial()) return;
    }

    public boolean hasTarget() {
        if(this.target != null) {
            this.ticks++;
            return this.ticks > TICKS_BEFORE_ATTACK;
        }
        return false;
    }

    public LivingEntity getTarget() {
        return this.target;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Attribute)) {
            return false;
        }
        final Attribute other = (Attribute) obj;
        return this.key.equals(other.key);
    }

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }

    @Override
    public String toString() {
        return "Attribute[" + this.key + "]";
    }

    public static void registerAttribute(@NotNull Attribute attribute) {
        if (byKey.containsKey(attribute.key)) {
            throw new IllegalArgumentException("Cannot set already-set attribute");
        } else if (!isAcceptingRegistrations()) {
            throw new IllegalStateException("No longer accepting new attributes (can only be done by the server implementation)");
        }

        byKey.put(attribute.key, attribute);
    }

    public static boolean isAcceptingRegistrations() {
        return acceptingNew;
    }

    public static void stopAcceptingRegistrations() {
        acceptingNew = false;
    }

    @Contract("null -> null")
    @Nullable
    public static Attribute getByKey(@Nullable NamespacedKey key) {
        return byKey.get(key);
    }

    @NotNull
    public static Attribute[] values() {
        return byKey.values().toArray(new Attribute[0]);
    }

}
