package org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet;

import org.thinkingstudio.rubidium_toolkit.features.zoom.network.RubidiumToolkitNetwork;
import org.thinkingstudio.rubidium_toolkit.features.zoom.utils.ZoomUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record ForceZoomDivisorPacket(double min, double max) implements Packet {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(min());
        buf.writeDouble(max());
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        if (min <= 0.0 || max <= 0.0) {
            ZoomUtils.LOGGER.info("This server has attempted to set invalid divisor values! (min {}, max {})", min, max);
        } else if (min != 1.0D && max != 50.0D) {
            ZoomUtils.LOGGER.info("This server has set the zoom divisors to minimum {} and maximum {}", min, max);
            RubidiumToolkitNetwork.maximumZoomDivisor = max;
            RubidiumToolkitNetwork.minimumZoomDivisor = min;
            RubidiumToolkitNetwork.forceZoomDivisors = true;
            RubidiumToolkitNetwork.checkRestrictions();
            RubidiumToolkitNetwork.configureZoomInstance();
        }
    }

    public static ForceZoomDivisorPacket decode(FriendlyByteBuf buf) {
        return new ForceZoomDivisorPacket(buf.readDouble(), buf.readDouble());
    }
}
