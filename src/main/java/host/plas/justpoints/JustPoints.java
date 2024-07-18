package host.plas.justpoints;

import host.plas.justpoints.commands.PointsCMD;
import host.plas.justpoints.config.MainConfig;
import host.plas.justpoints.data.PointPlayer;
import host.plas.justpoints.data.sql.PointsOperator;
import host.plas.justpoints.events.MainListener;
import host.plas.justpoints.managers.PointsManager;
import host.plas.justpoints.papi.PointsExpansion;
import host.plas.justpoints.timers.SyncTimer;
import host.plas.bou.PluginBase;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tv.quaint.objects.handling.derived.IPluginEventable;

import java.io.File;

@Getter @Setter
public final class JustPoints extends PluginBase implements IPluginEventable {
    @Getter @Setter
    private static JustPoints instance;

    @Getter @Setter
    private static MainConfig mainConfig;
    @Getter @Setter
    private static PointsOperator mainDatabase;
    @Getter @Setter
    private static SyncTimer syncTimer;
    @Getter @Setter
    private static MainListener mainListener;

    @Getter @Setter
    private static PointsExpansion expansion;

    @Getter @Setter
    private static PointsCMD pointsCMD;

    @Getter @Setter
    private static MyEventable myEventable;

    public JustPoints() {
        super();
    }

    @Override
    public void onBaseEnabled() {
        // Plugin startup logic
        setInstance(this);

        setMyEventable(new MyEventable(this));

        setMainConfig(new MainConfig());
        setMainDatabase(new PointsOperator(getMainConfig().buildConnectorSet()));
        getMainDatabase().ensureUsable(); // Test connection

        setExpansion(new PointsExpansion());
        getExpansion().register();

        setSyncTimer(new SyncTimer());

        setPointsCMD(new PointsCMD());
        getPointsCMD().register();

        setMainListener(new MainListener());
        Bukkit.getPluginManager().registerEvents(getMainListener(), this);
    }

    @Override
    public void onBaseDisable() {
        // Plugin shutdown logic
        getSyncTimer().cancel();

        PointsManager.getLoadedPlayers().forEach(PointPlayer::saveAndUnload);
    }

    @Getter @Setter
    public static class MyEventable implements IPluginEventable {
        private final JavaPlugin plugin;

        public MyEventable(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public File getDataFolder() {
            return plugin.getDataFolder();
        }

        @Override
        public String getIdentifier() {
            return plugin.getName();
        }
    }
}
