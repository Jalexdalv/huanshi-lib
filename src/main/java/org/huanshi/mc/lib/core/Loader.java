package org.huanshi.mc.lib.core;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.annotation.Command;
import org.huanshi.mc.lib.annotation.Config;
import org.huanshi.mc.lib.annotation.Data;
import org.huanshi.mc.lib.annotation.Listener;
import org.huanshi.mc.lib.annotation.Mapper;
import org.huanshi.mc.lib.annotation.Receiver;
import org.huanshi.mc.lib.annotation.Sender;
import org.huanshi.mc.lib.annotation.Service;
import org.huanshi.mc.lib.annotation.Task;
import org.huanshi.mc.lib.command.AbstractCommand;
import org.huanshi.mc.lib.command.Environment;
import org.huanshi.mc.lib.config.AbstractConfig;
import org.huanshi.mc.lib.listener.AbstractListener;
import org.huanshi.mc.lib.mapper.AbstractMapper;
import org.huanshi.mc.lib.protocol.AbstractReceiver;
import org.huanshi.mc.lib.protocol.AbstractSender;
import org.huanshi.mc.lib.service.AbstractService;
import org.huanshi.mc.lib.task.AbstractTask;
import org.huanshi.mc.lib.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Loader {
    private static final Map<Class<?>, AbstractConfig> LOADED_CONFIG_MAP = new HashMap<>();
    private static final Map<Class<?>, AbstractSender> LOADED_SENDER_MAP = new HashMap<>();
    private static final Map<Class<?>, AbstractReceiver> LOADED_RECEIVER_MAP = new HashMap<>();
    private static final Map<Class<?>, AbstractMapper> LOADED_MAPPER_MAP = new HashMap<>();
    private static final Map<Class<?>, AbstractService> LOADED_SERVICE_MAP = new HashMap<>();
    private static final Map<Class<?>, AbstractCommand> LOADED_COMMAND_MAP = new HashMap<>();
    private static final Map<Class<?>, AbstractTask> LOADED_TASK_MAP = new HashMap<>();
    private static final Map<Class<?>, AbstractListener> LOADED_LISTENER_MAP = new HashMap<>();
    private static final Map<Class<?>, Map<String, AbstractConfig>> CONFIG_FILE_MAP = new HashMap<>();
    private static final Set<String> COMMAND_SET = new HashSet<>(), OP_COMMAND_SET = new HashSet<>();

    public static void load(@NotNull AbstractPlugin plugin) throws Throwable {
        Set<Class<?>> senderClassSet = new HashSet<>(), receiverClassSet = new HashSet<>(), mapperClassSet = new HashSet<>(), serviceClassSet = new HashSet<>(), taskClassSet = new HashSet<>(), listenerClassSet = new HashSet<>();
        Map<Class<?>, Config> configClassMap = new HashMap<>();
        Map<Class<?>, Command> commandClassMap = new HashMap<>();
        for (Class<?> clazz : ReflectUtils.getJarClasses(plugin.getClass())) {
            Config config = clazz.getAnnotation(Config.class);
            Sender sender = clazz.getAnnotation(Sender.class);
            Receiver receiver = clazz.getAnnotation(Receiver.class);
            Mapper mapper = clazz.getAnnotation(Mapper.class);
            Service service = clazz.getAnnotation(Service.class);
            Command command = clazz.getAnnotation(Command.class);
            Task task = clazz.getAnnotation(Task.class);
            Listener listener = clazz.getAnnotation(Listener.class);
            if (config != null && sender == null && receiver == null && mapper == null && service == null && command == null && task == null && listener == null && AbstractConfig.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                configClassMap.put(clazz, config);
            } else if (sender != null && config == null && receiver == null && mapper == null && service == null && command == null && task == null && listener == null && AbstractSender.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                senderClassSet.add(clazz);
            } else if (receiver != null && config == null && sender == null && mapper == null && service == null && command == null && task == null && listener == null && AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                receiverClassSet.add(clazz);
            } else if (mapper != null && config == null && sender == null && receiver == null && service == null && command == null && task == null && listener == null && AbstractMapper.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                mapperClassSet.add(clazz);
            } else if (service != null && config == null && sender == null && receiver == null && mapper == null && command == null && task == null && listener == null && AbstractService.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                serviceClassSet.add(clazz);
            } else if (command != null && config == null && sender == null && receiver == null && mapper == null && service == null && task == null && listener == null && AbstractCommand.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                commandClassMap.put(clazz, command);
            } else if (task != null && config == null && sender == null && receiver == null && mapper == null && service == null && command == null && listener == null && AbstractTask.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractListener.class.isAssignableFrom(clazz)) {
                taskClassSet.add(clazz);
            } else if (listener != null && config == null && sender == null && receiver == null && mapper == null && service == null && command == null && task == null && AbstractListener.class.isAssignableFrom(clazz) && !AbstractConfig.class.isAssignableFrom(clazz) && !AbstractSender.class.isAssignableFrom(clazz) && !AbstractReceiver.class.isAssignableFrom(clazz) && !AbstractMapper.class.isAssignableFrom(clazz) && !AbstractService.class.isAssignableFrom(clazz) && !AbstractCommand.class.isAssignableFrom(clazz) && !AbstractTask.class.isAssignableFrom(clazz)) {
                listenerClassSet.add(clazz);
            }
        }
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
        Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.computeIfAbsent(plugin.getClass(), key -> new HashMap<>());
        if (!LOADED_CONFIG_MAP.containsKey(clazz)) {
            AbstractConfig config = (AbstractConfig) clazz.getConstructor().newInstance();
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Data data = field.getAnnotation(Data.class);
                if (field.getAnnotation(Autowired.class) != null && AbstractPlugin.class.isAssignableFrom(field.getType())) {
                    field.set(config, plugin);
                } else if (data != null) {
                    field.set(config, Loader.class.getDeclaredField(data.name()).get(null));
                }
            }
            config.load(new File(plugin.getDataFolder(), file), Objects.requireNonNull(clazz.getResource(resource)).openStream());
            LOADED_CONFIG_MAP.put(clazz, config);
            configFileMap.put(file, config);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadSender(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin) throws Throwable {
        if (!LOADED_SENDER_MAP.containsKey(clazz)) {
            AbstractSender sender = (AbstractSender) clazz.getConstructor().newInstance();
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(plugin.getClass());
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                Data data = field.getAnnotation(Data.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(sender, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(sender, LOADED_CONFIG_MAP.get(field.getType()));
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
                } else if (data != null) {
                    field.set(sender, Loader.class.getDeclaredField(data.name()).get(null));
                }
            }
            sender.load();
            sender.register();
            LOADED_SENDER_MAP.put(clazz, sender);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadReceiver(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin) throws Throwable {
        if (!LOADED_RECEIVER_MAP.containsKey(clazz)) {
            AbstractReceiver receiver = (AbstractReceiver) clazz.getConstructor().newInstance();
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(plugin.getClass());
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                Data data = field.getAnnotation(Data.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(receiver, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(receiver, LOADED_CONFIG_MAP.get(field.getType()));
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
                    } else if (data != null) {
                        field.set(receiver, Loader.class.getDeclaredField(data.name()).get(null));
                    }
                }
            }
            receiver.load();
            receiver.register();
            LOADED_RECEIVER_MAP.put(clazz, receiver);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadMapper(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin) throws Throwable {
        if (!LOADED_MAPPER_MAP.containsKey(clazz)) {
            AbstractMapper mapper = (AbstractMapper) clazz.getConstructor().newInstance();
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(plugin.getClass());
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                Data data = field.getAnnotation(Data.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(mapper, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(mapper, LOADED_CONFIG_MAP.get(field.getType()));
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
                } else if (data != null) {
                    field.set(mapper, Loader.class.getDeclaredField(data.name()).get(null));
                }
            }
            mapper.load();
            LOADED_MAPPER_MAP.put(clazz, mapper);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadService(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin) throws Throwable {
        if (!LOADED_SERVICE_MAP.containsKey(clazz)) {
            AbstractService service = (AbstractService) clazz.getConstructor().newInstance();
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(plugin.getClass());
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                Data data = field.getAnnotation(Data.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(service, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(service, LOADED_CONFIG_MAP.get(field.getType()));
                    } else if (AbstractMapper.class.isAssignableFrom(field.getType())) {
                        field.set(service, LOADED_MAPPER_MAP.get(field.getType()));
                    } else if (AbstractSender.class.isAssignableFrom(field.getType())) {
                        field.set(service, LOADED_SENDER_MAP.get(field.getType()));
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
                } else if (data != null) {
                    field.set(service, Loader.class.getDeclaredField(data.name()).get(null));
                }
            }
            service.load();
            LOADED_SERVICE_MAP.put(clazz, service);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadCommand(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin, @NotNull Environment environment, boolean op, boolean combat, @NotNull String permission, @NotNull String head, @NotNull String @NotNull [] args) throws Throwable {
        if (!LOADED_COMMAND_MAP.containsKey(clazz)) {
            AbstractCommand command = (AbstractCommand) clazz.getConstructor().newInstance();
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(plugin.getClass());
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                Data data = field.getAnnotation(Data.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(command, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(command, LOADED_CONFIG_MAP.get(field.getType()));
                    } else if (AbstractService.class.isAssignableFrom(field.getType())) {
                        field.set(command, LOADED_SERVICE_MAP.get(field.getType()));
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
                } else if (data != null) {
                    field.set(command, Loader.class.getDeclaredField(data.name()).get(null));
                }
            }
            command.load(environment, op, combat, permission, head, args);
            command.register();
            LOADED_COMMAND_MAP.put(clazz, command);
            if (op) {
                OP_COMMAND_SET.add(head);
            } else {
                COMMAND_SET.add(head);
            }
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadTask(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin) throws Throwable {
        if (!LOADED_TASK_MAP.containsKey(clazz)) {
            AbstractTask task = (AbstractTask) clazz.getConstructor().newInstance();
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(plugin.getClass());
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                Data data = field.getAnnotation(Data.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(task, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(task, LOADED_CONFIG_MAP.get(field.getType()));
                    } else if (AbstractService.class.isAssignableFrom(field.getType())) {
                        field.set(task, LOADED_SERVICE_MAP.get(field.getType()));
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
                } else if (data != null) {
                    field.set(task, Loader.class.getDeclaredField(data.name()).get(null));
                }
            }
            task.load();
            task.register();
            LOADED_TASK_MAP.put(clazz, task);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }

    private static void loadListener(@NotNull Class<?> clazz, @NotNull AbstractPlugin plugin) throws Throwable {
        if (!LOADED_LISTENER_MAP.containsKey(clazz)) {
            AbstractListener listener = (AbstractListener) clazz.getConstructor().newInstance();
            Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(plugin.getClass());
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                Data data = field.getAnnotation(Data.class);
                if (autowired != null) {
                    if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                        field.set(listener, plugin);
                    } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                        field.set(listener, LOADED_CONFIG_MAP.get(field.getType()));
                    } else if (AbstractService.class.isAssignableFrom(field.getType())) {
                        field.set(listener, LOADED_SERVICE_MAP.get(field.getType()));
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
                } else if (data != null) {
                    field.set(listener, Loader.class.getDeclaredField(data.name()).get(null));
                }
            }
            listener.load();
            listener.register();
            LOADED_LISTENER_MAP.put(clazz, listener);
            plugin.getLogger().info(clazz.getName() + " 加载成功");
        }
    }
}
