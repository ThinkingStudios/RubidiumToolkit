package org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet;

import org.thinkingstudio.rubidium_toolkit.features.zoom.network.RubidiumToolkitNetwork;
import org.thinkingstudio.rubidium_toolkit.features.zoom.utils.ZoomUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record DisableZoomScrollingPacket(boolean disableScrolling) implements Packet {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(disableScrolling);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        RubidiumToolkitNetwork.disableZoomScrolling = disableScrolling;
        RubidiumToolkitNetwork.checkRestrictions();
        if (disableScrolling) {
            ZoomUtils.LOGGER.info("This server has disabled zoom scrolling");
        }
        RubidiumToolkitNetwork.configureZoomInstance();
    }

    public static DisableZoomScrollingPacket decode(FriendlyByteBuf buf) {
        return new DisableZoomScrollingPacket(buf.readBoolean());
    }
}
