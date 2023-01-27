package org.thinkingstudio.rubidium_toolkit.mixins.dynlights;

import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightSource;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightsFeature;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DynamicLightSource {
    @Shadow
    public abstract boolean isSpectator();

    private int   lambdynlights_luminance;
    private World lambdynlights_lastWorld;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void dynamicLightTick() {
        if (this.isOnFire() || this.isGlowing())
        {
            this.lambdynlights_luminance = 15;
        }
        else
        {
            int luminance = 0;
            BlockPos eyePos = new BlockPos(this.getX(), this.getEyeY(), this.getZ());
            boolean submergedInFluid = !this.world.getFluidState(eyePos).isEmpty();
            for (ItemStack equipped : this.getItemsEquipped()) {
                if (!equipped.isEmpty())
                    luminance = Math.max(luminance, DynamicLightsFeature.getLuminanceFromItemStack(equipped, submergedInFluid));
            }

            this.lambdynlights_luminance = luminance;
        }

        if (this.isSpectator())
            this.lambdynlights_luminance = 0;

        if (this.lambdynlights_lastWorld != this.getEntityWorld()) {
            this.lambdynlights_lastWorld = this.getEntityWorld();
            this.lambdynlights_luminance = 0;
        }
    }

    @Override
    public int getLuminance() {
        return this.lambdynlights_luminance;
    }
}
