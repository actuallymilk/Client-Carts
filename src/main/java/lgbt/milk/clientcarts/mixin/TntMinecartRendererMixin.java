package lgbt.milk.clientcarts.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lgbt.milk.clientcarts.client.ClientCartsConfig;
import lgbt.milk.clientcarts.client.GhostMinecartManager;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.state.MinecartTntRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
/*? if >=26.1 {*/
/*import net.minecraft.client.renderer.block.BlockModelRenderState;*/
/*?} else {*/
import net.minecraft.world.level.block.state.BlockState;
/*?}*/
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntMinecartRenderer.class)
public abstract class TntMinecartRendererMixin {
    @Unique private static final Identifier CLIENTCARTS_TNT_SIDE =
            Identifier.withDefaultNamespace("textures/block/tnt_side.png");
    @Unique private static final Identifier CLIENTCARTS_TNT_TOP =
            Identifier.withDefaultNamespace("textures/block/tnt_top.png");
    @Unique private static final Identifier CLIENTCARTS_TNT_BOTTOM =
            Identifier.withDefaultNamespace("textures/block/tnt_bottom.png");
    @Unique private boolean clientcarts$ghost;

    /*? if >=26.1 {*/
    /*@Inject(method = "submitMinecartContents(Lnet/minecraft/client/renderer/entity/state/MinecartTntRenderState;Lnet/minecraft/client/renderer/block/BlockModelRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", at = @At("HEAD"))
    private void clientcarts$beginTntRender(MinecartTntRenderState state, BlockModelRenderState block, PoseStack matrices,
                                            SubmitNodeCollector queue, int light, CallbackInfo ci) {
        clientcarts$ghost = GhostMinecartManager.isGhost(state);
    }

    @Inject(method = "submitMinecartContents(Lnet/minecraft/client/renderer/entity/state/MinecartTntRenderState;Lnet/minecraft/client/renderer/block/BlockModelRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", at = @At("RETURN"))
    private void clientcarts$endTntRender(MinecartTntRenderState state, BlockModelRenderState block, PoseStack matrices,
                                          SubmitNodeCollector queue, int light, CallbackInfo ci) {
        clientcarts$ghost = false;
    }

    @Redirect(
            method = "submitMinecartContents(Lnet/minecraft/client/renderer/entity/state/MinecartTntRenderState;Lnet/minecraft/client/renderer/block/BlockModelRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/TntMinecartRenderer;submitWhiteSolidBlock(Lnet/minecraft/client/renderer/block/BlockModelRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IZI)V")
    )
    private void clientcarts$renderTnt(BlockModelRenderState state, PoseStack matrices, SubmitNodeCollector queue,
                                       int light, boolean white, int outline) {
        if (!clientcarts$ghost) {
            TntMinecartRenderer.submitWhiteSolidBlock(state, matrices, queue, light, white, outline);
            return;
        }

        queue.submitCustomGeometry(matrices, clientcarts$translucent(CLIENTCARTS_TNT_SIDE),
                (pose, vertices) -> clientcarts$sides(pose, vertices, light));
        queue.submitCustomGeometry(matrices, clientcarts$translucent(CLIENTCARTS_TNT_TOP),
                (pose, vertices) -> clientcarts$top(pose, vertices, light));
        queue.submitCustomGeometry(matrices, clientcarts$translucent(CLIENTCARTS_TNT_BOTTOM),
                (pose, vertices) -> clientcarts$bottom(pose, vertices, light));
    }*/
    /*?} else {*/
    @Inject(method = "submitMinecartContents(Lnet/minecraft/client/renderer/entity/state/MinecartTntRenderState;Lnet/minecraft/world/level/block/state/BlockState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", at = @At("HEAD"))
    private void clientcarts$beginTntRender(MinecartTntRenderState state, BlockState block, PoseStack matrices,
                                            SubmitNodeCollector queue, int light, CallbackInfo ci) {
        clientcarts$ghost = GhostMinecartManager.isGhost(state);
    }

