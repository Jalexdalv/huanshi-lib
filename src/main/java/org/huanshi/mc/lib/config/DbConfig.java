package org.huanshi.mc.lib.config;

import org.huanshi.mc.lib.annotation.Config;

/**
 * 数据库配置
 * @author Jalexdalv
 */
@Config(file = "db.yml", resource = "/db.yml")
public class DbConfig extends AbstractConfig {}
