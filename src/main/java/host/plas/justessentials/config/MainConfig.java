package host.plas.justessentials.config;

import host.plas.justessentials.JustEssentials;
import org.bukkit.potion.PotionEffectType;
import tv.quaint.storage.resources.flat.simple.SimpleConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MainConfig extends SimpleConfiguration {
    public MainConfig() {
        super("config.yml", JustEssentials.getInstance(), true);
    }

    @Override
    public void init() {
        isSpawnOnJoin();
        isSpawnMainSpawnByDefault();

        isFlightPutOnGround();
        isFlightCheckOnJoin();
        isFlightCheckOnMove();
        getFlightCheckPermissions();

        getHealBadPotionEffects();

        getFeedBadPotionEffects();
    }

    public boolean isSpawnOnJoin() {
        reloadResource();

        return getOrSetDefault("spawn.on-join", false);
    }

    public boolean isSpawnMainSpawnByDefault() {
        reloadResource();

        return getOrSetDefault("spawn.command.main-by-default", true);
    }

    public boolean isFlightPutOnGround() {
        reloadResource();

        return getOrSetDefault("flight.toggle.put-on-ground", true);
    }

    public boolean isFlightCheckOnJoin() {
        reloadResource();

        return getOrSetDefault("flight.toggle.check-on-join", true);
    }

    public boolean isFlightCheckOnLeave() {
        reloadResource();

        return getOrSetDefault("flight.toggle.check-on-leave", false);
    }

    public boolean isFlightCheckOnMove() {
        reloadResource();

        return getOrSetDefault("flight.toggle.check-on-move", false);
    }

    public List<String> getFlightCheckPermissions() {
        reloadResource();

        return getOrSetDefault("flight.permissions", new ArrayList<>(List.of("justessentials.command.fly")));
    }

    public List<PotionEffectType> getHealBadPotionEffects() {
        reloadResource();

        List<PotionEffectType> potionEffects = new ArrayList<>();

        for (String effect : getOrSetDefault("heal.bad-effects", new ArrayList<>(List.of("CONFUSION", "BLINDNESS", "HUNGER")))) {
            try {
                potionEffects.add(PotionEffectType.getByName(effect));
            } catch (Exception e) {
                JustEssentials.getInstance().logWarning("Invalid potion effect: " + effect);
            }
        }

        return potionEffects;
    }

    public List<PotionEffectType> getFeedBadPotionEffects() {
        reloadResource();

        List<PotionEffectType> potionEffects = new ArrayList<>();

        for (String effect : getOrSetDefault("feed.bad-effects", new ArrayList<>(List.of("HUNGER")))) {
            try {
                potionEffects.add(PotionEffectType.getByName(effect));
            } catch (Exception e) {
                JustEssentials.getInstance().logWarning("Invalid potion effect: " + effect);
            }
        }

        return potionEffects;
    }
}
