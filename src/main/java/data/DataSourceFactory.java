package data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceFactory {
    /** Example: sqlite("app.db") creates/opens a file named app.db next to your jar. */
    public static DataSource sqlite(String dbPath) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + dbPath);
        config.setMaximumPoolSize(3);
        config.setPoolName("sqlite-pool");
        return new HikariDataSource(config);
    }
}
