package host.plas.justessentials.data.warps;

import host.plas.bou.commands.Sender;
import host.plas.bou.scheduling.TaskManager;
import host.plas.justessentials.data.location.SavableLocation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import tv.quaint.objects.Identifiable;

@Getter @Setter
public class Warp implements Identifiable {
    private String identifier;
    private SavableLocation location;
    private WarpType type;

    public Warp(String identifier, SavableLocation location, WarpType type) {
        this.identifier = identifier;
        this.location = location;
        this.type = type;
    }

    public Warp(String identifier, Location location, WarpType type) {
        this(identifier, new SavableLocation(location), type);
    }

    public Warp(String identifier, Location location) {
        this(identifier, location, WarpType.ADMIN);
    }

    public boolean isSpawn() {
        return type == WarpType.SPAWN || type == WarpType.MAIN_SPAWN;
    }

    public boolean isHome() {
        return type == WarpType.HOME;
    }

    public boolean isAdmin() {
        return type == WarpType.ADMIN;
    }

    public boolean isPlayer() {
        return type == WarpType.PLAYER;
    }

    public boolean isMainSpawn() {
        return type == WarpType.MAIN_SPAWN;
    }

    public void save() {
        save(true);
    }

    public void save(boolean async) {
        WarpManager.save(identifier, async);
    }

    public void load() {
        WarpManager.loadWarp(this);
    }

    public void unload(boolean save, boolean async) {
        WarpManager.unloadWarp(identifier, save, async);
    }

    public void unload() {
        WarpManager.unloadWarp(identifier);
    }

    public Location asLocation() {
        return location.asLocation();
    }

    public void teleport(Entity entity) {
        entity.teleport(asLocation());
        if (entity instanceof Player) {
            Player player = (Player) entity;
            Sender sender = new Sender(player);

            sender.sendMessage("&7Teleported to &c" + identifier + "&7.");
        }
    }
}
