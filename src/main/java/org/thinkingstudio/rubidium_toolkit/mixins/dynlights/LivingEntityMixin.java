package org.thinkingstudio.rubidium_toolkit.mixins.dynlights;

import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightSource;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightsFeature;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.api.DynamicLightHandlers;
//import com.texstudio.rubidium_toolkit.features.dynlights.config.DynamicLightsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DynamicLightSource {
    private int lambdynlights_luminance;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void dynamicLightTick() {
        if (this.isOnFire() || this.isGlowing()) {
            this.lambdynlights_luminance = 15;
        } else {
            int luminance = 0;
            BlockPos eyePos = new BlockPos(this.getX(), this.getEyeY(), this.getZ());
            boolean submergedInFluid = !this.world.getFluidState(eyePos).isEmpty();
            for (ItemStack equipped : this.getItemsEquipped()) {
                if (!equipped.isEmpty())
                    luminance = Math.max(luminance, DynamicLightsFeature.getLuminanceFromItemStack(equipped, submergedInFluid));
            }

            this.lambdynlights_luminance = luminance;
        }

        int luminance = DynamicLightHandlers.getLuminanceFrom(this);
        if (luminance > this.lambdynlights_luminance)
            this.lambdynlights_luminance = luminance;

        if (!ToolkitConfig.entityLighting.get() && this.getType() != EntityType.PLAYER)
            this.lambdynlights_luminance = 0;
    }

    @Override
    public int getLuminance() {
        return this.lambdynlights_luminance;
    }
}
