package com.texstudio.rubidium_toolkit.mixins.sodium;
import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;

import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import com.texstudio.rubidium_toolkit.dynamic_lights.RubidiumDynamicLights;
import com.texstudio.rubidium_toolkit.dynamic_lights.config.DynamicLightsConfig;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(SodiumOptionsGUI.class)
public class DynlightsOptionsPage {

    @Shadow
    @Final
    private List<OptionPage> pages;

    private static final SodiumOptionsStorage dynamicLightsOpts = new SodiumOptionsStorage();


    @Inject(method = "<init>", at = @At("RETURN"))
    private void DynamicLights(Screen prevScreen, CallbackInfo ci)
    {
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, DynamicLightsConfig.QualityMode> qualityMode = OptionImpl.createBuilder(DynamicLightsConfig.QualityMode.class, dynamicLightsOpts)
                .setName(I18n.get("rubidium_toolkit.dynlights.speed.name"))
                .setTooltip(I18n.get("rubidium_toolkit.dynlights.speed.tooltip"))
                .setControl(
                        (option) -> new CyclingControl<>(option, DynamicLightsConfig.QualityMode.class, new String[] {
                                I18n.get("rubidium_toolkit.option.off"),
                                I18n.get("rubidium_toolkit.option.slow"),
                                I18n.get("rubidium_toolkit.option.fast"),
                                I18n.get("rubidium_toolkit.option.realtime")
                        }
                        )
                )
                .setBinding(
                        (options, value) -> {
                            DynamicLightsConfig.Quality.set(value.toString());
                            RubidiumDynamicLights.clearLightSources();
                        },
                        (options) -> DynamicLightsConfig.QualityMode.valueOf(DynamicLightsConfig.Quality.get()))
                .setImpact(OptionImpact.MEDIUM)
                .build();


        OptionImpl<SodiumGameOptions, Boolean> entityLighting = OptionImpl.createBuilder(Boolean.class, dynamicLightsOpts)
                .setName(I18n.get("rubidium_toolkit.dynlights.entity_lights.name"))
                .setTooltip(I18n.get("rubidium_toolkit.dynlights.entity_lights.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> DynamicLightsConfig.EntityLighting.set(value),
                        (options) -> DynamicLightsConfig.EntityLighting.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> tileEntityLighting = OptionImpl.createBuilder(Boolean.class, dynamicLightsOpts)
                .setName(I18n.get("rubidium_toolkit.dynlights.block_lights.name"))
                .setTooltip(I18n.get("rubidium_toolkit.dynlights.block_lights.tooltip"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> DynamicLightsConfig.TileEntityLighting.set(value),
                        (options) -> DynamicLightsConfig.TileEntityLighting.get())
                .setImpact(OptionImpact.MEDIUM)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(qualityMode)
                .add(entityLighting)
                .add(tileEntityLighting)
                .build()
        );

        pages.add(new OptionPage(I18n.get("rubidium_toolkit.dynlights.options.name"), ImmutableList.copyOf(groups)));
    }
}
