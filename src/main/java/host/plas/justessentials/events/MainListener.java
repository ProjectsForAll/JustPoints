package host.plas.justessentials.events;

import host.plas.justessentials.JustEssentials;
import host.plas.justessentials.data.flight.FlightCheck;
import host.plas.justessentials.data.flight.FlightManager;
import host.plas.justessentials.data.warps.WarpManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MainListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        FlightManager.checkPlayer(player, FlightCheck.JOIN);

        if (JustEssentials.getMainConfig().isSpawnOnJoin()) {
            WarpManager.getMainSpawn().ifPresent(warp -> {
                warp.teleport(player);
            });
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        FlightManager.checkPlayer(player, FlightCheck.LEAVE);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        FlightManager.checkPlayer(player, FlightCheck.MOVE);
    }
}
