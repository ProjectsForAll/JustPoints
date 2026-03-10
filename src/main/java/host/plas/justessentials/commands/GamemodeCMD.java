package host.plas.justessentials.commands;

import host.plas.bou.commands.CommandArgument;
import host.plas.bou.commands.CommandContext;
import host.plas.bou.commands.SimplifiedCommand;
import host.plas.bou.utils.PlayerUtils;
import host.plas.justessentials.JustEssentials;
import host.plas.justessentials.data.warps.Warp;
import host.plas.justessentials.data.warps.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class GamemodeCMD extends SimplifiedCommand {
    public GamemodeCMD() {
        super("gamemode", JustEssentials.getInstance());
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
        GameMode gameMode = player.getGameMode() == GameMode.CREATIVE ? GameMode.SURVIVAL : GameMode.CREATIVE;
        if (commandContext.getArgCount() < 1) {
            player.setGameMode(gameMode);
            commandContext.sendMessage("&eYour gamemode has been set to &9&l" + gameMode.name() + "&8.");
            return true;
        }

        String gamemodeArg = commandContext.getStringArg(0).toLowerCase();
        GameMode targetGameMode;
        switch (gamemodeArg) {
            case "survival", "s", "0":
                targetGameMode = GameMode.SURVIVAL;
                break;
            case "creative", "c", "1":
                targetGameMode = GameMode.CREATIVE;
                break;
            case "adventure", "a", "2":
                targetGameMode = GameMode.ADVENTURE;
                break;
            case "spectator", "sp", "3":
                targetGameMode = GameMode.SPECTATOR;
                break;
            default:
                commandContext.sendMessage("&cInvalid gamemode. Valid options are: &9&lSURVIVAL, CREATIVE, ADVENTURE, SPECTATOR&8.");
                return false;
        }

        if (commandContext.getArgCount() == 1) {
            player.setGameMode(targetGameMode);
            commandContext.sendMessage("&7Your gamemode has been set to &c" + targetGameMode.name() + "&8.");
            return true;
        }

        Player targetPlayer = commandContext.getPlayerArg(1).orElse(null);
        if (targetPlayer == null) {
            commandContext.sendMessage("&cPlayer not found.");
            return false;
        }

        targetPlayer.setGameMode(targetGameMode);
        commandContext.sendMessage("&7Set gamemode of &d" + targetPlayer.getName() + " &7to &c" + targetGameMode.name() + "&8.");
        return true;
    }

    @Override
    public ConcurrentSkipListSet<String> tabComplete(CommandContext commandContext) {
        ConcurrentSkipListSet<String> completions = new ConcurrentSkipListSet<>();

        if (commandContext.getArgCount() <= 1) {
            completions.add("survival");
            completions.add("creative");
            completions.add("adventure");
            completions.add("spectator");
        } else if (commandContext.getArgCount() == 2) {
            completions.addAll(PlayerUtils.getOnlinePlayerNames());
        }

        return completions;
    }
}
