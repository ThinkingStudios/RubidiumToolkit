package com.texstudio.rubidium_toolkit.features.Zoom.network.packet;

import com.texstudio.rubidium_toolkit.config.ConfigEnums;
import com.texstudio.rubidium_toolkit.features.Zoom.network.RubidiumToolkitNetwork;
import com.texstudio.rubidium_toolkit.features.Zoom.utils.ZoomUtils;
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
