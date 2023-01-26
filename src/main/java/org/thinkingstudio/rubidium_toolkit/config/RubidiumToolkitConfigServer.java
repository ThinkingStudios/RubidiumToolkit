package org.thinkingstudio.rubidium_toolkit.config;

import org.thinkingstudio.rubidium_toolkit.features.zoom.network.RubidiumToolkitNetwork;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.AcknowledgeModPacket;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.DisableZoomPacket;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.DisableZoomScrollingPacket;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.ForceClassicModePacket;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.ForceOverlayPacket;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.ForceSpyglassPacket;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.ForceZoomDivisorPacket;
import org.thinkingstudio.rubidium_toolkit.features.zoom.utils.ZoomUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;

public class RubidiumToolkitConfigServer {
    public static final ForgeConfigSpec SPEC;

    public static final BooleanValue ALLOW_ZOOM;
    public static final BooleanValue DISABLE_ZOOM_SCROLLING;
    public static final BooleanValue FORCE_CLASSIC_MODE;
    public static final ForgeConfigSpec.DoubleValue MINIMUM_ZOOM_DIVISOR;
    public static final ForgeConfigSpec.DoubleValue MAXIMUM_ZOOM_DIVISOR;
    public static final ForgeConfigSpec.EnumValue<ConfigEnums.SpyglassDependency> SPYGLASS_DEPENDENCY;
    public static final ForgeConfigSpec.EnumValue<ConfigEnums.ZoomOverlays> ZOOM_OVERLAY;

    static {
        final var builder = new ForgeConfigSpec.Builder();

        ALLOW_ZOOM = builder.comment("If players should be allowed to zoom using OkZoomer.")
                .define("allow_zoom", true);
        DISABLE_ZOOM_SCROLLING = builder.comment("If false, allows players to use the scroll key to adjust zoom.")
                .define("disable_zoom_scrolling", false);
        FORCE_CLASSIC_MODE = builder.comment("If true, players will be forced to use classic mode for zooming.")
                .define("force_classic_mode", false);

        MINIMUM_ZOOM_DIVISOR = builder.comment("The minimum value that players can scroll down.")
                .defineInRange("minimum_zoom_divisor", 1.0D, 0D, Double.MAX_VALUE);
        MAXIMUM_ZOOM_DIVISOR = builder.comment("The maximum value that players can scroll down.")
                .defineInRange("maximum_zoom_divisor", 50.0D, 0D, Double.MAX_VALUE);

        SPYGLASS_DEPENDENCY = builder.comment("Enforce a spyglass dependency for zoom.")
                .defineEnum("spyglass_dependency", ConfigEnums.SpyglassDependency.OFF);
        ZOOM_OVERLAY = builder.comment("Enforce a zoom overlay over the zoom.")
                .defineEnum("zoom_overlay", ConfigEnums.ZoomOverlays.OFF);

        SPEC = builder.build();
    }

    @SubscribeEvent
    static void configChanged(final ModConfigEvent.Reloading event) {
        if (event.getConfig().getType() != ModConfig.Type.SERVER)
            return;
        ZoomUtils.LOGGER.info("THe RubidiumToolkit server config has been changed!");
        final var currentServer = ServerLifecycleHooks.getCurrentServer();
        if (currentServer != null) {
            currentServer.getPlayerList().getPlayers().forEach(RubidiumToolkitConfigServer::sendPacket);
        }
    }

    public static void sendPacket(ServerPlayer player) {
        if (RubidiumToolkitNetwork.EXISTENCE_CHANNEL.isRemotePresent(player.connection.connection)) {
            final var packets = new ArrayList<>();
            packets.add(new DisableZoomPacket(!ALLOW_ZOOM.get()));
            packets.add(new DisableZoomScrollingPacket(DISABLE_ZOOM_SCROLLING.get()));
            packets.add(new ForceClassicModePacket(FORCE_CLASSIC_MODE.get()));
            packets.add(new ForceZoomDivisorPacket(MINIMUM_ZOOM_DIVISOR.get(), MAXIMUM_ZOOM_DIVISOR.get()));
            packets.add(new ForceSpyglassPacket(SPYGLASS_DEPENDENCY.get()));
            packets.add(new ForceOverlayPacket(ZOOM_OVERLAY.get()));
            packets.add(new AcknowledgeModPacket());
            packets.forEach(pkt -> RubidiumToolkitNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), pkt));
        }
    }
}
