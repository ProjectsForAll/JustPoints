package host.plas.justessentials.data.flight;

import host.plas.justessentials.JustEssentials;
import org.bukkit.entity.Player;

import java.util.List;

public class FlightManager {
    public static void checkPlayer(Player player, FlightCheck check) {
        switch (check) {
            case JOIN:
                if (JustEssentials.getMainConfig().isFlightCheckOnJoin()) {
                    checkFlight(player);
                }
                break;
            case MOVE:
                if (JustEssentials.getMainConfig().isFlightCheckOnMove()) {
                    checkFlight(player);
                }
                break;
            case LEAVE:
                if (JustEssentials.getMainConfig().isFlightCheckOnLeave()) {
                    checkFlight(player);
                }
                break;
        }
    }

    public static void checkFlight(Player player) {
        List<String> permissions = JustEssentials.getMainConfig().getFlightCheckPermissions();

        boolean allowed = true;

        for (String permission : permissions) {
            if (! player.hasPermission(permission)) {
                allowed = false;
                break;
            }
        }

        if (! allowed) setFlight(player, false);
    }

    public static void toggleFlight(Player target) {
        setFlight(target, ! target.getAllowFlight());
    }

    public static void setFlight(Player target, boolean flight) {
        target.setAllowFlight(flight);
        if (flight && ! target.isFlying() && ! target.isOnGround()) {
            target.setFlying(true);
        }
        if (! flight && target.isFlying()) {
            target.setFlying(false);
        }
    }
}
