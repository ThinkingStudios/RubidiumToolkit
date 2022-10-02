package com.texstudio.rubidium_toolkit.features.FPSCounter;

import com.texstudio.rubidium_toolkit.RubidiumToolkit;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RubidiumToolkit.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DebugOverlayImprovements
{
    @SubscribeEvent
    public static void onRenderDebugText(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.DEBUG)
            return;

        // cancel rendering text if chart is displaying
        val minecraft = Minecraft.getInstance();
        if (minecraft.options.renderFpsChart)
            event.setCanceled(true);

    }
}