package host.plas.justessentials.commands;

import host.plas.bou.commands.CommandContext;
import host.plas.bou.commands.SimplifiedCommand;
import host.plas.bou.scheduling.TaskManager;
import host.plas.bou.utils.PlayerUtils;
import host.plas.justessentials.JustEssentials;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentSkipListSet;

public class TpCMD extends SimplifiedCommand {
    public TpCMD() {
        super("teleport", JustEssentials.getInstance());
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
            commandContext.sendMessage("&cUsage: /tp <player> (player)");
            return false;
        }

        Player target = commandContext.getPlayerArg(0).orElse(null);
        if (target == null) {
            commandContext.sendMessage("&cPlayer not found.");
            return false;
        }

        if (commandContext.getArgCount() == 1) {
            TaskManager.teleport(player, target.getLocation());
            commandContext.sendMessage("&7Teleported to &d" + target.getName() + "&8.");

            return true;
        } else if (commandContext.getArgCount() == 2) {
            Player target2 = commandContext.getPlayerArg(1).orElse(null);
            if (target2 == null) {
                commandContext.sendMessage("&cPlayer not found.");
                return false;
            }
            target2.teleport(target);
            commandContext.sendMessage("&7Teleported &d" + target2.getName() + " &7to &d" + target.getName() + "&8.");

            return true;
        } else {
            commandContext.sendMessage("&cUsage: /tp <player> (player)");
            return false;
        }
    }

    @Override
    public ConcurrentSkipListSet<String> tabComplete(CommandContext commandContext) {
        ConcurrentSkipListSet<String> completions = new ConcurrentSkipListSet<>();

        if (commandContext.getArgCount() <= 1) {
            completions.addAll(PlayerUtils.getOnlinePlayerNames());
        } else if (commandContext.getArgCount() == 2) {
            completions.addAll(PlayerUtils.getOnlinePlayerNames());
        }

        return completions;
    }
}
