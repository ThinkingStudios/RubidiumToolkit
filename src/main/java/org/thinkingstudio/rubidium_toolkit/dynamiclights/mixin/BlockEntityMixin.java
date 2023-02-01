package org.thinkingstudio.rubidium_toolkit.dynamiclights.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightSource;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightsFeature;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.api.DynamicLightHandlers;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements DynamicLightSource {
    @Shadow
    protected BlockPos worldPosition;

    @Shadow
    @Nullable
    protected Level level;

    @Shadow
    protected boolean remove;
    private int lambdynlights_luminance = 0;
    private int lambdynlights_lastLuminance = 0;
    private long lambdynlights_lastUpdate = 0;
    private LongOpenHashSet trackedLitChunkPos = new LongOpenHashSet();

    @Override
    public double getDynamicLightX() {
        return this.worldPosition.getX() + 0.5;
    }

    @Override
    public double getDynamicLightY() {
        return this.worldPosition.getY() + 0.5;
    }

    @Override
    public double getDynamicLightZ() {
        return this.worldPosition.getZ() + 0.5;
    }

    @Override
    public Level getDynamicLightWorld() {
        return this.level;
    }

    @Inject(method = "setRemoved", at = @At("TAIL"))
    private void onRemoved(CallbackInfo ci) {
        this.setDynamicLightEnabled(false);
    }

    @Override
    public void resetDynamicLight() {
        this.lambdynlights_lastLuminance = 0;
    }

    @Override
    public void dynamicLightTick() {
        // We do not want to update the entity on the server.
        if (this.level == null || !this.level.isClientSide)
            return;
        if (!this.remove) {
            this.lambdynlights_luminance = DynamicLightHandlers.getLuminanceFrom((BlockEntity) (Object) this);
            DynamicLightsFeature.updateTracking(this);

            if (!this.isDynamicLightEnabled()) {
                this.lambdynlights_lastLuminance = 0;
            }
        }
    }

    @Override
    public int getLuminance() {
        return this.lambdynlights_luminance;
    }

    @Override
    public boolean shouldUpdateDynamicLight() {
        return DynamicLightsFeature.ShouldUpdateDynamicLights();
    }

    @Override
    public boolean lambdynlights_updateDynamicLight(@NotNull LevelRenderer renderer) {
        if (!this.shouldUpdateDynamicLight())
            return false;

        int luminance = this.getLuminance();

        if (luminance != this.lambdynlights_lastLuminance) {
            this.lambdynlights_lastLuminance = luminance;

            if (this.trackedLitChunkPos.isEmpty()) {
                BlockPos.MutableBlockPos chunkPos = new BlockPos.MutableBlockPos(Mth.getInt(String.valueOf(this.worldPosition.getX()), 16),
                        Mth.getInt(String.valueOf(this.worldPosition.getY()), 16),
                        Mth.getInt(String.valueOf(this.worldPosition.getZ()), 16));

                DynamicLightsFeature.updateTrackedChunks(chunkPos, null, this.trackedLitChunkPos);

                Direction directionX = (this.worldPosition.getX() & 15) >= 8 ? Direction.EAST : Direction.WEST;
                Direction directionY = (this.worldPosition.getY() & 15) >= 8 ? Direction.UP : Direction.DOWN;
                Direction directionZ = (this.worldPosition.getZ() & 15) >= 8 ? Direction.SOUTH : Direction.NORTH;

                for (int i = 0; i < 7; i++) {
                    if (i % 4 == 0) {
                        chunkPos.move(directionX); // X
                    } else if (i % 4 == 1) {
                        chunkPos.move(directionZ); // XZ
                    } else if (i % 4 == 2) {
                        chunkPos.move(directionX.getOpposite()); // Z
                    } else {
                        chunkPos.move(directionZ.getOpposite()); // origin
                        chunkPos.move(directionY); // Y
                    }
                    DynamicLightsFeature.updateTrackedChunks(chunkPos, null, this.trackedLitChunkPos);
                }
            }

            // Schedules the rebuild of chunks.
            this.lambdynlights_scheduleTrackedChunksRebuild(renderer);
            return true;
        }
        return false;
    }

    @Override
    public void lambdynlights_scheduleTrackedChunksRebuild(@NotNull LevelRenderer renderer) {
        if (this.level == Minecraft.getInstance().level)
            for (long pos : this.trackedLitChunkPos) {
            DynamicLightsFeature.scheduleChunkRebuild(renderer, pos);
        }
    }
}
