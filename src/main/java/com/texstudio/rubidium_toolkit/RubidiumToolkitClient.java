package com.texstudio.rubidium_toolkit;

import com.texstudio.rubidium_toolkit.features.Zoom.APIImpl;
import com.texstudio.rubidium_toolkit.features.Zoom.ZoomKeyBinds;
import com.texstudio.rubidium_toolkit.features.Zoom.api.OkZoomerAPI;
import com.texstudio.rubidium_toolkit.features.Zoom.api.ZoomInstance;
import com.texstudio.rubidium_toolkit.features.Zoom.api.ZoomOverlay;
import com.texstudio.rubidium_toolkit.config.ClientConfig;
import com.texstudio.rubidium_toolkit.features.Zoom.events.ManageKeyBindsEvent;
import com.texstudio.rubidium_toolkit.features.Zoom.events.ManageZoomEvent;
//import com.texstudio.rubidium_toolkit.config.ConfigEnums;
import com.texstudio.rubidium_toolkit.features.Zoom.network.RubidiumToolkitNetwork;
import com.texstudio.rubidium_toolkit.features.Zoom.utils.SpyglassHelper;
import com.texstudio.rubidium_toolkit.features.Zoom.utils.ZoomUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

import static com.texstudio.rubidium_toolkit.config.ConfigEnums.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = OkZoomerAPI.MOD_ID, value = Dist.CLIENT)
public class RubidiumToolkitClient {

    private static boolean shouldCancelOverlay;

    @SubscribeEvent
    static void clientSetup(final FMLClientSetupEvent event) {
        OverlayRegistry.registerOverlayTop("LibZoomer", (gui, poseStack, partialTick, width, height) -> {
            shouldCancelOverlay = false;
            for (ZoomInstance instance : APIImpl.getZoomInstances()) {
                ZoomOverlay overlay = instance.getZoomOverlay();
                if (overlay != null) {
                    overlay.tickBeforeRender();
                    if (overlay.getActive()) {
                        shouldCancelOverlay = overlay.cancelOverlayRendering() || true;
                        overlay.renderOverlay();
                    }
                }
            }
        });
        MinecraftForge.EVENT_BUS.addListener(RubidiumToolkitClient::clientTick);
        MinecraftForge.EVENT_BUS.addListener(RubidiumToolkitClient::onMouseInput);

        ItemProperties.registerGeneric(new ResourceLocation(OkZoomerAPI.MOD_ID, "scoping"),
                (ClampedItemPropertyFunction) (stack, clientWorld, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack && entity.getUseItem().is(SpyglassHelper.SPYGLASSES) ? 1.0F : 0.0F);

        ClientRegistry.registerKeyBinding(ZoomKeyBinds.ZOOM_KEY);
        if (ZoomKeyBinds.areExtraKeyBindsEnabled()) {
            ClientRegistry.registerKeyBinding(ZoomKeyBinds.DECREASE_ZOOM_KEY);
            ClientRegistry.registerKeyBinding(ZoomKeyBinds.INCREASE_ZOOM_KEY);
            ClientRegistry.registerKeyBinding(ZoomKeyBinds.RESET_ZOOM_KEY);
        }

        RubidiumToolkitNetwork.configureZoomInstance();
    }

    static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.ClientTickEvent.Phase.END && Minecraft.getInstance().level == null)
            return;
        OverlayRegistry.enableOverlay(ForgeIngameGui.SPYGLASS_ELEMENT, !shouldCancelOverlay);

        if (switch (RubidiumToolkitNetwork.getSpyglassDependency()) {
            case REPLACE_ZOOM, BOTH -> true;
            default -> false;
        }) {
            OverlayRegistry.enableOverlay(ForgeIngameGui.SPYGLASS_ELEMENT, false);
        }

        ManageKeyBindsEvent.onTickEnd();
        ManageZoomEvent.endTick(Minecraft.getInstance());
    }

    static void onMouseInput(final InputEvent.MouseInputEvent event) {
        if (ClientConfig.ALLOW_SCROLLING.get() && !RubidiumToolkitNetwork.getDisableZoomScrolling()) {
            if (ClientConfig.ZOOM_MODE.get().equals(ZoomModes.PERSISTENT) && !ZoomKeyBinds.ZOOM_KEY.isDown()) {
                return;
            }

            if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_MIDDLE && ZoomKeyBinds.ZOOM_KEY.isDown() && ClientConfig.RESET_ZOOM_WITH_MOUSE.get()) {
                ZoomUtils.resetZoomDivisor(true);
            }
        }
    }

    private static final TranslatableComponent TOAST_TITLE = new TranslatableComponent("toast.rubidium_toolkit.title");

    public static void sendToast(Component description) {
        if (ClientConfig.SHOW_RESTRICTION_TOASTS.get()) {
            Minecraft.getInstance().getToasts().addToast(SystemToast.multiline(Minecraft.getInstance(), SystemToast.SystemToastIds.TUTORIAL_HINT, TOAST_TITLE, description));
        }
    }
}
