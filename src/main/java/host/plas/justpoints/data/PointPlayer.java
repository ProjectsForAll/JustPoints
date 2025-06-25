package host.plas.justpoints.data;

import gg.drak.thebase.async.AsyncUtils;
import gg.drak.thebase.objects.Identifiable;
import host.plas.justpoints.JustPoints;
import host.plas.justpoints.managers.PointsManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;

@Getter @Setter
public class PointPlayer implements Identifiable {
    private String identifier;

    private String username;
    private ConcurrentSkipListMap<String, Double> points;

    private long lastEditedMillis;

    private boolean loadedAtLeastOnce;

    private boolean fullyLoaded = false;

    public PointPlayer(String identifier, String username, ConcurrentSkipListMap<String, Double> points) {
        this.identifier = identifier;
        this.username = username;
        this.points = points;
        this.loadedAtLeastOnce = false;
    }

    public PointPlayer(String uuid, String username) {
        this(uuid, username, new ConcurrentSkipListMap<>());
    }

    public PointPlayer(String uuid) {
        this(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
    }

    public PointPlayer(OfflinePlayer player) {
        this(player.getUniqueId().toString(), player.getName());
    }

    public PointPlayer(OfflinePlayer player, ConcurrentSkipListMap<String, Double> points) {
        this(player.getUniqueId().toString(), player.getName(), points);
    }

    public double getPoints(String type) {
        return points.getOrDefault(type, 0.0);
    }

    public void addPoints(String type, double points) {
        double currentPoints = getPoints(type);
        currentPoints += points;

        this.points.put(type, currentPoints);
    }

    public void removePoints(String type, double points) {
        double currentPoints = getPoints(type);
        currentPoints -= points;

        this.points.put(type, currentPoints);
    }

    public void setPointsSpecific(String type, double points) {
        this.points.put(type, points);
    }

    public void saveAndUnload() {
        PointsManager.unloadPlayer(getIdentifier(), true);
    }

    public void save() {
        JustPoints.getMainDatabase().savePlayer(this);
        setLastEditedMillis(System.currentTimeMillis());
    }

    public PointPlayer augment(CompletableFuture<Optional<PointPlayer>> future, boolean isGet) {
        this.fullyLoaded = false;

        future.whenComplete( (result, exception) -> {
            if (exception != null) {
                JustPoints.getInstance().logSevere("Error augmenting player data for " + getIdentifier(), exception);
                this.fullyLoaded = true;
                return;
            }

            if (result.isPresent()) {
                PointPlayer player = result.get();

                if (loadedAtLeastOnce) {
                    player.getPoints().forEach((key, value) -> {
                        this.points.compute(key, (k, current) -> value);
                    });
                } else {
                    player.getPoints().forEach((key, value) -> {
                        this.points.compute(key, (k, current) -> {
                            if (current == null) return value;
                            return current + value;
                        });
                    });
                }

                this.username = player.getUsername();
                this.lastEditedMillis = player.getLastEditedMillis();

                this.loadedAtLeastOnce = true;
            } else {
                if (! isGet) {
                    save();
                }
            }

            this.fullyLoaded = true;
        });

        return this;
    }

    public void load() {
        PointsManager.loadPlayer(this);
    }

    public void unload() {
        PointsManager.unloadPlayer(getIdentifier(), false);
    }

    public void reset(String key) {
        points.remove(key);

        JustPoints.getMainDatabase().resetPoints(key, this);

        save();
    }

    public void action(Consumer<PointPlayer> action) {
        action.accept(this);
    }

    public PointPlayer waitUntilFullyLoaded() {
        while (! this.fullyLoaded) {
            Thread.onSpinWait();
        }

        return this;
    }

    public void onceLoaded(Consumer<PointPlayer> action) {
        AsyncUtils.executeAsync(() -> {
            action.accept(waitUntilFullyLoaded());
        });
    }
}
