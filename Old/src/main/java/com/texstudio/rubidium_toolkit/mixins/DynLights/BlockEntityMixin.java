package com.texstudio.rubidium_toolkit.mixins.DynLights;

import com.texstudio.rubidium_toolkit.dynamic_lights.DynamicLightSource;
import com.texstudio.rubidium_toolkit.dynamic_lights.RubidiumDynamicLights;
import com.texstudio.rubidium_toolkit.dynamic_lights.api.DynamicLightHandlers;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(TileEntity.class)
public abstract class BlockEntityMixin implements DynamicLightSource
{
    @Shadow
    protected BlockPos worldPosition;

    @Shadow
    @Nullable
    protected World level;

    @Shadow
    protected boolean remove;
    private int lambdynlights_luminance = 0;
    private int lambdynlights_lastLuminance = 0;
    private long lambdynlights_lastUpdate = 0;
    private LongOpenHashSet trackedLitChunkPos = new LongOpenHashSet();

    @Override
    public double getDynamicLightX()
    {
        return this.worldPosition.getX() + 0.5;
    }

    @Override
    public double getDynamicLightY()
    {
        return this.worldPosition.getY() + 0.5;
    }

    @Override
    public double getDynamicLightZ()
    {
        return this.worldPosition.getZ() + 0.5;
    }

    @Override
    public World getDynamicLightWorld()
    {
        return this.level;
    }

    @Inject(method = "setRemoved", at = @At("TAIL"))
    private void onRemoved(CallbackInfo ci)
    {
        this.setDynamicLightEnabled(false);
    }

    @Override
    public void resetDynamicLight()
    {
        this.lambdynlights_lastLuminance = 0;
    }

    @Override
    public void dynamicLightTick()
    {
        // We do not want to update the entity on the server.
        if (this.level == null || !this.level.isClientSide())
            return;
        if (!this.remove) {
            this.lambdynlights_luminance = DynamicLightHandlers.getLuminanceFrom((TileEntity) (Object) this);
            RubidiumDynamicLights.updateTracking(this);

            if (!this.isDynamicLightEnabled()) {
                this.lambdynlights_lastLuminance = 0;
            }
        }
    }

    @Override
    public int getLuminance()
    {
        return this.lambdynlights_luminance;
    }

    @Override
    public boolean shouldUpdateDynamicLight()
    {
        return RubidiumDynamicLights.ShouldUpdateDynamicLights();
    }

    @Override
    public boolean lambdynlights_updateDynamicLight(@NotNull WorldRenderer renderer)
    {
        if (!this.shouldUpdateDynamicLight())
            return false;

        int luminance = this.getLuminance();

        if (luminance != this.lambdynlights_lastLuminance) {
            this.lambdynlights_lastLuminance = luminance;

            if (this.trackedLitChunkPos.isEmpty()) {
                BlockPos.Mutable chunkPos = new BlockPos.Mutable(MathHelper.intFloorDiv(this.worldPosition.getX(), 16),
                        MathHelper.intFloorDiv(this.worldPosition.getY(), 16),
                        MathHelper.intFloorDiv(this.worldPosition.getZ(), 16));

                RubidiumDynamicLights.updateTrackedChunks(chunkPos, null, this.trackedLitChunkPos);

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
                    RubidiumDynamicLights.updateTrackedChunks(chunkPos, null, this.trackedLitChunkPos);
                }
            }

            // Schedules the rebuild of chunks.
            this.lambdynlights_scheduleTrackedChunksRebuild(renderer);
            return true;
        }
        return false;
    }

    @Override
    public void lambdynlights_scheduleTrackedChunksRebuild(@NotNull WorldRenderer renderer)
    {
        if (this.level == Minecraft.getInstance().level)
        for (long pos : this.trackedLitChunkPos) {
            RubidiumDynamicLights.scheduleChunkRebuild(renderer, pos);
        }
    }
}
