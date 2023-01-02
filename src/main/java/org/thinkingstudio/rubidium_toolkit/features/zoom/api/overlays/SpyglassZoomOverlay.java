package org.thinkingstudio.rubidium_toolkit.features.zoom.api.overlays;

import org.thinkingstudio.rubidium_toolkit.features.zoom.api.OkZoomerAPI;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.ZoomOverlay;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * An implementation of the spyglass overlay as a zoom overlay
 */
public class SpyglassZoomOverlay implements ZoomOverlay {
    private static final ResourceLocation OVERLAY_ID = new ResourceLocation(OkZoomerAPI.MOD_ID, "spyglass_zoom");
    private final ResourceLocation textureId;
    private final Minecraft mc;
    private float scale;
    private boolean active;

    /**
     * Initializes an instance of the spyglass mouse modifier with the specified texture identifier
     * @param textureId The texture identifier for the spyglass overlay
    */
    public SpyglassZoomOverlay(ResourceLocation textureId) {
        this.textureId = textureId;
        this.mc = Minecraft.getInstance();
        this.scale = 0.5F;
        this.active = false;
    }

    @Override
    public ResourceLocation getId() {
        return OVERLAY_ID;
    }

    @Override
    public boolean getActive() {
        return this.active;
    }

    @Override
    public boolean cancelOverlayRendering() {
        return true;
    }

    @Override
    public void renderOverlay() {
        int scaledWidth = this.mc.getWindow().getGuiScaledWidth();
        int scaledHeight = this.mc.getWindow().getGuiScaledHeight();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.textureId);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        float f = Math.min(scaledWidth, scaledHeight);
        float h = Math.min(scaledWidth / f, scaledHeight / f) * this.scale;
        float i = f * h;
        float j = (scaledWidth - i) / 2.0F;
        float k = (scaledHeight - i) / 2.0F;
        float l = j + i;
        float m = k + i;
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(j, m, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(l, m, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferBuilder.vertex(l, k, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(j, k, -90.0D).uv(0.0F, 0.0F).endVertex();
        tesselator.end();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableTexture();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.vertex(0.0D, scaledHeight, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(scaledWidth, m, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(0.0D, m, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(0.0D, k, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(scaledWidth, k, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(scaledWidth, 0.0D, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(0.0D, 0.0D, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(0.0D, m, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(j, m, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(j, k, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(0.0D, k, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(l, m, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(scaledWidth, m, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(scaledWidth, k, -90.0D).color(0, 0, 0, 255).endVertex();
        bufferBuilder.vertex(l, k, -90.0D).color(0, 0, 0, 255).endVertex();
        tesselator.end();
        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void tick(boolean active, double divisor, double transitionMultiplier) {
        this.active = active;
    }

    @Override
    public void tickBeforeRender() {
        if (this.mc.options.getCameraType().isFirstPerson()) {
            if (!this.active) {
                this.scale = 0.5F;
            } else {
                float lastFrameDuration = this.mc.getDeltaFrameTime();
                this.scale = Mth.lerp(0.5F * lastFrameDuration, this.scale, 1.125F);
            }
        }
    }
}
