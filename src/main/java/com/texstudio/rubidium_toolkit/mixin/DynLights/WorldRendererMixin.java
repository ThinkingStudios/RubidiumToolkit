package com.texstudio.rubidium_toolkit.mixin.DynLights;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.texstudio.rubidium_toolkit.features.DynLights.RubidiumDynLights;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin
{
    @Inject(at = @At("HEAD"), method = "renderLevel")
    public void render(PoseStack outlinebuffersource, float i, long j, boolean k, Camera l, GameRenderer i1, LightTexture lightTexture, Matrix4f multibuffersource, CallbackInfo ci)
    {
        Minecraft.getInstance().getProfiler().popPush("dynamic_lighting");
        RubidiumDynLights.get().updateAll((LevelRenderer) (Object) this);
    }
}