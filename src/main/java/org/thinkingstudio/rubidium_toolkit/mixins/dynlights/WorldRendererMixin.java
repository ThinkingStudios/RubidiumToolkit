package org.thinkingstudio.rubidium_toolkit.mixins.dynlights;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.thinkingstudio.rubidium_toolkit.features.dynlights.DynamicLightsFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(at = @At("HEAD"), method = "render")
    public void render(MatrixStack l, float outlinelayerbuffer, long i2, boolean j2, Sprite.Info k2, GameRenderer l2, LightmapTextureManager i3, Matrix4f irendertypebuffer, CallbackInfo ci) {
        MinecraftClient.getInstance().getProfiler().visit("dynamic_lighting");
        DynamicLightsFeature.updateAll((WorldRenderer) (Object) this);
    }
}