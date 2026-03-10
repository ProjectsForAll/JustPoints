package host.plas.justessentials.commands;

import host.plas.bou.commands.CommandArgument;
import host.plas.bou.commands.CommandContext;
import host.plas.bou.commands.Sender;
import host.plas.bou.commands.SimplifiedCommand;
import host.plas.justessentials.JustEssentials;
import host.plas.justessentials.data.flight.FlightManager;
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

public class FlyCMD extends SimplifiedCommand {
    public FlyCMD() {
        super("fly", JustEssentials.getInstance());
    }

    @Override
    public boolean command(CommandContext commandContext) {
        if (commandContext.isConsole()) {
            commandContext.sendMessage("&cThis command can only be executed by a player.");
            return false;
        }

        Player player = commandContext.getPlayer().get();

        Player target = player;

        boolean isMain = JustEssentials.getMainConfig().isSpawnMainSpawnByDefault();
        for (CommandArgument argument : commandContext.getArgs()) {
            try {
                Player t = Bukkit.getPlayer(argument.getContent());
                if (t != null) {
                    target = t;
                }
            } catch (Exception e) {
                // Do nothing
            }
        }

        FlightManager.toggleFlight(target);

        if (target != player) {
            Sender tSender = new Sender(target);

            if (target.getAllowFlight()) {
                commandContext.sendMessage("&7Flight &aENABLED &7for &d" + target.getName() + "&7.");
                tSender.sendMessage("&7Flight &aENABLED&7.");
            } else {
                commandContext.sendMessage("&7Flight &cDISABLED &7for &d" + target.getName() + "&7.");
                tSender.sendMessage("&7Flight &cDISABLED&7.");
            }
        } else {
            if (target.getAllowFlight()) {
                commandContext.sendMessage("&7Flight &aENABLED&7.");
            } else {
                commandContext.sendMessage("&7Flight &cDISABLED&7.");
            }
        }

        return true;
    }

    @Override
    public ConcurrentSkipListSet<String> tabComplete(CommandContext commandContext) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toCollection(ConcurrentSkipListSet::new));
    }
}
