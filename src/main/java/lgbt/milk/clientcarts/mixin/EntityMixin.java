package lgbt.milk.clientcarts.mixin;

import lgbt.milk.clientcarts.client.GhostMinecartManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "isPickable", at = @At("HEAD"), cancellable = true)
    private void clientcarts$ignoreGhostMinecart(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof MinecartTNT minecart && GhostMinecartManager.isGhost(minecart)) {
            cir.setReturnValue(false);
        }
    }
}
