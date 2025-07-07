package com.jodexindustries.jguiwrapper.api.item;

import net.kyori.adventure.key.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * Реестр шаблонов DataModel по Key (adventure key)
 */
public class DataModelRegistry {
    private static final Map<Key, DataModel> models = new HashMap<>();

    public static void register(Key key, DataModel model) {
        models.put(key, model);
    }

    public static DataModel get(Key key) {
        return models.get(key);
    }

    /**
     * Для совместимости: регистрация по строке (namespace:id)
     */
    public static void register(String namespacedId, DataModel model) {
        register(Key.key(namespacedId), model);
    }
    public static DataModel get(String namespacedId) {
        return get(Key.key(namespacedId));
    }
} 