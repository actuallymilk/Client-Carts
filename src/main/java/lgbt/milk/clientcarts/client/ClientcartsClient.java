package lgbt.milk.clientcarts.client;

import net.fabricmc.api.ClientModInitializer;
/*? if >=26.1 {*/
/*import net.fabricmc.fabric.api.client.command.v2.ClientCommands;*/
/*?} else {*/
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
/*?}*/
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.item.Items;

public class ClientcartsClient implements ClientModInitializer {
    private static boolean openConfig;

    @Override
    public void onInitializeClient() {
        ClientCartsConfig.get();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                /*? if >=26.1 {*/
                /*ClientCommands.literal("clientcart")*/
                /*?} else {*/
                ClientCommandManager.literal("clientcart")
                /*?}*/
                        .executes(context -> {
                            openConfig = true;
                            return 1;
                        })
        ));

        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            if (world instanceof ClientLevel clientWorld && player.getItemInHand(hand).is(Items.TNT_MINECART)) {
                GhostMinecartManager.place(clientWorld, hit);
            }
            return InteractionResult.PASS;
        });

        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof MinecartTNT minecart) {
                GhostMinecartManager.onMinecartLoaded(minecart);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            GhostMinecartManager.tick(minecraft);
            if (openConfig) {
                openConfig = false;
                /*? if >=26.2 {*/
                /*minecraft.gui.setScreen(ClientCartsConfigScreen.create(minecraft.gui.screen()));*/
                /*?} else {*/
                minecraft.setScreen(ClientCartsConfigScreen.create(minecraft.screen));
                /*?}*/
            }
        });
    }
}
