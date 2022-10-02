package com.texstudio.rubidium_toolkit.mixin.Zoom;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.texstudio.rubidium_toolkit.features.Zoom.utils.SpyglassHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@Mixin(PlayerItemInHandLayer.class)
public abstract class PlayerItemInHandLayerMixin {
    @Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
    private void okzoomer$renderCustomSpyglassesAsSpyglass(LivingEntity entity, ItemStack stack, ItemTransforms.TransformType transformationMode, HumanoidArm arm, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
        if (stack.is(SpyglassHelper.SPYGLASSES) && entity.getUseItem() == stack && entity.swingTime == 0) {
            this.renderArmWithSpyglass(entity, stack, arm, matrices, vertexConsumers, light);
            ci.cancel();
        }
    }

    @Shadow
    protected abstract void renderArmWithSpyglass(LivingEntity livingEntity, ItemStack itemStack, HumanoidArm arm, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i);
}
