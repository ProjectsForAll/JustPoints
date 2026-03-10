package host.plas.justessentials.config;

import host.plas.bou.sql.ConnectorSet;
import host.plas.bou.sql.DatabaseType;
import host.plas.justessentials.JustEssentials;
import tv.quaint.storage.resources.flat.simple.SimpleConfiguration;

public class DatabaseConfig extends SimpleConfiguration {
    public DatabaseConfig() {
        super("database-config.yml", JustEssentials.getInstance(), true);
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
    }

    public DatabaseType getDatabaseType() {
        reloadResource();

        return DatabaseType.valueOf(getOrSetDefault("type", DatabaseType.SQLITE.name()));
    }

    public String getDatabaseHost() {
        reloadResource();

        return getOrSetDefault("host", "localhost");
    }

    public int getDatabasePort() {
        reloadResource();

        return getOrSetDefault("port", 3306);
    }

    public String getDatabaseName() {
        reloadResource();

        return getOrSetDefault("database", "jessentials");
    }

    public String getDatabaseUsername() {
        reloadResource();

        return getOrSetDefault("username", "username");
    }

    public String getDatabasePassword() {
        reloadResource();

        return getOrSetDefault("password", "password");
    }

    public String getDatabaseTablePrefix() {
        reloadResource();

        return getOrSetDefault("table-prefix", "jess_");
    }

    public String getSqliteFile() {
        reloadResource();

        return getOrSetDefault("sqlite-file", "jessentials.db");
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
}
