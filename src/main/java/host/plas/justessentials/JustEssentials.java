package host.plas.justessentials;

import host.plas.bou.BetterPlugin;
import host.plas.justessentials.commands.*;
import host.plas.justessentials.config.DatabaseConfig;
import host.plas.justessentials.config.MainConfig;
import host.plas.justessentials.config.WarpConfig;
import host.plas.justessentials.data.warps.WarpManager;
import host.plas.justessentials.events.MainListener;
import host.plas.justessentials.holders.CompatManager;
import host.plas.justessentials.papi.PointsExpansion;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter @Setter
public final class JustEssentials extends BetterPlugin {
    @Getter @Setter
    private static JustEssentials instance;

    @Getter @Setter
    private static MainConfig mainConfig;
    @Getter @Setter
    private static DatabaseConfig databaseConfig;
    @Getter @Setter
    private static MainListener mainListener;

    @Getter @Setter
    private static PointsExpansion expansion;

    @Getter @Setter
    private static WarpConfig warpConfig;

    public JustEssentials() {
        super();
    }

    @Override
    public void onBaseEnabled() {
        // Plugin startup logic
        setInstance(this);

        setMainConfig(new MainConfig());
        setDatabaseConfig(new DatabaseConfig());

        setWarpConfig(new WarpConfig());

        CompatManager.get().register(this);

        if (host.plas.bou.compat.CompatManager.getHolder(host.plas.bou.compat.CompatManager.PAPI_IDENTIFIER).isEnabled()) {
            setExpansion(new PointsExpansion());
            getExpansion().register();
        }

        new FeedCMD();
        new FlyCMD();
        new GamemodeCMD();
        new HealCMD();
        new SetSpawnCMD();
        new SpawnCMD();
        new TpCMD();
        new TpHereCMD();

        setMainListener(new MainListener());
        Bukkit.getPluginManager().registerEvents(getMainListener(), this);
    }

    @Override
    public void onBaseDisable() {
        // Plugin shutdown logic
        WarpManager.saveAllWarps(false);
    }
}
