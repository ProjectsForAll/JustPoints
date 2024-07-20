package host.plas.justpoints.config;

import host.plas.bou.sql.ConnectorSet;
import host.plas.bou.sql.DatabaseType;
import host.plas.justpoints.JustPoints;
import tv.quaint.storage.resources.flat.simple.SimpleConfiguration;

public class MainConfig extends SimpleConfiguration {
    public MainConfig() {
        super("config.yml", JustPoints.getInstance(), true);
    }

    @Override
    public void init() {
        getDatabaseType();
        getDatabaseHost();
        getDatabasePort();
        getDatabaseName();
        getDatabaseTablePrefix();
        getDatabaseUsername();
        getDatabasePassword();
        getSqliteFile();

        getPointsDefault();
        getPointsOnJoinLoad();
        getPointsOnQuitSave();
        getPointsOnQuitDispose();
        getPointsSaveInterval();
    }

    public DatabaseType getDatabaseType() {
        reloadResource();

        return DatabaseType.valueOf(getOrSetDefault("database.type", DatabaseType.SQLITE.name()));
    }

    public String getDatabaseHost() {
        reloadResource();

        return getOrSetDefault("database.host", "localhost");
    }

    public int getDatabasePort() {
        reloadResource();

        return getOrSetDefault("database.port", 3306);
    }

    public String getDatabaseName() {
        reloadResource();

        return getOrSetDefault("database.database", "database");
    }

    public String getDatabaseUsername() {
        reloadResource();

        return getOrSetDefault("database.username", "username");
    }

    public String getDatabasePassword() {
        reloadResource();

        return getOrSetDefault("database.password", "password");
    }

    public String getDatabaseTablePrefix() {
        reloadResource();

        return getOrSetDefault("database.table-prefix", "pnts_");
    }

    public String getSqliteFile() {
        reloadResource();

        return getOrSetDefault("database.sqlite-file", "points.db");
    }

    public ConnectorSet buildConnectorSet() {
        return new ConnectorSet(
                getDatabaseType(),
                getDatabaseHost(),
                getDatabasePort(),
                getDatabaseName(),
                getDatabaseUsername(),
                getDatabasePassword(),
                getDatabaseTablePrefix(),
                getSqliteFile()
        );
    }

    public double getPointsDefault() {
        reloadResource();

        return getOrSetDefault("points.default", 0.0);
    }

    public boolean getPointsOnJoinLoad() {
        reloadResource();

        return getOrSetDefault("points.on-join.load", true);
    }

    public boolean getPointsOnQuitSave() {
        reloadResource();

        return getOrSetDefault("points.on-quit.save", true);
    }

    public boolean getPointsOnQuitDispose() {
        reloadResource();

        return getOrSetDefault("points.on-quit.dispose", true);
    }

    public int getPointsSaveInterval() {
        reloadResource();

        return getOrSetDefault("points.save-interval", 5);
    }
}
