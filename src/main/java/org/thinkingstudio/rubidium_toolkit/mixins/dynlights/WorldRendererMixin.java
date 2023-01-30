package org.thinkingstudio.rubidium_toolkit.mixins.dynlights;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightsFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin {
    @Inject(at = @At("HEAD"), method = "renderLevel")
    public void render(PoseStack l, float outlinelayerbuffer, long i2, boolean j2, Camera k2, GameRenderer l2, LightTexture i3, Matrix4f irendertypebuffer, CallbackInfo ci) {
        Minecraft.getInstance().getProfiler().incrementCounter("dynamic_lighting");
        DynamicLightsFeature.updateAll((LevelRenderer) (Object) this);
    }
}