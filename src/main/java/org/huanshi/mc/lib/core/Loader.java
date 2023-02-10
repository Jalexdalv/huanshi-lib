package org.huanshi.mc.lib.core;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Command;
import org.huanshi.mc.lib.annotation.Config;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.annotation.Mapper;
import org.huanshi.mc.lib.annotation.Receiver;
import org.huanshi.mc.lib.annotation.Sender;
import org.huanshi.mc.lib.annotation.Service;
import org.huanshi.mc.lib.annotation.Skill;
import org.huanshi.mc.lib.annotation.Task;
import org.huanshi.mc.lib.command.AbstractCommand;
import org.huanshi.mc.lib.command.Environment;
import org.huanshi.mc.lib.config.AbstractConfig;
import org.huanshi.mc.lib.listener.AbstractListener;
import org.huanshi.mc.lib.mapper.AbstractMapper;
import org.huanshi.mc.lib.protocol.AbstractReceiver;
import org.huanshi.mc.lib.protocol.AbstractSender;
import org.huanshi.mc.lib.service.AbstractService;
import org.huanshi.mc.lib.skill.AbstractSkill;
import org.huanshi.mc.lib.task.AbstractTask;
import org.huanshi.mc.lib.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Loader {
    private static final Map<Class<?>, Set<Class<?>>> SENDER_CLASS_MAP = new HashMap<>(), RECEIVER_CLASS_MAP = new HashMap<>(), MAPPER_CLASS_MAP = new HashMap<>(), SERVICE_CLASS_MAP = new HashMap<>(), SKILL_CLASS_MAP = new HashMap<>(), TASK_CLASS_MAP = new HashMap<>(), LISTENER_CLASS_MAP = new HashMap<>();
    private static final Map<Class<?>, Map<Class<?>, Config>> CONFIG_CLASS_MAP = new HashMap<>();
    private static final Map<Class<?>, Map<Class<?>, Command>> COMMAND_CLASS_MAP = new HashMap<>();
    private static final Map<Class<?>, Map<Class<?>, AbstractConfig>> LOADED_CONFIG_MAP = new HashMap<>();
    private static final Map<Class<?>, Map<Class<?>, AbstractSender>> LOADED_SENDER_MAP = new HashMap<>();
    private static final Map<Class<?>, Map<Class<?>, AbstractReceiver>> LOADED_RECEIVER_MAP = new HashMap<>();
    private static final Map<Class<?>, Map<Class<?>, AbstractMapper>> LOADED_MAPPER_MAP = new HashMap<>();
    private static final Map<Class<?>, Map<Class<?>, AbstractService>> LOADED_SERVICE_MAP = new HashMap<>();
    private static final Map<Class<?>, Map<Class<?>, AbstractSkill>> LOADED_SKILL_MAP = new HashMap<>();
    private static final Map<Class<?>, Map<Class<?>, AbstractCommand>> LOADED_COMMAND_MAP = new HashMap<>();
    private static final Map<Class<?>, Map<Class<?>, AbstractTask>> LOADED_TASK_MAP = new HashMap<>();
    private static final Map<Class<?>, Map<Class<?>, AbstractListener>> LOADED_LISTENER_MAP = new HashMap<>();
    private static final Map<Class<?>, Map<String, AbstractConfig>> CONFIG_FILE_MAP = new HashMap<>();
    private static final Set<String> COMMAND_SET = new HashSet<>(), OP_COMMAND_SET = new HashSet<>();

    public static void scan(@NotNull AbstractPlugin plugin) throws IOException, ClassNotFoundException {
        Class<?> pluginClass = plugin.getClass();
        Set<Class<?>> senderClassSet = new HashSet<>(), receiverClassSet = new HashSet<>(), mapperClassSet = new HashSet<>(), serviceClassSet = new HashSet<>(), skillClassSet = new HashSet<>(), taskClassSet = new HashSet<>(), listenerClassSet = new HashSet<>();
        Map<Class<?>, Config> configClassMap = new HashMap<>();
        Map<Class<?>, Command> commandClassMap = new HashMap<>();
        for (Class<?> clazz : ReflectUtils.getJarClasses(pluginClass)) {
            Config config = clazz.getAnnotation(Config.class);
            Sender sender = clazz.getAnnotation(Sender.class);
            Receiver receiver = clazz.getAnnotation(Receiver.class);
            Mapper mapper = clazz.getAnnotation(Mapper.class);
            Service service = clazz.getAnnotation(Service.class);
            Skill skill = clazz.getAnnotation(Skill.class);
            Command command = clazz.getAnnotation(Command.class);
            Task task = clazz.getAnnotation(Task.class);
            Listener listener = clazz.getAnnotation(Listener.class);
            if (config != null && sender == null && receiver == null && mapper == null && service == null && skill == null && command == null && task == null && listener == null && AbstractConfig.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractSkill.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                configClassMap.put(clazz, config);
            } else if (sender != null && config == null && receiver == null && mapper == null && service == null && skill == null && command == null && task == null && listener == null && AbstractSender.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractSkill.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                senderClassSet.add(clazz);
            } else if (receiver != null && config == null && sender == null && mapper == null && service == null && skill == null && command == null && task == null && listener == null && AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractSkill.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                receiverClassSet.add(clazz);
            } else if (mapper != null && config == null && sender == null && receiver == null && service == null && skill == null && command == null && task == null && listener == null && AbstractMapper.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractSkill.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                mapperClassSet.add(clazz);
            } else if (service != null && config == null && sender == null && receiver == null && mapper == null && skill == null && command == null && task == null && listener == null && AbstractService.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractSkill.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                serviceClassSet.add(clazz);
            } else if (skill != null && config == null && sender == null && receiver == null && mapper == null && service == null && command == null && task == null && listener == null && AbstractSkill.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                skillClassSet.add(clazz);
            } else if (command != null && config == null && sender == null && receiver == null && mapper == null && service == null && skill == null && task == null && listener == null && AbstractCommand.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractSkill.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                commandClassMap.put(clazz, command);
            } else if (task != null && config == null && sender == null && receiver == null && mapper == null && service == null && skill == null && command == null && listener == null && AbstractTask.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractSkill.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                taskClassSet.add(clazz);
            } else if (listener != null && config == null && sender == null && receiver == null && mapper == null && service == null && skill == null && command == null && task == null && AbstractListener.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractSkill.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz)) {
                listenerClassSet.add(clazz);
            }
        }
        CONFIG_CLASS_MAP.put(pluginClass, configClassMap);
        SENDER_CLASS_MAP.put(pluginClass, senderClassSet);
        RECEIVER_CLASS_MAP.put(pluginClass, receiverClassSet);
        MAPPER_CLASS_MAP.put(pluginClass, mapperClassSet);
        SERVICE_CLASS_MAP.put(pluginClass, serviceClassSet);
        SKILL_CLASS_MAP.put(pluginClass, skillClassSet);
        COMMAND_CLASS_MAP.put(pluginClass, commandClassMap);
        TASK_CLASS_MAP.put(pluginClass, taskClassSet);
        LISTENER_CLASS_MAP.put(pluginClass, listenerClassSet);
    }

    public static void load(@NotNull AbstractPlugin plugin) throws Throwable {
        Class<?> pluginClass = plugin.getClass();
        Set<Class<?>> senderClassSet = SENDER_CLASS_MAP.get(pluginClass), receiverClassSet = RECEIVER_CLASS_MAP.get(pluginClass), mapperClassSet = MAPPER_CLASS_MAP.get(pluginClass), serviceClassSet = SERVICE_CLASS_MAP.get(pluginClass), skillClassSet = SKILL_CLASS_MAP.get(pluginClass), taskClassSet = TASK_CLASS_MAP.get(pluginClass), listenerClassSet = LISTENER_CLASS_MAP.get(pluginClass);
        Map<Class<?>, Config> configClassMap = CONFIG_CLASS_MAP.get(pluginClass);
        Map<Class<?>, Command> commandClassMap = COMMAND_CLASS_MAP.get(pluginClass);
        for (Map.Entry<Class<?>, Config> entry : configClassMap.entrySet()) {
            Config config = entry.getValue();
            loadConfig(entry.getKey(), plugin, StringUtils.trimToNull(config.file()), StringUtils.trimToNull(config.resource()));
        }
        for (Class<?> clazz : senderClassSet) {
            loadSender(clazz, plugin);
        }
        for (Class<?> clazz : receiverClassSet) {
            loadReceiver(clazz, plugin);
        }
        for (Class<?> clazz : mapperClassSet) {
            loadMapper(clazz, plugin);
        }
        for (Class<?> clazz : serviceClassSet) {
            loadService(clazz, plugin);
        }
        for (Class<?> clazz : skillClassSet) {
            loadSkill(clazz, plugin);
        }
        for (Map.Entry<Class<?>, Command> entry : commandClassMap.entrySet()) {
            Command command = entry.getValue();
            for (int i = 0, len = command.args().length; i < len; i++) {
                command.args()[i] = StringUtils.trimToNull(command.args()[i]);
            }
            loadCommand(entry.getKey(), plugin, command.environment(), command.op(), command.combat(), StringUtils.trimToNull(command.permission()), StringUtils.trimToNull(command.head()), command.args());
        }
        for (Class<?> clazz : taskClassSet) {
            loadTask(clazz, plugin);
        }
        for (Class<?> clazz : listenerClassSet) {
            loadListener(clazz, plugin);
        }
    }

    private static void loadConfig(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin, @NotNull String file, @NotNull String resource) throws Throwable {
        Class<?> pluginClass = plugin.getClass();
        Map<Class<?>, AbstractConfig> configMap = LOADED_CONFIG_MAP.computeIfAbsent(pluginClass, key -> new HashMap<>());
        Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.computeIfAbsent(pluginClass, key -> new HashMap<>());
        if (!configMap.containsKey(clazz)) {
            AbstractConfig config = (AbstractConfig) clazz.getConstructor().newInstance();
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                if (field.getAnnotation(Autowired.class) != null && AbstractPlugin.class.isAssignableFrom(field.getType())) {
                    field.set(config, plugin);
                }
            }
            config.load(new File(plugin.getDataFolder(), file), Objects.requireNonNull(clazz.getResource(resource)).openStream());
            configMap.put(clazz, config);
            configFileMap.put(file, config);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadSender(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin) throws Throwable {
        Class<?> pluginClass = plugin.getClass();
        Map<Class<?>, AbstractSender> senderMap = LOADED_SENDER_MAP.computeIfAbsent(pluginClass, key -> new HashMap<>());
        if (!senderMap.containsKey(clazz)) {
            AbstractSender sender = (AbstractSender) clazz.getConstructor().newInstance();
            Map<Class<?>, AbstractConfig> configMap = LOADED_CONFIG_MAP.get(pluginClass);
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(pluginClass);
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(sender, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(sender, configMap.get(field.getType()));
                    } else if (Location.class == field.getType()) {
                        field.set(sender, configFileMap.get(autowired.file()).getLocation(autowired.path()[0]));
                    } else if (String.class == field.getType()) {
                        field.set(sender, configFileMap.get(autowired.file()).getString(autowired.path()[0]));
                    } else if (List.class == field.getType()) {
                        field.set(sender, configFileMap.get(autowired.file()).getStringList(autowired.path()[0]));
                    } else if (Set.class == field.getType()) {
                        field.set(sender, configFileMap.get(autowired.file()).getStringSet(autowired.path()[0]));
                    } else if (long.class == field.getType()) {
                        field.set(sender, configFileMap.get(autowired.file()).getLong(autowired.path()[0]));
                    } else if (int.class == field.getType()) {
                        field.set(sender, configFileMap.get(autowired.file()).getInt(autowired.path()[0]));
                    } else if (double.class == field.getType()) {
                        field.set(sender, configFileMap.get(autowired.file()).getDouble(autowired.path()[0]));
                    } else if (float.class == field.getType()) {
                        field.set(sender, configFileMap.get(autowired.file()).getFloat(autowired.path()[0]));
                    }
                }
            }
            sender.load();
            sender.register();
            senderMap.put(clazz, sender);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadReceiver(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin) throws Throwable {
        Class<?> pluginClass = plugin.getClass();
        Map<Class<?>, AbstractReceiver> receiverMap = LOADED_RECEIVER_MAP.computeIfAbsent(pluginClass, key -> new HashMap<>());
        if (!receiverMap.containsKey(clazz)) {
            AbstractReceiver receiver = (AbstractReceiver) clazz.getConstructor().newInstance();
            Map<Class<?>, AbstractConfig> configMap = LOADED_CONFIG_MAP.get(pluginClass);
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(pluginClass);
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(receiver, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(receiver, configMap.get(field.getType()));
                    } else if (Location.class == field.getType()) {
                        field.set(receiver, configFileMap.get(autowired.file()).getLocation(autowired.path()[0]));
                    } else if (String.class == field.getType()) {
                        field.set(receiver, configFileMap.get(autowired.file()).getString(autowired.path()[0]));
                    } else if (List.class == field.getType()) {
                        field.set(receiver, configFileMap.get(autowired.file()).getStringList(autowired.path()[0]));
                    } else if (Set.class == field.getType()) {
                        field.set(receiver, configFileMap.get(autowired.file()).getStringSet(autowired.path()[0]));
                    } else if (long.class == field.getType()) {
                        field.set(receiver, configFileMap.get(autowired.file()).getLong(autowired.path()[0]));
                    } else if (int.class == field.getType()) {
                        field.set(receiver, configFileMap.get(autowired.file()).getInt(autowired.path()[0]));
                    } else if (double.class == field.getType()) {
                        field.set(receiver, configFileMap.get(autowired.file()).getDouble(autowired.path()[0]));
                    } else if (float.class == field.getType()) {
                        field.set(receiver, configFileMap.get(autowired.file()).getFloat(autowired.path()[0]));
                    }
                }
            }
            receiver.load();
            receiver.register();
            receiverMap.put(clazz, receiver);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadMapper(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin) throws Throwable {
        Class<?> pluginClass = plugin.getClass();
        Map<Class<?>, AbstractMapper> mapperMap = LOADED_MAPPER_MAP.computeIfAbsent(pluginClass, key -> new HashMap<>());
        if (!mapperMap.containsKey(clazz)) {
            AbstractMapper mapper = (AbstractMapper) clazz.getConstructor().newInstance();
            Map<Class<?>, AbstractConfig> configMap = LOADED_CONFIG_MAP.get(pluginClass);
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(pluginClass);
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(mapper, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(mapper, configMap.get(field.getType()));
                    } else if (Location.class == field.getType()) {
                        field.set(mapper, configFileMap.get(autowired.file()).getLocation(autowired.path()[0]));
                    } else if (String.class == field.getType()) {
                        field.set(mapper, configFileMap.get(autowired.file()).getString(autowired.path()[0]));
                    } else if (List.class == field.getType()) {
                        field.set(mapper, configFileMap.get(autowired.file()).getStringList(autowired.path()[0]));
                    } else if (Set.class == field.getType()) {
                        field.set(mapper, configFileMap.get(autowired.file()).getStringSet(autowired.path()[0]));
                    } else if (long.class == field.getType()) {
                        field.set(mapper, configFileMap.get(autowired.file()).getLong(autowired.path()[0]));
                    } else if (int.class == field.getType()) {
                        field.set(mapper, configFileMap.get(autowired.file()).getInt(autowired.path()[0]));
                    } else if (double.class == field.getType()) {
                        field.set(mapper, configFileMap.get(autowired.file()).getDouble(autowired.path()[0]));
                    } else if (float.class == field.getType()) {
                        field.set(mapper, configFileMap.get(autowired.file()).getFloat(autowired.path()[0]));
                    }
                }
            }
            mapper.load();
            mapperMap.put(clazz, mapper);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadService(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin) throws Throwable {
        Class<?> pluginClass = plugin.getClass();
        Map<Class<?>, AbstractService> serviceMap = LOADED_SERVICE_MAP.computeIfAbsent(pluginClass, key -> new HashMap<>());
        if (!serviceMap.containsKey(clazz)) {
            AbstractService service = (AbstractService) clazz.getConstructor().newInstance();
            Map<Class<?>, AbstractConfig> configMap = LOADED_CONFIG_MAP.get(pluginClass);
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(pluginClass);
            Map<Class<?>, AbstractMapper> mapperMap = LOADED_MAPPER_MAP.get(pluginClass);
            Map<Class<?>, AbstractSender> senderMap = LOADED_SENDER_MAP.get(pluginClass);
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(service, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(service, configMap.get(field.getType()));
                    } else if (AbstractMapper.class.isAssignableFrom(field.getType())) {
                        field.set(service, mapperMap.get(field.getType()));
                    } else if (AbstractSender.class.isAssignableFrom(field.getType())) {
                        field.set(service, senderMap.get(field.getType()));
                    } else if (Location.class == field.getType()) {
                        field.set(service, configFileMap.get(autowired.file()).getLocation(autowired.path()[0]));
                    } else if (String.class == field.getType()) {
                        field.set(service, configFileMap.get(autowired.file()).getString(autowired.path()[0]));
                    } else if (List.class == field.getType()) {
                        field.set(service, configFileMap.get(autowired.file()).getStringList(autowired.path()[0]));
                    } else if (Set.class == field.getType()) {
                        field.set(service, configFileMap.get(autowired.file()).getStringSet(autowired.path()[0]));
                    } else if (long.class == field.getType()) {
                        field.set(service, configFileMap.get(autowired.file()).getLong(autowired.path()[0]));
                    } else if (int.class == field.getType()) {
                        field.set(service, configFileMap.get(autowired.file()).getInt(autowired.path()[0]));
                    } else if (double.class == field.getType()) {
                        field.set(service, configFileMap.get(autowired.file()).getDouble(autowired.path()[0]));
                    } else if (float.class == field.getType()) {
                        field.set(service, configFileMap.get(autowired.file()).getFloat(autowired.path()[0]));
                    }
                }
            }
            service.load();
            serviceMap.put(clazz, service);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadSkill(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin) throws Throwable {
        Class<?> pluginClass = plugin.getClass();
        Map<Class<?>, AbstractSkill> skillMap = LOADED_SKILL_MAP.computeIfAbsent(pluginClass, key -> new HashMap<>());
        if (!skillMap.containsKey(clazz)) {
            AbstractSkill skill = (AbstractSkill) clazz.getConstructor().newInstance();
            Map<Class<?>, AbstractConfig> configMap = LOADED_CONFIG_MAP.get(pluginClass);
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(pluginClass);
            Map<Class<?>, AbstractService> serviceMap = LOADED_SERVICE_MAP.get(pluginClass);
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(skill, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(skill, configMap.get(field.getType()));
                    } else if (AbstractService.class.isAssignableFrom(field.getType())) {
                        field.set(skill, serviceMap.get(field.getType()));
                    } else if (Location.class == field.getType()) {
                        field.set(skill, configFileMap.get(autowired.file()).getLocation(autowired.path()[0]));
                    } else if (String.class == field.getType()) {
                        field.set(skill, configFileMap.get(autowired.file()).getString(autowired.path()[0]));
                    } else if (List.class == field.getType()) {
                        field.set(skill, configFileMap.get(autowired.file()).getStringList(autowired.path()[0]));
                    } else if (Set.class == field.getType()) {
                        field.set(skill, configFileMap.get(autowired.file()).getStringSet(autowired.path()[0]));
                    } else if (long.class == field.getType()) {
                        field.set(skill, configFileMap.get(autowired.file()).getLong(autowired.path()[0]));
                    } else if (int.class == field.getType()) {
                        field.set(skill, configFileMap.get(autowired.file()).getInt(autowired.path()[0]));
                    } else if (double.class == field.getType()) {
                        field.set(skill, configFileMap.get(autowired.file()).getDouble(autowired.path()[0]));
                    } else if (float.class == field.getType()) {
                        field.set(skill, configFileMap.get(autowired.file()).getFloat(autowired.path()[0]));
                    }
                }
            }
            skill.load();
            skillMap.put(clazz, skill);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadCommand(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin, @NotNull Environment environment, boolean op, boolean combat, @NotNull String permission, @NotNull String head, @NotNull String @NotNull [] args) throws Throwable {
        Class<?> pluginClass = plugin.getClass();
        Map<Class<?>, AbstractCommand> commandMap = LOADED_COMMAND_MAP.computeIfAbsent(pluginClass, key -> new HashMap<>());
        if (!commandMap.containsKey(clazz)) {
            AbstractCommand command = (AbstractCommand) clazz.getConstructor().newInstance();
            Map<Class<?>, AbstractConfig> configMap = LOADED_CONFIG_MAP.get(pluginClass);
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(pluginClass);
            Map<Class<?>, AbstractService> serviceMap = LOADED_SERVICE_MAP.get(pluginClass);
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(command, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(command, configMap.get(field.getType()));
                    } else if (AbstractService.class.isAssignableFrom(field.getType())) {
                        field.set(command, serviceMap.get(field.getType()));
                    } else if (Location.class == field.getType()) {
                        field.set(command, configFileMap.get(autowired.file()).getLocation(autowired.path()[0]));
                    } else if (String.class == field.getType()) {
                        field.set(command, configFileMap.get(autowired.file()).getString(autowired.path()[0]));
                    } else if (List.class == field.getType()) {
                        field.set(command, configFileMap.get(autowired.file()).getStringList(autowired.path()[0]));
                    } else if (Set.class == field.getType()) {
                        field.set(command, configFileMap.get(autowired.file()).getStringSet(autowired.path()[0]));
                    } else if (long.class == field.getType()) {
                        field.set(command, configFileMap.get(autowired.file()).getLong(autowired.path()[0]));
                    } else if (int.class == field.getType()) {
                        field.set(command, configFileMap.get(autowired.file()).getInt(autowired.path()[0]));
                    } else if (double.class == field.getType()) {
                        field.set(command, configFileMap.get(autowired.file()).getDouble(autowired.path()[0]));
                    } else if (float.class == field.getType()) {
                        field.set(command, configFileMap.get(autowired.file()).getFloat(autowired.path()[0]));
                    }
                }
            }
            command.load(environment, op, combat, permission, head, args);
            command.register();
            commandMap.put(clazz, command);
            if (op) {
                OP_COMMAND_SET.add(head);
            } else {
                COMMAND_SET.add(head);
            }
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadTask(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin) throws Throwable {
        Class<?> pluginClass = plugin.getClass();
        Map<Class<?>, AbstractTask> taskMap = LOADED_TASK_MAP.computeIfAbsent(pluginClass, key -> new HashMap<>());
        if (!taskMap.containsKey(clazz)) {
            AbstractTask task = (AbstractTask) clazz.getConstructor().newInstance();
            Map<Class<?>, AbstractConfig> configMap = LOADED_CONFIG_MAP.get(pluginClass);
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(pluginClass);
            Map<Class<?>, AbstractService> serviceMap = LOADED_SERVICE_MAP.get(pluginClass);
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(task, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(task, configMap.get(field.getType()));
                    } else if (AbstractService.class.isAssignableFrom(field.getType())) {
                        field.set(task, serviceMap.get(field.getType()));
                    } else if (Location.class == field.getType()) {
                        field.set(task, configFileMap.get(autowired.file()).getLocation(autowired.path()[0]));
                    } else if (String.class == field.getType()) {
                        field.set(task, configFileMap.get(autowired.file()).getString(autowired.path()[0]));
                    } else if (List.class == field.getType()) {
                        field.set(task, configFileMap.get(autowired.file()).getStringList(autowired.path()[0]));
                    } else if (Set.class == field.getType()) {
                        field.set(task, configFileMap.get(autowired.file()).getStringSet(autowired.path()[0]));
                    } else if (long.class == field.getType()) {
                        field.set(task, configFileMap.get(autowired.file()).getLong(autowired.path()[0]));
                    } else if (int.class == field.getType()) {
                        field.set(task, configFileMap.get(autowired.file()).getInt(autowired.path()[0]));
                    } else if (double.class == field.getType()) {
                        field.set(task, configFileMap.get(autowired.file()).getDouble(autowired.path()[0]));
                    } else if (float.class == field.getType()) {
                        field.set(task, configFileMap.get(autowired.file()).getFloat(autowired.path()[0]));
                    }
                }
            }
            task.load();
            task.register();
            taskMap.put(clazz, task);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadListener(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin) throws Throwable {
        Class<?> pluginClass = plugin.getClass();
        Map<Class<?>, AbstractListener> listenerMap = LOADED_LISTENER_MAP.computeIfAbsent(pluginClass, key -> new HashMap<>());
        if (!listenerMap.containsKey(clazz)) {
            AbstractListener listener = (AbstractListener) clazz.getConstructor().newInstance();
            Map<Class<?>, AbstractConfig> configMap = LOADED_CONFIG_MAP.get(pluginClass);
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(pluginClass);
            Map<Class<?>, AbstractService> serviceMap = LOADED_SERVICE_MAP.get(pluginClass);
            Map<Class<?>, AbstractSkill> skillMap = LOADED_SKILL_MAP.get(pluginClass);
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(listener, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(listener, configMap.get(field.getType()));
                    } else if (AbstractService.class.isAssignableFrom(field.getType())) {
                        field.set(listener, serviceMap.get(field.getType()));
                    } else if (AbstractSkill.class.isAssignableFrom(field.getType())) {
                        field.set(listener, skillMap.get(field.getType()));
                    } else if (Location.class == field.getType()) {
                        field.set(listener, configFileMap.get(autowired.file()).getLocation(autowired.path()[0]));
                    } else if (String.class == field.getType()) {
                        field.set(listener, configFileMap.get(autowired.file()).getString(autowired.path()[0]));
                    } else if (List.class == field.getType()) {
                        field.set(listener, configFileMap.get(autowired.file()).getStringList(autowired.path()[0]));
                    } else if (Set.class == field.getType()) {
                        field.set(listener, configFileMap.get(autowired.file()).getStringSet(autowired.path()[0]));
                    } else if (long.class == field.getType()) {
                        field.set(listener, configFileMap.get(autowired.file()).getLong(autowired.path()[0]));
                    } else if (int.class == field.getType()) {
                        field.set(listener, configFileMap.get(autowired.file()).getInt(autowired.path()[0]));
                    } else if (double.class == field.getType()) {
                        field.set(listener, configFileMap.get(autowired.file()).getDouble(autowired.path()[0]));
                    } else if (float.class == field.getType()) {
                        field.set(listener, configFileMap.get(autowired.file()).getFloat(autowired.path()[0]));
                    }
                }
            }
            listener.load();
            listener.register();
            listenerMap.put(clazz, listener);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    public static boolean isCommand(@NotNull String command) {
        return COMMAND_SET.contains(command);
    }

    public static boolean isOpCommand(@NotNull String command) {
        return OP_COMMAND_SET.contains(command);
    }

    public static @NotNull Set<String> getCommands() {
        return COMMAND_SET;
    }

    public static @NotNull Set<String> getOpCommands() {
        return OP_COMMAND_SET;
    }
}
