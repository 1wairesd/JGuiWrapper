package com.jodexindustries.jguiwrapper.gui;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import com.jodexindustries.jguiwrapper.api.item.DataModel;
import com.jodexindustries.jguiwrapper.api.nms.NMSWrapper;
import com.jodexindustries.jguiwrapper.api.item.DataHandler;
import com.jodexindustries.jguiwrapper.api.item.ItemHandlerRegistry;
import com.jodexindustries.jguiwrapper.api.gui.MetaGuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGui implements Gui {

    public static final LegacyComponentSerializer LEGACY_AMPERSAND = LegacyComponentSerializer.legacyAmpersand();
    private static final NMSWrapper NMS_WRAPPER = GuiApi.get().getNMSWrapper();

    private int size;
    private Component title;
    private InventoryType type;

    private GuiHolder holder;
    private final List<MetaGuiItem> metaItems = new ArrayList<>();

    public AbstractGui(@NotNull String title) {
        this(54, title);
    }

    public AbstractGui(int size, @NotNull String title) {
        this(size, LEGACY_AMPERSAND.deserialize(title));
    }

    public AbstractGui(@NotNull Component title) {
        this(InventoryType.CHEST, title);
    }

    public AbstractGui(@NotNull InventoryType type, @NotNull Component title) {
        this(type.getDefaultSize(), type, title);
    }

    public AbstractGui(int size, @NotNull Component title) {
        this(size, null, title);
    }

    private AbstractGui(int size, @Nullable InventoryType type, @NotNull Component title) {
        this.size =  adaptSize(size);
        this.title = title;
        this.holder = new GuiHolder(this, type);
        this.type = holder.getInventory().getType();

        INSTANCES.add(new WeakReference<>(this));
    }

    public final int size() {
        return size;
    }

    public final void size(int size) {
        this.size = adaptSize(size);
    }

    public final @NotNull Component title() {
        return title;
    }

    public final void title(@NotNull Component title) {
        this.title = title;
    }

    public final void title(@NotNull String title) {
        this.title = LEGACY_AMPERSAND.deserialize(title);
    }

    public final void type(@NotNull InventoryType type) {
        this.type = type;
    }

    @NotNull
    public final InventoryType type() {
        return type;
    }

    @Override
    public final @NotNull GuiHolder holder() {
        return holder;
    }

    @Override
    public final void open(@NotNull HumanEntity player) {
        open(player, title);
    }

    @Override
    public final void open(@NotNull HumanEntity player, Component title) {
        InventoryView view = NMS_WRAPPER.openInventory(player, holder.getInventory(), type, size, title);

        if (view == null) {
            player.openInventory(holder.getInventory());
        } else {
            onOpen(new InventoryOpenEvent(view));
        }
    }

    @Override
    public final void close(@NotNull HumanEntity player) {
        InventoryCloseEvent.Reason reason = InventoryCloseEvent.Reason.PLUGIN;
        onClose(new InventoryCloseEvent(player.getOpenInventory(), reason));
        player.closeInventory(reason);
    }

    public void onOpen(@NotNull InventoryOpenEvent event) {}

    public void onClose(@NotNull InventoryCloseEvent event) {}

    public void onClick(@NotNull InventoryClickEvent event) {}

    public void onDrag(@NotNull InventoryDragEvent event) {}

    public final void updateMenu() {
        updateMenu(this.type, this.size, this.title);
    }

    public final void updateMenu(HumanEntity player) {
        updateMenu(player, this.type, this.size, this.title);
    }

    public final void updateMenu(Component title) {
        updateMenu(null, this.size, title);
    }

    public final void updateMenu(InventoryType type) {
        updateMenu(type, this.size);
    }

    public final void updateMenu(InventoryType type, int size) {
        updateMenu(type, size, null);
    }

    public final void updateMenu(InventoryType type, int size, Component title) {
        this.holder.getInventory().getViewers().forEach(humanEntity -> updateMenu(humanEntity, type, size, title));
    }

    public final void updateMenu(@NotNull HumanEntity player, Component title) {
        updateMenu(player, null, this.size, title);
    }

    public final void updateMenu(@NotNull HumanEntity player, InventoryType type) {
        updateMenu(player, type, this.size, null);
    }

    public final void updateMenu(@NotNull HumanEntity player, InventoryType type, int size) {
        updateMenu(player, type, size, null);
    }

    public final void updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type, int size, @Nullable Component title) {
        NMS_WRAPPER.updateMenu(player, type, size, title);
    }

    @Override
    public final void updateHolder() {
        List<HumanEntity> viewers = new ArrayList<>(this.holder.getInventory().getViewers());

        close();

        this.holder = new GuiHolder(this, type);

        viewers.forEach(this::open);
    }

    /**
     * Добавить предмет в GUI по namespace:id, DataModel и слоту.
     */
    public void addMetaItem(@NotNull String namespacedId, @NotNull DataModel dataModel, int slot) {
        metaItems.add(new MetaGuiItem(namespacedId, dataModel, slot));
        renderMetaItem(namespacedId, dataModel, slot);
    }

    /**
     * Обновить предмет в GUI по namespace:id, DataModel и слоту.
     */
    public void updateMetaItem(@NotNull String namespacedId, @NotNull DataModel dataModel, int slot) {
        metaItems.removeIf(item -> item.getSlot() == slot);
        addMetaItem(namespacedId, dataModel, slot);
    }

    /**
     * Перерисовать все metaItems (например, после смены языка или данных).
     */
    public void rerenderMetaItems(@NotNull Player player) {
        for (MetaGuiItem item : metaItems) {
            renderMetaItem(item.getNamespacedId(), item.getDataModel(), item.getSlot(), player);
        }
    }

    /**
     * Внутренний рендер предмета через DataHandler.
     */
    private void renderMetaItem(@NotNull String namespacedId, @NotNull DataModel dataModel, int slot) {
        for (HumanEntity viewer : holder.getInventory().getViewers()) {
            if (viewer instanceof Player player) {
                renderMetaItem(namespacedId, dataModel, slot, player);
            }
        }
    }
    private void renderMetaItem(@NotNull String namespacedId, @NotNull DataModel dataModel, int slot, @NotNull Player player) {
        DataHandler handler = ItemHandlerRegistry.get(namespacedId);
        if (handler != null) {
            holder.getInventory().setItem(slot, handler.render(dataModel, player).itemStack());
        }
    }

    protected static int adaptSize(int size) {
        return ((Math.min(Math.max(size, 1), 54) + 8) / 9) * 9;
    }

}
