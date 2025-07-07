package com.jodexindustries.jguiwrapper.api.item;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Обработчик предмета по модели данных.
 * @param <T> тип модели данных
 */
public interface DataHandler<T extends DataModel> {
    /**
     * Рендер предмета для GUI на основе модели данных.
     */
    @NotNull
    ItemWrapper render(@NotNull T model, @NotNull Player player);

    /**
     * Обработка клика по предмету.
     */
    default void onClick(@NotNull T model, @NotNull Player player) {}
} 