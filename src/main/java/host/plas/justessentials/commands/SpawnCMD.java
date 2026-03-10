package host.plas.justessentials.commands;

import host.plas.bou.commands.CommandArgument;
import host.plas.bou.commands.CommandContext;
import host.plas.bou.commands.SimplifiedCommand;
import host.plas.justessentials.JustEssentials;
import host.plas.justessentials.data.warps.Warp;
import host.plas.justessentials.data.warps.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class SpawnCMD extends SimplifiedCommand {
    public SpawnCMD() {
        super("spawn", JustEssentials.getInstance());
    }

    @Override
    public boolean command(CommandContext commandContext) {
        if (commandContext.isConsole()) {
            commandContext.sendMessage("&cThis command can only be executed by a player.");
            return false;
        }

        Player player = commandContext.getPlayer().get();

        List<Player> toTp = new ArrayList<>(List.of(player));

        boolean isMain = JustEssentials.getMainConfig().isSpawnMainSpawnByDefault();
        for (CommandArgument argument : commandContext.getArgs()) {
            if (argument.getContent().equalsIgnoreCase("-m")) {
                isMain = true;
            }
            if (argument.getContent().equalsIgnoreCase("-w")) {
                isMain = false;
            }
            try {
                Player target = Bukkit.getPlayer(argument.getContent());
                if (target != null) {
                    toTp.add(target);
                }
            } catch (Exception e) {
                // Do nothing
            }
        }

        Location location = player.getLocation();

        Optional<Warp> warp = Optional.empty();

        if (isMain) {
            warp = WarpManager.getMainSpawn();
        } else {
            warp = WarpManager.getSpawn(location);
        }

        if (warp.isPresent()) {
            Warp w = warp.get();
            toTp.forEach(w::teleport);
        } else {
            if (isMain) {
                commandContext.sendMessage("&cMAIN SPAWN &7not set.");
            } else {
                commandContext.sendMessage("&cSPAWN &8(&dfor this world&8) &7not set.");
            }
        }

        return true;
    }

    @Override
    public ConcurrentSkipListSet<String> tabComplete(CommandContext commandContext) {
        ConcurrentSkipListSet<String> completions = new ConcurrentSkipListSet<>();

        if (commandContext.getArgCount() <= 1) {
            completions = new ConcurrentSkipListSet<>(List.of("-m", "-w"));
        }

        completions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));

        return completions;
    }
}
