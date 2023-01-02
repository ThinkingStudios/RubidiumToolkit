package org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet;

import org.thinkingstudio.rubidium_toolkit.config.ConfigEnums;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.RubidiumToolkitNetwork;
import org.thinkingstudio.rubidium_toolkit.features.zoom.utils.ZoomUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record ForceOverlayPacket(ConfigEnums.ZoomOverlays overlay) implements Packet {

    public static ForceOverlayPacket decode(FriendlyByteBuf buf) {
        return new ForceOverlayPacket(buf.readEnum(ConfigEnums.ZoomOverlays.class));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(overlay());
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        if (overlay != ConfigEnums.ZoomOverlays.OFF) {
            ZoomUtils.LOGGER.info("This server has imposed an overlay on the zoom: {}", overlay);
            RubidiumToolkitNetwork.spyglassOverlay = overlay;
            RubidiumToolkitNetwork.checkRestrictions();
            RubidiumToolkitNetwork.configureZoomInstance();
        }
    }
}
