package org.thinkingstudio.rubidium_toolkit.features.zoom.zoom;

import org.thinkingstudio.rubidium_toolkit.features.zoom.api.OkZoomerAPI;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.ZoomOverlay;
import org.thinkingstudio.rubidium_toolkit.config.ClientConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.thinkingstudio.rubidium_toolkit.config.ConfigEnums;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

// Implements the zoom overlay
public class ZoomerZoomOverlay implements ZoomOverlay {
    private static final ResourceLocation OVERLAY_ID = new ResourceLocation(OkZoomerAPI.MOD_ID, "zoom_overlay");
    private ResourceLocation textureId;
    private boolean active;
    private boolean zoomActive;
    private double divisor;
    private final Minecraft client;

    public float zoomOverlayAlpha = 0.0F;
    public float lastZoomOverlayAlpha = 0.0F;

    public ZoomerZoomOverlay(ResourceLocation textureId) {
        this.textureId = textureId;
        this.active = false;
        this.client = Minecraft.getInstance();
    }

    @Override
    public ResourceLocation getId() {
        return OVERLAY_ID;
    }

    @Override
    public boolean getActive() {
        if (client.options.hideGui && ClientConfig.DISABLE_OVERLAY_NO_HUD.get()) {
            return false;
        }
        return this.active;
    }

    @Override
    public void renderOverlay() {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.zoomOverlayAlpha);
        RenderSystem.setShaderTexture(0, this.textureId);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(0.0D, (double) this.client.getWindow().getGuiScaledHeight(), -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferBuilder.vertex((double) this.client.getWindow().getGuiScaledWidth(), (double) this.client.getWindow().getGuiScaledHeight(), -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferBuilder.vertex((double) this.client.getWindow().getGuiScaledWidth(), 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void tick(boolean active, double divisor, double transitionMultiplier) {
        this.divisor = divisor;
        this.zoomActive = active;
        if ((!active && zoomOverlayAlpha == 0.0f) || active) {
            this.active = active;
        }

        float zoomMultiplier = this.zoomActive ? 1.0F : 0.0F;

        lastZoomOverlayAlpha = zoomOverlayAlpha;

        if (ClientConfig.ZOOM_TRANSITION.get().equals(ConfigEnums.ZoomTransitionOptions.SMOOTH)) {
            zoomOverlayAlpha += (zoomMultiplier - zoomOverlayAlpha) * ClientConfig.SMOOTH_MULTIPLIER.get();
        } else if (ClientConfig.ZOOM_TRANSITION.get().equals(ConfigEnums.ZoomTransitionOptions.LINEAR)) {
            double linearStep = Mth.clamp(1.0F / this.divisor, ClientConfig.MINIMUM_LINEAR_STEP.get(), ClientConfig.MAXIMUM_LINEAR_STEP.get());

            zoomOverlayAlpha = Mth.approach(zoomOverlayAlpha, zoomMultiplier, (float) linearStep);
        }
    }
}
