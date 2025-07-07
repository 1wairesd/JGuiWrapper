package com.jodexindustries.jguiwrapper.api.item;

import java.util.HashMap;
import java.util.Map;

/**
 * Реестр обработчиков предметов по namespace:id
 */
public class ItemHandlerRegistry {
    private static final Map<String, DataHandler<?>> handlers = new HashMap<>();

    public static void register(String namespacedId, DataHandler<?> handler) {
        handlers.put(namespacedId, handler);
    }

    public static DataHandler<?> get(String namespacedId) {
        return handlers.get(namespacedId);
    }
} 