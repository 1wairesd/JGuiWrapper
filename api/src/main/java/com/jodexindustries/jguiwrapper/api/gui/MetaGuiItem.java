package com.jodexindustries.jguiwrapper.api.gui;

import com.jodexindustries.jguiwrapper.api.item.DataModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Описание предмета для динамического GUI: namespace:id, DataModel, слот.
 */
public class MetaGuiItem {
    private final String namespacedId;
    private final DataModel dataModel;
    private final int slot;

    public MetaGuiItem(@NotNull String namespacedId, @NotNull DataModel dataModel, int slot) {
        this.namespacedId = namespacedId;
        this.dataModel = dataModel;
        this.slot = slot;
    }

    @NotNull
    public String getNamespacedId() {
        return namespacedId;
    }

    @NotNull
    public DataModel getDataModel() {
        return dataModel;
    }

    public int getSlot() {
        return slot;
    }
} 