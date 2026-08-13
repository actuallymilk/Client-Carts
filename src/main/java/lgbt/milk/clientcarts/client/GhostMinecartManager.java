package lgbt.milk.clientcarts.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
/*? if >=26.2 {*/
/*import net.minecraft.world.entity.EntityTypes;*/
/*?} else {*/
import net.minecraft.world.entity.EntityType;
/*?}*/
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public final class GhostMinecartManager {
    private static final int TIMEOUT_TICKS = 20;
    private static final List<Ghost> GHOSTS = new ArrayList<>();
    private static final Set<MinecartTNT> GHOST_ENTITIES =
            Collections.newSetFromMap(new IdentityHashMap<>());
    private static final Set<MinecartRenderState> GHOST_STATES =
            Collections.newSetFromMap(new WeakHashMap<>());

    public static void place(ClientLevel world, BlockHitResult hit) {
        if (!ClientCartsConfig.get().modEnabled) {
            return;
        }

        BlockPos pos = hit.getBlockPos();
        BlockState state = world.getBlockState(pos);
        if (!state.is(BlockTags.RAILS)
                || GHOSTS.stream().anyMatch(ghost -> ghost.pos.distanceToSqr(Vec3.atCenterOf(pos)) < 1.0)) {
            return;
        }

        RailShape shape = state.getBlock() instanceof BaseRailBlock rail
                ? state.getValue(rail.getShapeProperty())
                : RailShape.NORTH_SOUTH;
        Vec3 placement = new Vec3(
                pos.getX() + 0.5,
                pos.getY() + 0.0625 + (shape.isSlope() ? 0.5 : 0.0),
                pos.getZ() + 0.5
        );
        /*? if >=26.2 {*/
        /*MinecartTNT ghost = new MinecartTNT(EntityTypes.TNT_MINECART, world);*/
        /*?} else {*/
        MinecartTNT ghost = new MinecartTNT(EntityType.TNT_MINECART, world);
        /*?}*/
        ghost.setUUID(java.util.UUID.randomUUID());
        ghost.setPos(placement);
        ghost.noPhysics = true;
        ghost.blocksBuilding = false;

        GHOST_ENTITIES.add(ghost);
        GHOSTS.add(new Ghost(ghost, placement, TIMEOUT_TICKS));
        world.addEntity(ghost);
    }

    public static void onMinecartLoaded(MinecartTNT minecart) {
        if (GHOST_ENTITIES.contains(minecart)) {
            return;
        }

        GHOSTS.stream()
                .filter(ghost -> ghost.pos.distanceToSqr(minecart.position()) < 1.0)
                .findFirst()
                .ifPresent(GhostMinecartManager::remove);
    }

    public static void tick(Minecraft minecraft) {
        if (!ClientCartsConfig.get().modEnabled) {
            while (!GHOSTS.isEmpty()) {
                remove(GHOSTS.getLast());
            }
            return;
        }

        ClientLevel world = minecraft.level;
        for (int i = GHOSTS.size() - 1; i >= 0; i--) {
            Ghost ghost = GHOSTS.get(i);
            if (world == null || ghost.entity.level() != world
                    || !world.getBlockState(BlockPos.containing(ghost.pos)).is(BlockTags.RAILS)
                    || ghost.entity.isRemoved() || --ghost.ticksLeft <= 0) {
                remove(ghost);
                continue;
            }
            ghost.entity.setDeltaMovement(Vec3.ZERO);
            ghost.entity.setPos(ghost.pos);
        }
    }

    public static boolean isGhost(MinecartTNT minecart) {
        return GHOST_ENTITIES.contains(minecart);
    }

    public static void markRenderState(MinecartRenderState state, boolean ghost) {
        if (ghost) {
            GHOST_STATES.add(state);
        } else {
            GHOST_STATES.remove(state);
        }
    }

    public static boolean isGhost(MinecartRenderState state) {
        return GHOST_STATES.contains(state);
    }

    private static void remove(Ghost ghost) {
        GHOSTS.remove(ghost);
        GHOST_ENTITIES.remove(ghost.entity);
        if (!ghost.entity.isRemoved()) {
            ghost.entity.discard();
        }
    }

    private static final class Ghost {
        private final MinecartTNT entity;
        private final Vec3 pos;
        private int ticksLeft;

        private Ghost(MinecartTNT entity, Vec3 pos, int ticksLeft) {
            this.entity = entity;
            this.pos = pos;
            this.ticksLeft = ticksLeft;
        }
    }

    private GhostMinecartManager() {
    }
}
