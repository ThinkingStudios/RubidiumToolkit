package com.texstudio.rubidium_toolkit.features.Zoom.network.packet;

import com.texstudio.rubidium_toolkit.config.ConfigEnums;
import com.texstudio.rubidium_toolkit.features.Zoom.network.RubidiumToolkitNetwork;
import com.texstudio.rubidium_toolkit.features.Zoom.utils.ZoomUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record ForceSpyglassPacket(ConfigEnums.SpyglassDependency dependency) implements Packet {

    public static ForceSpyglassPacket decode(FriendlyByteBuf buf) {
        return new ForceSpyglassPacket(buf.readEnum(ConfigEnums.SpyglassDependency.class));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(dependency);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        RubidiumToolkitNetwork.spyglassDependency = dependency;
        RubidiumToolkitNetwork.checkRestrictions();
        RubidiumToolkitNetwork.configureZoomInstance();
        if (dependency() != ConfigEnums.SpyglassDependency.OFF)
            ZoomUtils.LOGGER.info("This server has the following spyglass restriction: {}", dependency);
    }
}
