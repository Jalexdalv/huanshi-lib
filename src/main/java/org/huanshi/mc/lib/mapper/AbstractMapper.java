package org.huanshi.mc.lib.mapper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.config.DbConfig;
import org.huanshi.mc.lib.engine.Component;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractMapper implements Component {
    private static HikariDataSource hikariDataSource;
    @Autowired
    private DbConfig dbConfig;

    @Override
    public void load() {
        if (hikariDataSource == null) {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setConnectionTimeout(dbConfig.getLong("hikari-cp.connection-timeout"));
            hikariConfig.setMinimumIdle(dbConfig.getInt("hikari-cp.minimum-idle"));
            hikariConfig.setMaximumPoolSize(dbConfig.getInt("hikari-cp.maximum-pool-size"));
            hikariConfig.setJdbcUrl("jdbc:mysql://" + dbConfig.getString("mysql.address") + ":" + dbConfig.getInt("mysql.port") + "/" + dbConfig.getString("mysql.database") + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai");
            hikariConfig.setUsername(dbConfig.getString("mysql.user"));
            hikariConfig.setPassword(dbConfig.getString("mysql.password"));
            hikariConfig.setAutoCommit(true);
            hikariDataSource = new HikariDataSource(hikariConfig);
        }
    }

    @Override
    public void onLoad() {}

    protected @NotNull Connection getMySQLConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
