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
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class HealCMD extends SimplifiedCommand {
    public HealCMD() {
        super("heal", JustEssentials.getInstance());
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

        // Set health to full
        try {
            AttributeInstance maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealth != null) {
                target.setHealth(maxHealth.getValue());
            } else {
                target.setHealth(target.getMaxHealth());
            }
        } catch (Throwable e) {
            target.setHealth(target.getMaxHealth());
        }

        // Set food and saturation to full
        try {
            target.setFoodLevel(20);
            target.setSaturation(20);
        } catch (Exception e) {
            // Do nothing
        }

        // Remove bad potion effects
        List<PotionEffectType> toRemove = new ArrayList<>();
        target.getActivePotionEffects().forEach(potionEffect -> {
            if (JustEssentials.getMainConfig().getHealBadPotionEffects().contains(potionEffect.getType())) toRemove.add(potionEffect.getType());
        });
        toRemove.forEach(target::removePotionEffect);

        // Notify players
        if (target != player) {
            Sender tSender = new Sender(target);

            commandContext.sendMessage("&aHEALED &d" + target.getName() + "&7.");
            tSender.sendMessage("&7Health restored to full.");
        } else {
            commandContext.sendMessage("&7Health restored to full.");
        }

        return true;
    }

    @Override
    public ConcurrentSkipListSet<String> tabComplete(CommandContext commandContext) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toCollection(ConcurrentSkipListSet::new));
    }
}
