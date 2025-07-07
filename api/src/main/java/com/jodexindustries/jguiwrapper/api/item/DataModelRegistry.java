package com.jodexindustries.jguiwrapper.api.item;

import java.util.HashMap;
import java.util.Map;

/**
 * Реестр шаблонов DataModel по namespace:id
 */
public class DataModelRegistry {
    private static final Map<String, DataModel> models = new HashMap<>();

    public static void register(String namespacedId, DataModel model) {
        models.put(namespacedId, model);
    }

    public static DataModel get(String namespacedId) {
        return models.get(namespacedId);
    }
} 