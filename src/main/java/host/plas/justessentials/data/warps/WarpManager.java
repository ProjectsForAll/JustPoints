package host.plas.justessentials.data.warps;

import host.plas.justessentials.JustEssentials;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class WarpManager {
    @Getter @Setter
    private static ConcurrentSkipListSet<Warp> loadedWarps = new ConcurrentSkipListSet<>();

    public static void loadWarp(Warp warp) {
        unloadWarp(warp.getIdentifier(), false, false);

        getLoadedWarps().add(warp);
    }

    public static void unloadWarp(String identifier, boolean save, boolean async) {
        getLoadedWarps().forEach(warp -> {
            if (warp.getIdentifier().equals(identifier)) {
                if (save) warp.save(async);

                getLoadedWarps().remove(warp);
            }
        });
    }

    public static void unloadWarp(String identifier) {
        unloadWarp(identifier, true, true);
    }

    public static void useWarp(Predicate<Warp> predicate, Consumer<Warp> consumer) {
        getLoadedWarps().stream().filter(predicate).forEach(consumer);
    }

    public static void useWarp(String identifier, Consumer<Warp> consumer) {
        useWarp(warp -> warp.getIdentifier().equals(identifier), consumer);
    }

    public static void save(String identifier, boolean async) {
        if (async) {
            CompletableFuture.runAsync(() -> saveThreaded(identifier));
        } else {
            saveThreaded(identifier);
        }
    }

    private static void saveThreaded(String identifier) {
        useWarp(identifier, warp -> JustEssentials.getWarpConfig().saveWarp(warp));
    }

    public static void flushWarps() {
        getLoadedWarps().clear();
    }

    public static void loadAllWarps(ConcurrentSkipListSet<Warp> warps) {
        warps.forEach(WarpManager::loadWarp);
    }

    public static void saveAllWarps(boolean async) {
        getLoadedWarps().forEach(warp -> warp.save(async));
    }

    public static void saveAllWarps() {
        saveAllWarps(true);
    }

    public static Warp setSpawn(Location location, boolean isMain) {
        if (isMain) {
            Warp warp = new Warp("spawn", location, WarpType.MAIN_SPAWN);
            updateOrSetMainSpawn(warp);

            return warp;
        } else {
            World world = location.getWorld();
            String worldName = world.getName();

            Warp warp = new Warp(worldName + "_spawn", location, WarpType.SPAWN);
            updateOrSetWorldSpawn(warp, worldName);

            return warp;
        }
    }

    public static Optional<Warp> getMainSpawn() {
        AtomicReference<Optional<Warp>> optional = new AtomicReference<>(Optional.empty());

        getLoadedWarps().forEach(warp -> {
            if (optional.get().isPresent()) return;

            if (warp.isMainSpawn()) {
                optional.set(Optional.of(warp));
            }
        });

        return optional.get();
    }

    public static void updateOrSetMainSpawn(Warp mainSpawn) {
        getMainSpawn().ifPresent(warp -> warp.unload(false, false));

        mainSpawn.load();
        mainSpawn.save();
    }

    public static Optional<Warp> getWorldSpawn(String worldName) {
        AtomicReference<Optional<Warp>> optional = new AtomicReference<>(Optional.empty());

        getLoadedWarps().forEach(warp -> {
            if (optional.get().isPresent()) return;

            if (warp.isSpawn() && warp.getIdentifier().equals(worldName + "_spawn")) {
                optional.set(Optional.of(warp));
            }
        });

        return optional.get();
    }

    public static void updateOrSetWorldSpawn(Warp spawn, String worldName) {
        getWorldSpawn(worldName).ifPresent(warp -> warp.unload(false, false));

        spawn.load();
        spawn.save();
    }

    public static Optional<Warp> getSpawn(Location location) {
        World world = location.getWorld();
        String worldName = world.getName();

        AtomicReference<Optional<Warp>> optional = new AtomicReference<>(Optional.empty());

        getLoadedWarps().forEach(warp -> {
            if (optional.get().isPresent()) return;

            if (warp.isSpawn() && warp.getIdentifier().equals(worldName + "_spawn")) {
                optional.set(Optional.of(warp));
            }
        });

        return optional.get();
    }
}
