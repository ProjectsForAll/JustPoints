package host.plas.justessentials.commands;

import host.plas.bou.commands.CommandContext;
import host.plas.bou.commands.SimplifiedCommand;
import host.plas.bou.scheduling.TaskManager;
import host.plas.bou.utils.PlayerUtils;
import host.plas.justessentials.JustEssentials;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class TpHereCMD extends SimplifiedCommand {
    public TpHereCMD() {
        super("teleporthere", JustEssentials.getInstance());
    }

    @Override
    public boolean command(CommandContext commandContext) {
        if (commandContext.isConsole()) {
            commandContext.sendMessage("&cThis command can only be executed by a player.");
            return false;
        }

        Player player = commandContext.getPlayer().orElse(null);
        if (player == null) {
            commandContext.sendMessage("&cAn error occurred while executing the command.");
            return false;
        }

        if (commandContext.getArgCount() < 1) {
            commandContext.sendMessage("&cUsage: /tphere <player>");
            return false;
        }

        Player target = commandContext.getPlayerArg(0).orElse(null);
        if (target == null) {
            commandContext.sendMessage("&cPlayer not found.");
            return false;
        }

        TaskManager.teleport(target, player.getLocation());
        commandContext.sendMessage("&7Teleported &d" + target.getName() + " &7to &dyou&8.");
        return true;
    }

    @Override
    public ConcurrentSkipListSet<String> tabComplete(CommandContext commandContext) {
        ConcurrentSkipListSet<String> completions = new ConcurrentSkipListSet<>();

        if (commandContext.getArgCount() <= 1) {
            completions.addAll(PlayerUtils.getOnlinePlayerNames());
        }

        return completions;
    }
}
