package host.plas.justpoints.data.sql;

import lombok.Getter;

@Getter
public enum DatabaseType {
    MYSQL("jdbc:mysql://", "com.mysql.cj.jdbc.Driver"),
    SQLITE("jdbc:sqlite:", "org.sqlite.JDBC"),
    ;

    private final String urlPrefix;
    private final String driver;

    DatabaseType(String urlPrefix, String driver) {
        this.urlPrefix = urlPrefix;
        this.driver = driver;
    }
}
