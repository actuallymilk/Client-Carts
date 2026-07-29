package lgbt.milk.clientcarts.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.item.Items;

public class ClientcartsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCartsConfig.get();

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

        ClientTickEvents.END_CLIENT_TICK.register(GhostMinecartManager::tick);
    }
}
