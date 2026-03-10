package host.plas.justessentials.papi;

import host.plas.justessentials.JustEssentials;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tv.quaint.utils.StringUtils;

public class PointsExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "jess";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Drak";
    }

    @Override
    public @NotNull String getVersion() {
        return JustEssentials.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        return null;
    }
}
