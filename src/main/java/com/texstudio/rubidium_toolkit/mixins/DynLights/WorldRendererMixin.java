package com.texstudio.rubidium_toolkit.mixins.DynLights;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.texstudio.rubidium_toolkit.dynamic_lights.RubidiumDynamicLights;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.vector.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin
{
    @Inject(at = @At("HEAD"), method = "renderLevel")
    public void render(MatrixStack l, float outlinelayerbuffer, long i2, boolean j2, ActiveRenderInfo k2, GameRenderer l2, LightTexture i3, Matrix4f irendertypebuffer, CallbackInfo ci)
    {
        Minecraft.getInstance().getProfiler().popPush("dynamic_lighting");
        RubidiumDynamicLights.updateAll((WorldRenderer) (Object) this);
    }
}