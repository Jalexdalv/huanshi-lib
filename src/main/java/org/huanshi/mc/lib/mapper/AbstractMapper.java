package org.huanshi.mc.lib.mapper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.huanshi.mc.lib.Component;
import org.huanshi.mc.lib.annotation.Autowired;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractMapper implements Component {
    private static HikariDataSource hikariDataSource;
    @Autowired(file = "db.yml", path = "hikari-cp.connection-timeout")
    private long timeout;
    @Autowired(file = "db.yml", path = "hikari-cp.minimum-idle")
    private int minimumIdle;
    @Autowired(file = "db.yml", path = "hikari-cp.maximum-pool-size")
    private int maximumPoolSize;
    @Autowired(file = "db.yml", path = "mysql.address")
    private String address;
    @Autowired(file = "db.yml", path = "mysql.port")
    private int port;
    @Autowired(file = "db.yml", path = "mysql.database")
    private String database;
    @Autowired(file = "db.yml", path = "mysql.user")
    private String user;
    @Autowired(file = "db.yml", path = "mysql.password")
    private String password;

    public void load() {
        if (hikariDataSource == null) {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setConnectionTimeout(timeout);
            hikariConfig.setMinimumIdle(minimumIdle);
            hikariConfig.setMaximumPoolSize(maximumPoolSize);
            hikariConfig.setJdbcUrl("jdbc:mysql://" + address + ":" + port + "/" + database + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai");
            hikariConfig.setUsername(user);
            hikariConfig.setPassword(password);
            hikariConfig.setAutoCommit(true);
            hikariDataSource = new HikariDataSource(hikariConfig);
        }
    }

    protected @NotNull Connection getMySQLConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
