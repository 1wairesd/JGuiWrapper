package com.jodexindustries.jguiwrapper.api.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Реестр переводов для GUI.
 */
public class TranslationRegistry {
    private static final Map<String, Map<String, String>> translations = new HashMap<>();

    /**
     * Регистрирует перевод по ключу и языку.
     */
    public static void register(String key, String lang, String value) {
        translations.computeIfAbsent(key, k -> new HashMap<>()).put(lang, value);
    }

    /**
     * Получить перевод по ключу и языку. Если нет перевода — вернуть ключ.
     */
    public static String get(String key, Locale locale) {
        String lang = locale.getLanguage();
        Map<String, String> map = translations.get(key);
        if (map == null) return key;
        return map.getOrDefault(lang, map.getOrDefault("en", key));
    }
} 