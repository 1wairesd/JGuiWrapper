package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.api.item.DataHandler;
import com.jodexindustries.jguiwrapper.api.item.DataModel;
import com.jodexindustries.jguiwrapper.api.item.ItemHandlerRegistry;
import com.jodexindustries.jguiwrapper.api.i18n.TranslationRegistry;
import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.jodexindustries.jguiwrapper.api.item.DataModelRegistry;

import java.util.stream.IntStream;

public class TestAdvancedGui extends AdvancedGui {

    private int clicks;

    public TestAdvancedGui() {
        super("&cAdvanced gui");

        // Регистрация перевода
        TranslationRegistry.register("test.button", "en", "Click me!");
        TranslationRegistry.register("test.button", "ru", "Нажми меня!");

        // Регистрация обработчика
        ItemHandlerRegistry.register("test:button", new DataHandler<TestButtonModel>() {
            @Override
            public ItemWrapper render(TestButtonModel model, Player player) {
                String label = TranslationRegistry.get("test.button", player.locale());
                return ItemWrapper.builder(Material.GOLD_BLOCK)
                        .displayName(Component.text(label + " (" + model.getClicks() + ")"))
                        .build();
            }
            @Override
            public void onClick(TestButtonModel model, Player player) {
                model.increment();
                player.sendMessage("Clicked! " + model.getClicks());
                rerenderMetaItems(player);
            }
        });

        // Регистрируем DataModel заранее
        DataModelRegistry.register("test:button", new TestButtonModel());

        // Используем модель из реестра при создании меню
        addMetaItem("test:button", (TestButtonModel) DataModelRegistry.get("test:button"), 0);

        onClose(event -> {
            event.getPlayer().sendMessage("Closed");
        });

        onOpen(event -> {
            event.getPlayer().sendMessage("Opened");
        });

        registerItem("test", builder -> {
            builder.withSlots(IntStream.range(0, size() / 2).toArray())
                    .withDefaultItem(ItemWrapper.builder(Material.GOLD_BLOCK).build())
                    .withDefaultClickHandler((event, controller) -> {
                        event.setCancelled(true);

                        clicks++;

                        controller.updateAllItemWrappers(itemWrapper -> {
                            itemWrapper.displayName(Component.text(clicks));
                        });

                        title("&cAdvanced gui clicked: &a" + clicks + " &ctimes");
                        updateMenu();
                    });
        });

        registerItem("close", builder -> {
            builder.withSlots(size() - 1)
                    .withDefaultItem(ItemWrapper.builder(Material.BARRIER)
                            .displayName(LEGACY_AMPERSAND.deserialize("&cClose")).build())
                    .withDefaultClickHandler((event, controller) -> {
                        event.setCancelled(true);

                        close(event.getWhoClicked());
                    });
        });

    }

    // Пример DataModel
    public static class TestButtonModel implements DataModel {
        private int clicks = 0;
        public int getClicks() { return clicks; }
        public void increment() { clicks++; }
    }
}