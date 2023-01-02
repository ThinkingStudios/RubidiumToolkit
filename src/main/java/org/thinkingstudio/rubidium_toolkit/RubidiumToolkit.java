package org.thinkingstudio.rubidium_toolkit;

import org.thinkingstudio.rubidium_toolkit.features.zoom.api.OkZoomerAPI;
import org.thinkingstudio.rubidium_toolkit.config.ClientConfig;
import org.thinkingstudio.rubidium_toolkit.config.ServerConfig;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.RubidiumToolkitNetwork;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.AcknowledgeModPacket;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.DisableZoomPacket;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.DisableZoomScrollingPacket;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.ForceClassicModePacket;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.ForceOverlayPacket;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.ForceSpyglassPacket;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.ForceZoomDivisorPacket;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.Packet;
import org.thinkingstudio.rubidium_toolkit.features.zoom.network.packet.ResetRestrictionsPacket;
import com.mojang.brigadier.Command;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.command.EnumArgument;

import java.util.function.Function;

@Mod(RubidiumToolkit.MOD_ID)
public class RubidiumToolkit {

    public static final String MOD_ID = "rubidium_toolkit";

    public RubidiumToolkit() {

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, "RubidiumToolkit/" + RubidiumToolkit.MOD_ID + "-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC, "RubidiumToolkit/" + RubidiumToolkit.MOD_ID + "-sever.toml");

        final var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(RubidiumToolkit::commonSetup);
        modBus.register(ClientConfig.class);
        modBus.register(ServerConfig.class);

        MinecraftForge.EVENT_BUS.addListener(RubidiumToolkit::registerClientCommands);
        MinecraftForge.EVENT_BUS.addListener(RubidiumToolkit::onPlayerLogout);
        MinecraftForge.EVENT_BUS.addListener(RubidiumToolkit::onPlayerLogin);
    }

    static void commonSetup(final FMLCommonSetupEvent event) {
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

    static void registerClientCommands(final RegisterClientCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(OkZoomerAPI.MOD_ID)
            .then(Commands.literal("client")
                .then(Commands.literal("config")
                    .then(Commands.literal("preset")
                            .then(Commands.argument("preset", EnumArgument.enumArgument(ClientConfig.ZoomPresets.class))
                            .executes(context -> {
                                final var preset = context.getArgument("preset", ClientConfig.ZoomPresets.class);
                                ClientConfig.resetToPreset(preset);
                                context.getSource().sendSuccess(new TranslatableComponent("command.rubidium_toolkit.client.config_present", new TextComponent(preset.toString()).withStyle(ChatFormatting.AQUA)), false);
                                return Command.SINGLE_SUCCESS;
                            }))))));
    }

    static void onPlayerLogin(final PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
             ServerConfig.sendPacket(player);
        }
    }

    static void onPlayerLogout(final PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player && RubidiumToolkitNetwork.EXISTENCE_CHANNEL.isRemotePresent(player.connection.getConnection())) {
            RubidiumToolkitNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new ResetRestrictionsPacket());
        }
    }
}
