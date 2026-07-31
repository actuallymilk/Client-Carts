package lgbt.milk.clientcarts.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import lgbt.milk.clientcarts.client.ClientCartsConfig;
import lgbt.milk.clientcarts.client.GhostMinecartManager;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
/*? if >=26.1 {*/
/*import net.minecraft.client.renderer.state.level.CameraRenderState;*/
/*?} else {*/
import net.minecraft.client.renderer.state.CameraRenderState;
/*?}*/
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartRenderer.class)
public abstract class AbstractMinecartRendererMixin {
    @Unique private static final Identifier CLIENTCARTS_MINECART =
            Identifier.withDefaultNamespace("textures/entity/minecart.png");
    @Unique private boolean clientcarts$ghost;

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;F)V", at = @At("TAIL"))
    private void clientcarts$markState(AbstractMinecart entity, MinecartRenderState state, float tickDelta, CallbackInfo ci) {
        GhostMinecartManager.markRenderState(state, entity instanceof MinecartTNT tnt && GhostMinecartManager.isGhost(tnt));
    }

    /*? if >=26.1 {*/
    /*@Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("HEAD"))*/
    /*?} else {*/
    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("HEAD"))
    /*?}*/
    private void clientcarts$beginGhostRender(MinecartRenderState state, PoseStack matrices, SubmitNodeCollector queue,
                                              CameraRenderState camera, CallbackInfo ci) {
        clientcarts$ghost = GhostMinecartManager.isGhost(state);
    }

    /*? if >=26.1 {*/
    /*@Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("RETURN"))*/
    /*?} else {*/
    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("RETURN"))
    /*?}*/
    private void clientcarts$endGhostRender(MinecartRenderState state, PoseStack matrices, SubmitNodeCollector queue,
                                            CameraRenderState camera, CallbackInfo ci) {
        clientcarts$ghost = false;
    }

    /*? if >=26.1 {*/
    /*@Redirect(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/Identifier;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V")
    )
    private <S> void clientcarts$renderCart(SubmitNodeCollector queue, Model<? super S> model, S state,
                                            PoseStack matrices, Identifier texture, int light, int overlay,
                                            int outline, CrumblingOverlay crumbling) {
        if (clientcarts$ghost) {
            queue.submitModel(model, state, matrices, clientcarts$translucent(texture), light, overlay,
                    ClientCartsConfig.renderColor(), null, outline, crumbling);
        } else {
            queue.submitModel(model, state, matrices, texture, light, overlay, outline, crumbling);
        }
    }*/
    /*?} else {*/
    @Redirect(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V")
    )
    private <S> void clientcarts$renderCart(SubmitNodeCollector queue, Model<? super S> model, S state,
                                            PoseStack matrices, RenderType renderType, int light, int overlay,
                                            int outline, CrumblingOverlay crumbling) {
        queue.submitModel(model, state, matrices, clientcarts$ghost ? clientcarts$translucent() : renderType,
                light, overlay, clientcarts$ghost ? ClientCartsConfig.renderColor() : -1, null, outline, crumbling);
    }
    /*?}*/

    /*? if >=26.1 {*/
    /*@Unique
    private static RenderType clientcarts$translucent(Identifier texture) {
        return RenderTypes.entityTranslucent(texture);
    }*/
    /*?} else {*/
    @Unique
    private static RenderType clientcarts$translucent() {
        return RenderTypes.entityTranslucent(CLIENTCARTS_MINECART);
    }
    /*?}*/
}