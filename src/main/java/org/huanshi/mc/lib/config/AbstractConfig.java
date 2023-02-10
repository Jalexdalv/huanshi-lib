package org.huanshi.mc.lib.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 抽象配置
 * @author Jalexdalv
 */
public abstract class AbstractConfig {
    @Autowired
    private AbstractPlugin plugin;
    private File file;
    private YamlConfiguration configuration;

    /**
     * 加载
     * @param file 文件
     * @param inputStream 数据流
     * @throws IOException 文件IO异常
     */
    public void load(@NotNull File file, @NotNull InputStream inputStream) throws IOException {
        this.file = file;
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
            if (file.exists()) {
                configuration = YamlConfiguration.loadConfiguration(file);
                configuration.setDefaults(YamlConfiguration.loadConfiguration(inputStreamReader));
                configuration.options().copyDefaults(true);
            } else {
                configuration = YamlConfiguration.loadConfiguration(inputStreamReader);
            }
        } finally {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    save();
                } catch (IOException ioException) {
                    throw new RuntimeException(ioException);
                }
            });
        }
    }

    /**
     * 保存
     * @throws IOException 文件IO异常
     */
    public void save() throws IOException {
        configuration.save(file);
    }

    /**
     * 设置
     * @param path 路径
     * @param value 值
     */
    public void set(@NotNull String path, @Nullable Object value) {
        configuration.set(path, value);
    }

    /**
     * 获取位置
     * @param path 路径
     * @return 位置
     */
    public @NotNull Location getLocation(@NotNull String path) {
        return Objects.requireNonNull(configuration.getLocation(path));
    }

    /**
     * 获取字符串
     * @param path 路径
     * @return 字符串
     */
    public @NotNull String getString(@NotNull String path) {
        return Objects.requireNonNull(configuration.getString(path));
    }

    /**
     * 获取字符串列表
     * @param path 路径
     * @return 字符串列表
     */
    public @NotNull List<String> getStringList(@NotNull String path) {
        return configuration.getStringList(path);
    }

    /**
     * 获取字符串集合
     * @param path 路径
     * @return 字符串集合
     */
    public @NotNull Set<String> getStringSet(@NotNull String path) {
        return new HashSet<>(configuration.getStringList(path));
    }

    /**
     * 获取长整型
     * @param path 路径
     * @return 长整型
     */
    public long getLong(@NotNull String path) {
        return configuration.getLong(path);
    }

    /**
     * 获取整型
     * @param path 路径
     * @return 整型
     */
    public int getInt(@NotNull String path) {
        return configuration.getInt(path);
    }

    /**
     * 获取双精度数
     * @param path 路径
     * @return 双精度数
     */
    public double getDouble(@NotNull String path) {
        return configuration.getDouble(path);
    }

    /**
     * 获取浮点数
     * @param path 路径
     * @return 浮点数
     */
    public float getFloat(@NotNull String path) {
        return (float) configuration.getDouble(path);
    }
}
