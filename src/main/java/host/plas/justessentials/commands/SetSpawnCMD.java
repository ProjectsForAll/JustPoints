package host.plas.justessentials.commands;

import host.plas.bou.commands.CommandContext;
import host.plas.bou.commands.SimplifiedCommand;
import host.plas.justessentials.JustEssentials;
import host.plas.justessentials.data.warps.Warp;
import host.plas.justessentials.data.warps.WarpManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class SetSpawnCMD extends SimplifiedCommand {
    public SetSpawnCMD() {
        super("setspawn", JustEssentials.getInstance());
    }

    @Override
    public boolean command(CommandContext commandContext) {
        if (commandContext.isConsole()) {
            commandContext.sendMessage("&cThis command can only be executed by a player.");
            return false;
        }

        Player player = commandContext.getPlayer().get();

        boolean isMain = false;
        if (commandContext.isArgUsable(0)) {
            String arg = commandContext.getStringArg(0);
            if (arg.equalsIgnoreCase("-m")) {
                isMain = true;
            }
        }

        Location location = player.getLocation();

        Warp warp = WarpManager.setSpawn(location, isMain);

        if (isMain) {
            commandContext.sendMessage("&7Set &cMAIN SPAWN &7at your location.");
        } else {
            commandContext.sendMessage("&7Set &cSPAWN &8(&dfor this world&8) &7at your location.");
        }

        return true;
    }

    @Override
    public ConcurrentSkipListSet<String> tabComplete(CommandContext commandContext) {
        if (commandContext.getArgCount() <= 1) {
            return new ConcurrentSkipListSet<>(List.of("-m"));
        }

        return new ConcurrentSkipListSet<>();
    }
}
