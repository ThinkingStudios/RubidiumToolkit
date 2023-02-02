package org.thinkingstudio.rubidium_toolkit.mixin.sodium;

import com.google.common.collect.ImmutableList;
import org.thinkingstudio.rubidium_toolkit.config.ConfigEnum;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;
import org.thinkingstudio.rubidium_toolkit.dynlights.DynLightsFeatures;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(SodiumOptionsGUI.class)
public abstract class DynLightsOptionsMixin {

    @Shadow
    @Final
    private List<OptionPage> pages;

    private static final SodiumOptionsStorage dynamicLightsOpts = new SodiumOptionsStorage();


    @Inject(method = "<init>", at = @At("RETURN"))
    private void DynamicLights(Screen prevScreen, CallbackInfo ci)
    {
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, ConfigEnum.QualityMode> qualityMode = OptionImpl.createBuilder(ConfigEnum.QualityMode.class, dynamicLightsOpts)
                .setName(Component.nullToEmpty("Dynamic Lights Speed"))
                .setTooltip(Component.nullToEmpty("""
                        Controls how often dynamic lights will update.\s

                        Lighting recalculation can be expensive, so slower values will give better performance.

                        Off - Self explanatory
                        Slow - Twice a second
                        Fast - Five times a second
                        Realtime - Every tick"""))
                .setControl(
                        (option) -> new CyclingControl<>(option, ConfigEnum.QualityMode.class, new Component[] {
                                Component.nullToEmpty("Off"),
                                Component.nullToEmpty("Slow"),
                                Component.nullToEmpty("Fast"),
                                Component.nullToEmpty("Realtime")
                        }))
                .setBinding(
                        (options, value) -> {
                            ToolkitConfig.Quality.set(ConfigEnum.QualityMode.valueOf(value.toString()));
                            DynLightsFeatures.get().clearLightSources();
                        },
                        (options) -> ConfigEnum.QualityMode.valueOf(String.valueOf(ToolkitConfig.Quality.get())))
                .setImpact(OptionImpact.MEDIUM)
                .build();


        OptionImpl<SodiumGameOptions, Boolean> entityLighting = OptionImpl.createBuilder(Boolean.class, dynamicLightsOpts)
                .setName(Component.nullToEmpty("Dynamic Entity Lights"))
                .setTooltip(Component.nullToEmpty("""
                        Turning this on will show dynamic lighting on entities (dropped items, mobs, etc).\s

                        This can drastically increase the amount of lighting updates, even when you're not holding a torch."""))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> ToolkitConfig.EntityLighting.set(value),
                        (options) -> ToolkitConfig.EntityLighting.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> tileEntityLighting = OptionImpl.createBuilder(Boolean.class, dynamicLightsOpts)
                .setName(Component.nullToEmpty("Dynamic Block Lights"))
                .setTooltip(Component.nullToEmpty("""
                        Turning this on will show dynamic lighting on tile entities (furnaces, modded machines, etc).\s

                        This can drastically increase the amount of lighting updates, even when you're not holding a torch."""))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> ToolkitConfig.TileEntityLighting.set(value),
                        (options) -> ToolkitConfig.TileEntityLighting.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(qualityMode)
                .add(entityLighting)
                .add(tileEntityLighting)
                .build()
        );

        pages.add(new OptionPage(Component.nullToEmpty("Dynamic Lights"), ImmutableList.copyOf(groups)));
    }


}