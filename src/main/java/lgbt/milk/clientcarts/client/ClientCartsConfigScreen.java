package lgbt.milk.clientcarts.client;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.Color;

public final class ClientCartsConfigScreen {
    public static Screen create(Screen parent) {
        ClientCartsConfig config = ClientCartsConfig.get();
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("ClientCarts"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("General"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Mod Enabled"))
                                .binding(true, () -> config.modEnabled, value -> config.modEnabled = value)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Color Enabled"))
                                .binding(false, () -> config.colorEnabled, value -> config.colorEnabled = value)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Component.literal("Overlay Color"))
                                .binding(Color.WHITE, () -> new Color(config.overlayColor),
                                        value -> config.overlayColor = value.getRGB() & 0xFF0000)
                                .controller(option -> ColorControllerBuilder.create(option).allowAlpha(false))
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Component.literal("Transparency"))
                                .binding(50, () -> config.transparency, value -> config.transparency = value)
                                .controller(option -> IntegerSliderControllerBuilder.create(option).range(0, 100).step(1))
                                .build())
                        .build())
                .save(ClientCartsConfig::save)
                .build()
                .generateScreen(parent);
    }

    private ClientCartsConfigScreen() {
    }
}
