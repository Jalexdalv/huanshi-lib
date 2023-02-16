package org.huanshi.mc.lib;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
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
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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

    public static void load(@NotNull AbstractPlugin plugin) throws IOException, ClassNotFoundException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Class<?>> senderClassList = new LinkedList<>(), receiverClassList = new LinkedList<>(), mapperClassList = new LinkedList<>(), serviceClassList = new LinkedList<>(), taskClassList = new LinkedList<>(), listenerClassList = new LinkedList<>();
        Map<Class<?>, Config> configClassMap = new HashMap<>();
        Map<Class<?>, Command> commandClassMap = new HashMap<>();
        for (Class<?> clazz : ReflectUtils.getJarClasses(plugin.getClass())) {
            Config config = clazz.getAnnotation(Config.class);
            if (config != null && AbstractConfig.class.isAssignableFrom(clazz)) {
                configClassMap.put(clazz, config);
            }
            if (clazz.getAnnotation(Sender.class) != null && AbstractSender.class.isAssignableFrom(clazz)) {
                senderClassList.add(clazz);
            }
            if (clazz.getAnnotation(Receiver.class) != null && AbstractReceiver.class.isAssignableFrom(clazz)) {
                receiverClassList.add(clazz);
            }
            if (clazz.getAnnotation(Mapper.class) != null && AbstractMapper.class.isAssignableFrom(clazz)) {
                mapperClassList.add(clazz);
            }
            if (clazz.getAnnotation(Service.class) != null && AbstractService.class.isAssignableFrom(clazz)) {
                serviceClassList.add(clazz);
            }
            Command command = clazz.getAnnotation(Command.class);
            if (command != null && AbstractCommand.class.isAssignableFrom(clazz)) {
                commandClassMap.put(clazz, command);
            }
            if (clazz.getAnnotation(Task.class) != null && AbstractTask.class.isAssignableFrom(clazz)) {
                taskClassList.add(clazz);
            }
            if (clazz.getAnnotation(Listener.class) != null && AbstractListener.class.isAssignableFrom(clazz)) {
                listenerClassList.add(clazz);
            }
        }
        for (Map.Entry<Class<?>, Config> entry : configClassMap.entrySet()) {
            loadConfig(plugin, entry.getKey(), StringUtils.trimToNull(entry.getValue().file()));
        }
        for (Class<?> clazz : senderClassList) {
            loadSender(plugin, clazz);
        }
        for (Class<?> clazz : receiverClassList) {
            loadReceiver(plugin, clazz);
        }
        for (Class<?> clazz : mapperClassList) {
            loadMapper(plugin, clazz);
        }
        for (Class<?> clazz : serviceClassList) {
            loadService(plugin, clazz);
        }
        for (Map.Entry<Class<?>, Command> entry : commandClassMap.entrySet()) {
            Command command = entry.getValue();
            for (int i = 0, len = command.args().length; i < len; i++) {
                command.args()[i] = StringUtils.trimToNull(command.args()[i]);
            }
            loadCommand(plugin, entry.getKey(), command.environment(), command.op(), command.combat(), StringUtils.trimToNull(command.permission()), StringUtils.trimToNull(command.head()), command.args());
        }
        for (Class<?> clazz : taskClassList) {
            loadTask(plugin, clazz);
        }
        for (Class<?> clazz : listenerClassList) {
            loadListener(plugin, clazz);
        }
    }

    private static void loadConfig(@NotNull AbstractPlugin plugin, @NotNull Class<?> clazz, @NotNull String file) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException, IOException {
        AbstractConfig config = (AbstractConfig) clazz.getConstructor().newInstance();
        for (Field field : ReflectUtils.getFields(clazz)) {
            field.setAccessible(true);
            Data data = field.getAnnotation(Data.class);
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (data != null) {
                field.set(config, Loader.class.getDeclaredField(data.name()).get(null));
            } else if (autowired != null) {
                if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                    field.set(config, plugin);
                }
            }
        }
        config.load(new File(plugin.getDataFolder(), file), Objects.requireNonNull(plugin.getResource(file)));
        LOADED_CONFIG_MAP.put(clazz, config);
        CONFIG_FILE_MAP.computeIfAbsent(plugin.getClass(), key -> new HashMap<>()).put(file, config);
        plugin.getLogger().info("Config: " + clazz.getName() + " 加载成功");
    }

    private static void loadConfiguration(@NotNull AbstractPlugin plugin, @NotNull Component component, @NotNull Field field, @NotNull Autowired autowired) throws IllegalAccessException {
        Map<String, AbstractConfig> configFileMap = CONFIG_FILE_MAP.get(plugin.getClass());
        if (Location.class == field.getType()) {
            field.set(component, configFileMap.get(autowired.file()).getLocation(autowired.path()[0]));
        } else if (String.class == field.getType()) {
            field.set(component, configFileMap.get(autowired.file()).getString(autowired.path()[0]));
        } else if (List.class == field.getType()) {
            field.set(component, configFileMap.get(autowired.file()).getStringList(autowired.path()[0]));
        } else if (Set.class == field.getType()) {
            field.set(component, configFileMap.get(autowired.file()).getStringSet(autowired.path()[0]));
        } else if (long.class == field.getType()) {
            field.set(component, configFileMap.get(autowired.file()).getLong(autowired.path()[0]));
        } else if (int.class == field.getType()) {
            field.set(component, configFileMap.get(autowired.file()).getInt(autowired.path()[0]));
        } else if (double.class == field.getType()) {
            field.set(component, configFileMap.get(autowired.file()).getDouble(autowired.path()[0]));
        } else if (float.class == field.getType()) {
            field.set(component, configFileMap.get(autowired.file()).getFloat(autowired.path()[0]));
        }
    }

    private static void loadSender(@NotNull AbstractPlugin plugin, @NotNull Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        AbstractSender sender = (AbstractSender) clazz.getConstructor().newInstance();
        for (Field field : ReflectUtils.getFields(clazz)) {
            field.setAccessible(true);
            Data data = field.getAnnotation(Data.class);
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (data != null) {
                field.set(sender, Loader.class.getDeclaredField(data.name()).get(null));
            } else if (autowired != null) {
                if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                    field.set(sender, plugin);
                } else {
                    loadConfiguration(plugin, sender, field, autowired);
                }
            }
        }
        sender.load();
        sender.register();
        LOADED_SENDER_MAP.put(clazz, sender);
        plugin.getLogger().info("Sender: " + clazz.getName() + " 加载成功");
    }

    private static void loadReceiver(@NotNull AbstractPlugin plugin, @NotNull Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        AbstractReceiver receiver = (AbstractReceiver) clazz.getConstructor().newInstance();
        for (Field field : ReflectUtils.getFields(clazz)) {
            field.setAccessible(true);
            Data data = field.getAnnotation(Data.class);
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (data != null) {
                field.set(receiver, Loader.class.getDeclaredField(data.name()).get(null));
            } else if (autowired != null) {
                if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                    field.set(receiver, plugin);
                } else {
                    loadConfiguration(plugin, receiver, field, autowired);
                }
            }
        }
        receiver.load();
        receiver.register();
        LOADED_RECEIVER_MAP.put(clazz, receiver);
        plugin.getLogger().info("Receiver: " + clazz.getName() + " 加载成功");
    }

    private static void loadMapper(@NotNull AbstractPlugin plugin, @NotNull Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        AbstractMapper mapper = (AbstractMapper) clazz.getConstructor().newInstance();
        for (Field field : ReflectUtils.getFields(clazz)) {
            field.setAccessible(true);
            Data data = field.getAnnotation(Data.class);
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (data != null) {
                field.set(mapper, Loader.class.getDeclaredField(data.name()).get(null));
            } else if (autowired != null) {
                if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                    field.set(mapper, plugin);
                } else {
                    loadConfiguration(plugin, mapper, field, autowired);
                }
            }
        }
        mapper.load();
        LOADED_MAPPER_MAP.put(clazz, mapper);
        plugin.getLogger().info("Mapper: " + clazz.getName() + " 加载成功");
    }

    private static void loadService(@NotNull AbstractPlugin plugin, @NotNull Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        AbstractService service = (AbstractService) clazz.getConstructor().newInstance();
        for (Field field : ReflectUtils.getFields(clazz)) {
            field.setAccessible(true);
            Data data = field.getAnnotation(Data.class);
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (data != null) {
                field.set(service, Loader.class.getDeclaredField(data.name()).get(null));
            } else if (autowired != null) {
                if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                    field.set(service, plugin);
                } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                    field.set(service, LOADED_CONFIG_MAP.get(field.getType()));
                } else if (AbstractSender.class.isAssignableFrom(field.getType())) {
                    field.set(service, LOADED_SENDER_MAP.get(field.getType()));
                } else if (AbstractMapper.class.isAssignableFrom(field.getType())) {
                    field.set(service, LOADED_MAPPER_MAP.get(field.getType()));
                } else {
                    loadConfiguration(plugin, service, field, autowired);
                }
            }
        }
        service.load();
        LOADED_SERVICE_MAP.put(clazz, service);
        plugin.getLogger().info("Service: " + clazz.getName() + " 加载成功");
    }

    private static void loadCommand(@NotNull AbstractPlugin plugin, @NotNull Class<?> clazz, @NotNull Environment environment, boolean op, boolean combat, @NotNull String permission, @NotNull String head, @NotNull String @NotNull [] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        AbstractCommand command = (AbstractCommand) clazz.getConstructor().newInstance();
        for (Field field : ReflectUtils.getFields(clazz)) {
            field.setAccessible(true);
            Data data = field.getAnnotation(Data.class);
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (data != null) {
                field.set(command, Loader.class.getDeclaredField(data.name()).get(null));
            } else if (autowired != null) {
                if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                    field.set(command, plugin);
                } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                    field.set(command, LOADED_CONFIG_MAP.get(field.getType()));
                } else if (AbstractSender.class.isAssignableFrom(field.getType())) {
                    field.set(command, LOADED_SENDER_MAP.get(field.getType()));
                } else if (AbstractService.class.isAssignableFrom(field.getType())) {
                    field.set(command, LOADED_SERVICE_MAP.get(field.getType()));
                } else {
                    loadConfiguration(plugin, command, field, autowired);
                }
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
        plugin.getLogger().info("Command: " + clazz.getName() + " 加载成功");
    }

    private static void loadTask(@NotNull AbstractPlugin plugin, @NotNull Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        AbstractTask task = (AbstractTask) clazz.getConstructor().newInstance();
        for (Field field : ReflectUtils.getFields(clazz)) {
            field.setAccessible(true);
            Data data = field.getAnnotation(Data.class);
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (data != null) {
                field.set(task, Loader.class.getDeclaredField(data.name()).get(null));
            } else if (autowired != null) {
                if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                    field.set(task, plugin);
                } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                    field.set(task, LOADED_CONFIG_MAP.get(field.getType()));
                } else if (AbstractSender.class.isAssignableFrom(field.getType())) {
                    field.set(task, LOADED_SENDER_MAP.get(field.getType()));
                } else if (AbstractService.class.isAssignableFrom(field.getType())) {
                    field.set(task, LOADED_SERVICE_MAP.get(field.getType()));
                } else {
                    loadConfiguration(plugin, task, field, autowired);
                }
            }
        }
        task.load();
        task.register();
        LOADED_TASK_MAP.put(clazz, task);
        plugin.getLogger().info("Task: " + clazz.getName() + " 加载成功");
    }

    private static void loadListener(@NotNull AbstractPlugin plugin, @NotNull Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        AbstractListener listener = (AbstractListener) clazz.getConstructor().newInstance();
        for (Field field : ReflectUtils.getFields(clazz)) {
            field.setAccessible(true);
            Data data = field.getAnnotation(Data.class);
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (data != null) {
                field.set(listener, Loader.class.getDeclaredField(data.name()).get(null));
            } else if (autowired != null) {
                if (AbstractPlugin.class.isAssignableFrom(field.getType())) {
                    field.set(listener, plugin);
                } else if (AbstractConfig.class.isAssignableFrom(field.getType())) {
                    field.set(listener, LOADED_CONFIG_MAP.get(field.getType()));
                } else if (AbstractSender.class.isAssignableFrom(field.getType())) {
                    field.set(listener, LOADED_SENDER_MAP.get(field.getType()));
                } else if (AbstractService.class.isAssignableFrom(field.getType())) {
                    field.set(listener, LOADED_SERVICE_MAP.get(field.getType()));
                } else {
                    loadConfiguration(plugin, listener, field, autowired);
                }
            }
        }
        listener.load();
        listener.register();
        LOADED_LISTENER_MAP.put(clazz, listener);
        plugin.getLogger().info("Listener: " + clazz.getName() + " 加载成功");
    }
}
