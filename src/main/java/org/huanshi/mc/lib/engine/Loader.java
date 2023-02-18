package org.huanshi.mc.lib.engine;

import org.bukkit.Bukkit;
import org.huanshi.mc.lib.AbstractPlugin;
import org.huanshi.mc.lib.annotation.Autowired;
import org.huanshi.mc.lib.exception.CircularDependencyException;
import org.huanshi.mc.lib.utils.ReflectUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Loader {
    private static final Map<Class<?>, Class<? extends Annotation>> ANNOTATION_MAP = new HashMap<>();
    private static final Map<Class<?>, Component> LOADED_COMPONENT_MAP = new HashMap<>();

    public static void register(@NotNull Class<? extends Component> clazz, @NotNull Class<? extends Annotation> annotationClass) {
        ANNOTATION_MAP.put(clazz, annotationClass);
    }

    public static void load(@NotNull AbstractPlugin plugin) throws Throwable {
        for (Class<?> clazz : ReflectUtils.getJarClasses(plugin.getClass())) {
            for (Map.Entry<Class<?>, Class<? extends Annotation>> entry : ANNOTATION_MAP.entrySet()) {
                if (entry.getKey().isAssignableFrom(clazz) && clazz.getAnnotation(entry.getValue()) != null) {
                    load(plugin, clazz, new HashSet<>(){{ add(clazz); }});
                }
            }
        }
    }

    private static @NotNull Component load(@NotNull AbstractPlugin plugin, @NotNull Class<?> clazz, @NotNull Set<Class<?>> autowiredClassSet) throws Throwable {
        Component component = LOADED_COMPONENT_MAP.get(clazz);
        if (component == null) {
            component = (Component) clazz.getConstructor().newInstance();
            for (Field field : ReflectUtils.getFields(clazz)) {
                field.setAccessible(true);
                if (field.getAnnotation(Autowired.class) != null) {
                    Class<?> fieldClass = field.getType();
                    if (AbstractPlugin.class.isAssignableFrom(fieldClass)) {
                        field.set(component, plugin);
                    } else {
                        for (Map.Entry<Class<?>, Class<? extends Annotation>> entry : ANNOTATION_MAP.entrySet()) {
                            if (entry.getKey().isAssignableFrom(fieldClass) && fieldClass.getAnnotation(entry.getValue()) != null) {
                                for (Class<?> autowiredClass : autowiredClassSet) {
                                    if (autowiredClass.isAssignableFrom(fieldClass)) {
                                        throw new CircularDependencyException(autowiredClassSet);
                                    }
                                }
                                autowiredClassSet.add(fieldClass);
                                field.set(component, load(plugin, fieldClass, autowiredClassSet));
                                autowiredClassSet.remove(fieldClass);
                            }
                        }
                    }
                }
            }
            component.load();
            component.onLoad();
            if (Registrable.class.isAssignableFrom(clazz)) {
                ((Registrable) component).register();
            }
            LOADED_COMPONENT_MAP.put(clazz, component);
            Bukkit.getLogger().info("[" + plugin.getName() + "] " + clazz.getName() + " 已加载");
        }
        return component;
    }
}
