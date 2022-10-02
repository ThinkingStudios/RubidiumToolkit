package com.texstudio.rubidium_toolkit.mixins.FrameCounter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.texstudio.rubidium_toolkit.features.FrameCounter.MinFrameProvider;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.texstudio.rubidium_toolkit.config.RubidiumToolkitConfig;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

@Mixin(ForgeIngameGui.class)
public class FrameCounterMixin
{

    private int lastMeasuredFPS;
    private String runningAverageFPS;
    private final Queue<Integer> fpsRunningAverageQueue = new LinkedList<Integer>();

    @Inject(at = @At("TAIL"), method = "render")
    public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo info)
    {
        if (Objects.equals(RubidiumToolkitConfig.fpsCounterMode.get(), "OFF"))
            return;

        Minecraft client = Minecraft.getInstance();

        // return if F3 menu open and graph not displayed
        if (client.options.renderDebug && !client.options.renderFpsChart)
            return;

        String displayString = null;
        int fps = FpsAccessorMixin.getFPS();

        if (Objects.equals(RubidiumToolkitConfig.fpsCounterMode.get(), "ADVANCED"))
            displayString = GetAdvancedFPSString(fps);
        else
            displayString = String.valueOf(fps);

        boolean textAlignRight = RubidiumToolkitConfig.fpsCounterAlignRight.get();

        float textPos = (int)RubidiumToolkitConfig.fpsCounterPosition.get();

        int textAlpha = 200;
        int textColor = 0xFFFFFF;
        float fontScale = 0.75F;

        double guiScale = client.getWindow().getGuiScale();
        if (guiScale > 0) {
            textPos /= guiScale;
        }

        // Prevent FPS-Display to render outside screenspace
        float maxTextPosX = client.getWindow().getGuiScaledWidth() - client.font.width(displayString);
        float maxTextPosY = client.getWindow().getGuiScaledHeight() - client.font.lineHeight;
        float textPosX, textPosY;
        if (textAlignRight)
            textPosX = client.getWindow().getGuiScaledWidth() - client.font.width(displayString) - textPos;
        else
            textPosX = Math.min(textPos, maxTextPosX);
        textPosX = Math.min(Math.max(textPosX, 0), maxTextPosX);
        textPosY = Math.min(textPos, maxTextPosY);

        int drawColor = ((textAlpha & 0xFF) << 24) | textColor;

        if (client.getWindow().getGuiScale() > 3)
        {
            GL11.glPushMatrix();
            GL11.glScalef(fontScale, fontScale, fontScale);
            client.font.drawShadow(matrixStack, displayString, textPosX, textPosY, drawColor);
            GL11.glPopMatrix();
        }
        else
        {
            client.font.drawShadow(matrixStack, displayString, textPosX, textPosY, drawColor);
        }
    }


    private String GetAdvancedFPSString(int fps)
    {
        MinFrameProvider.recalculate();

        if (lastMeasuredFPS != fps)
        {
            lastMeasuredFPS = fps;

            if (fpsRunningAverageQueue.size() > 14)
                fpsRunningAverageQueue.poll();

            fpsRunningAverageQueue.offer(fps);

            int totalFps = 0;
            int frameCount = 0;
            for (val frameTime : fpsRunningAverageQueue)
            {
                totalFps += frameTime;
                frameCount++;
            }

            int average = (int) (totalFps / frameCount);
            runningAverageFPS = String.valueOf(average);
        }

        return fps + " | MIN " + MinFrameProvider.getLastMinFrame() + " | AVG " + runningAverageFPS;
    }
}