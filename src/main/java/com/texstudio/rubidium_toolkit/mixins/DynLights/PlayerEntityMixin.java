package com.texstudio.rubidium_toolkit.mixins.DynLights;

import com.texstudio.rubidium_toolkit.dynamic_lights.DynamicLightSource;
import com.texstudio.rubidium_toolkit.dynamic_lights.RubidiumDynamicLights;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DynamicLightSource
{
    @Shadow
    public abstract boolean isSpectator();

    private int   lambdynlights_luminance;
    private World lambdynlights_lastWorld;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Override
    public void dynamicLightTick()
    {
        if (this.isOnFire() || this.isGlowing())
        {
            this.lambdynlights_luminance = 15;
        }
        else
        {
            int luminance = 0;
            BlockPos eyePos = new BlockPos(this.getX(), this.getEyeY(), this.getZ());
            boolean submergedInFluid = !this.level.getFluidState(eyePos).isEmpty();
            for (ItemStack equipped : this.getAllSlots()) {
                if (!equipped.isEmpty())
                    luminance = Math.max(luminance, RubidiumDynamicLights.getLuminanceFromItemStack(equipped, submergedInFluid));
            }

            this.lambdynlights_luminance = luminance;
        }

        if (this.isSpectator())
            this.lambdynlights_luminance = 0;

        if (this.lambdynlights_lastWorld != this.getCommandSenderWorld()) {
            this.lambdynlights_lastWorld = this.getCommandSenderWorld();
            this.lambdynlights_luminance = 0;
        }
    }

    @Override
    public int getLuminance()
    {
        return this.lambdynlights_luminance;
    }
}