    @Inject(method = "submitMinecartContents(Lnet/minecraft/client/renderer/entity/state/MinecartTntRenderState;Lnet/minecraft/world/level/block/state/BlockState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", at = @At("RETURN"))
    private void clientcarts$endTntRender(MinecartTntRenderState state, BlockState block, PoseStack matrices,
                                          SubmitNodeCollector queue, int light, CallbackInfo ci) {
        clientcarts$ghost = false;
    }

    @Redirect(
            method = "submitMinecartContents(Lnet/minecraft/client/renderer/entity/state/MinecartTntRenderState;Lnet/minecraft/world/level/block/state/BlockState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/TntMinecartRenderer;submitWhiteSolidBlock(Lnet/minecraft/world/level/block/state/BlockState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IZI)V")
    )
    private void clientcarts$renderTnt(BlockState state, PoseStack matrices, SubmitNodeCollector queue,
                                       int light, boolean white, int outline) {
        if (!clientcarts$ghost) {
            TntMinecartRenderer.submitWhiteSolidBlock(state, matrices, queue, light, white, outline);
            return;
        }

        queue.submitCustomGeometry(matrices, clientcarts$translucent(CLIENTCARTS_TNT_SIDE),
                (pose, vertices) -> clientcarts$sides(pose, vertices, light));
        queue.submitCustomGeometry(matrices, clientcarts$translucent(CLIENTCARTS_TNT_TOP),
                (pose, vertices) -> clientcarts$top(pose, vertices, light));
        queue.submitCustomGeometry(matrices, clientcarts$translucent(CLIENTCARTS_TNT_BOTTOM),
                (pose, vertices) -> clientcarts$bottom(pose, vertices, light));
    }
    /*?}*/

    @Unique
    private static RenderType clientcarts$translucent(Identifier texture) {
        /*? if >=26.1 {*/
        /*return RenderTypes.itemTranslucent(texture);*/
        /*?} else {*/
        return RenderTypes.itemEntityTranslucentCull(texture);
        /*?}*/
    }

    @Unique
    private static void clientcarts$sides(PoseStack.Pose pose, VertexConsumer vertices, int light) {
        clientcarts$quad(pose, vertices, light, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, -1);
        clientcarts$quad(pose, vertices, light, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1);
        clientcarts$quad(pose, vertices, light, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, -1, 0, 0);
        clientcarts$quad(pose, vertices, light, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0);
    }

    @Unique
    private static void clientcarts$top(PoseStack.Pose pose, VertexConsumer vertices, int light) {
        clientcarts$quad(pose, vertices, light, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0);
    }

    @Unique
    private static void clientcarts$bottom(PoseStack.Pose pose, VertexConsumer vertices, int light) {
        clientcarts$quad(pose, vertices, light, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, -1, 0);
    }

    @Unique
    private static void clientcarts$quad(PoseStack.Pose pose, VertexConsumer vertices, int light,
                                         float x1, float y1, float z1, float x2, float y2, float z2,
                                         float x3, float y3, float z3, float x4, float y4, float z4,
                                         float nx, float ny, float nz) {
        clientcarts$vertex(pose, vertices, light, x1, y1, z1, 0, 1, nx, ny, nz);
        clientcarts$vertex(pose, vertices, light, x2, y2, z2, 0, 0, nx, ny, nz);
        clientcarts$vertex(pose, vertices, light, x3, y3, z3, 1, 0, nx, ny, nz);
        clientcarts$vertex(pose, vertices, light, x4, y4, z4, 1, 1, nx, ny, nz);
    }

    @Unique
    private static void clientcarts$vertex(PoseStack.Pose pose, VertexConsumer vertices, int light,
                                           float x, float y, float z, float u, float v,
                                           float nx, float ny, float nz) {
        int color = ClientCartsConfig.renderColor();
        vertices.addVertex(pose, x, y, z)
                .setColor((color >> 16) & 255, (color >> 8) & 255, color & 255, color >>> 24)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, nx, ny, nz);
    }
}