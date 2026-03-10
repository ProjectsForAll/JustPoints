package host.plas.justessentials.commands;

import host.plas.bou.commands.CommandArgument;
import host.plas.bou.commands.CommandContext;
import host.plas.bou.commands.Sender;
import host.plas.bou.commands.SimplifiedCommand;
import host.plas.justessentials.JustEssentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class FeedCMD extends SimplifiedCommand {
    public FeedCMD() {
        super("feed", JustEssentials.getInstance());
    }

    @Override
    public boolean command(CommandContext commandContext) {
        if (commandContext.isConsole()) {
            commandContext.sendMessage("&cThis command can only be executed by a player.");
            return false;
        }

        Player player = commandContext.getPlayer().get();

        Player target = player;

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

        try {
            target.setFoodLevel(20);
            target.setSaturation(20);
        } catch (Exception e) {
            // Do nothing
        }

        // Remove bad potion effects
        List<PotionEffectType> toRemove = new ArrayList<>();
        target.getActivePotionEffects().forEach(potionEffect -> {
            if (JustEssentials.getMainConfig().getFeedBadPotionEffects().contains(potionEffect.getType())) toRemove.add(potionEffect.getType());
        });
        toRemove.forEach(target::removePotionEffect);

        if (target != player) {
            Sender tSender = new Sender(target);

            commandContext.sendMessage("&aFED &d" + target.getName() + "&7.");
            tSender.sendMessage("&7Hunger restored to full.");
        } else {
            commandContext.sendMessage("&7Hunger restored to full.");
        }

        return true;
    }

    @Override
    public ConcurrentSkipListSet<String> tabComplete(CommandContext commandContext) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toCollection(ConcurrentSkipListSet::new));
    }
}
