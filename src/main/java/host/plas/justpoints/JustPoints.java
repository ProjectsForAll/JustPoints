package host.plas.justpoints;

import host.plas.justpoints.commands.PointsCMD;
import host.plas.justpoints.config.MainConfig;
import host.plas.justpoints.data.PointPlayer;
import host.plas.justpoints.data.sql.CombinedSqlHelper;
import host.plas.justpoints.data.sql.MySqlHelper;
import host.plas.justpoints.events.MainListener;
import host.plas.justpoints.papi.PointsExpansion;
import host.plas.justpoints.timers.SaveTimer;
import io.streamlined.bukkit.PluginBase;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter @Setter
public final class JustPoints extends PluginBase {
    @Getter @Setter
    private static JustPoints instance;

    @Getter @Setter
    private static MainConfig mainConfig;
    @Getter @Setter
    private static CombinedSqlHelper combinedSqlHelper;
    @Getter @Setter
    private static SaveTimer saveTimer;
    @Getter @Setter
    private static MainListener mainListener;

    @Getter @Setter
    private static PointsExpansion expansion;

    @Getter @Setter
    private static PointsCMD pointsCMD;

    public JustPoints() {
        super();
    }

    @Override
    public void onBaseEnabled() {
        // Plugin startup logic
        setInstance(this);

        setMainConfig(new MainConfig());
        setCombinedSqlHelper(new CombinedSqlHelper(getMainConfig().buildConnectorSet()));
        getCombinedSqlHelper().ensureReady(); // Test connection

        setExpansion(new PointsExpansion());
        getExpansion().register();

        setSaveTimer(new SaveTimer());

        setPointsCMD(new PointsCMD());
        getPointsCMD().register();

        setMainListener(new MainListener());
        Bukkit.getPluginManager().registerEvents(getMainListener(), this);
    }

    @Override
    public void onBaseDisable() {
        // Plugin shutdown logic
        getSaveTimer().cancel();

        PointPlayer.getPlayers().forEach(PointPlayer::save);
    }
}
