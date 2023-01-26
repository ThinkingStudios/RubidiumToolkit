package org.thinkingstudio.rubidium_toolkit.features.zoom;

import com.mojang.brigadier.Command;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.command.EnumArgument;
import org.thinkingstudio.rubidium_toolkit.config.ConfigEnums;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfigClient;
import org.thinkingstudio.rubidium_toolkit.config.RubidiumToolkitConfigServer;
import org.thinkingstudio.rubidium_toolkit.features.zoom.api.OkZoomerAPI;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.RubidiumToolkitNetwork;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.*;

import java.util.function.Function;

public class ToolkitZoom {
    public static void zoomPacketRegister() {
        class PacketRegister {
            int pktIndex = 0;

            <T extends Packet> void register(Class<T> clazz, Function<FriendlyByteBuf, T> decode) {
                RubidiumToolkitNetwork.CHANNEL.messageBuilder(clazz, pktIndex++)
                        .encoder(Packet::encode)
                        .decoder(decode)
                        .consumer((pkt, sup) -> {
                            pkt.handle(sup.get());
                            return true;
                        })
                        .add();
            }
        }

        final var packets = new PacketRegister();
        packets.register(DisableZoomPacket.class, DisableZoomPacket::decode);
        packets.register(DisableZoomScrollingPacket.class, DisableZoomScrollingPacket::decode);
        packets.register(ForceClassicModePacket.class, ForceClassicModePacket::decode);
        packets.register(ForceZoomDivisorPacket.class, ForceZoomDivisorPacket::decode);
        packets.register(AcknowledgeModPacket.class, AcknowledgeModPacket::decode);
        packets.register(ForceSpyglassPacket.class, ForceSpyglassPacket::decode);
        packets.register(ForceOverlayPacket.class, ForceOverlayPacket::decode);
        packets.register(ResetRestrictionsPacket.class, ResetRestrictionsPacket::decode);
    }

    public static void registerClientCommands(final RegisterClientCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(OkZoomerAPI.MOD_ID)
                .then(Commands.literal("client")
                        .then(Commands.literal("config")
                                .then(Commands.literal("preset")
                                        .then(Commands.argument("preset", EnumArgument.enumArgument(ConfigEnums.ZoomPresets.class))
                                                .executes(context -> {
                                                    final var preset = context.getArgument("preset", ConfigEnums.ZoomPresets.class);
                                                    RubidiumToolkitConfigClient.resetToPreset(preset);
                                                    context.getSource().sendSuccess(new TranslatableComponent("command.rubidium_toolkit.client.config_present", new TextComponent(preset.toString()).withStyle(ChatFormatting.AQUA)), false);
                                                    return Command.SINGLE_SUCCESS;
                                                }))))));
    }

    public static void onPlayerLogin(final PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            RubidiumToolkitConfigServer.sendPacket(player);
        }
    }

    public static void onPlayerLogout(final PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player && RubidiumToolkitNetwork.EXISTENCE_CHANNEL.isRemotePresent(player.connection.getConnection())) {
            RubidiumToolkitNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new ResetRestrictionsPacket());
        }
    }
}
