package org.thinkingstudio.rubidium_toolkit.mixins.sodium;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import net.minecraft.client.resource.language.I18n;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thinkingstudio.rubidium_toolkit.config.ToolkitConfig;

import java.util.ArrayList;
import java.util.List;

@Mixin(SodiumOptionsGUI.class)
public class ToolkitOptionsPage {
    @Shadow
    @Final
    private List<OptionPage> pages;

    private static final SodiumOptionsStorage sodiumOpts = new SodiumOptionsStorage();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void Toolkit(Screen prevScreen, CallbackInfo ci) {
        List<OptionGroup> groups = new ArrayList<>();

        OptionImpl<SodiumGameOptions, Boolean> enableDistanceChecks = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName("Enable Max Entity Distance")
                .setTooltip("Toggles off entity culling.")
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> ToolkitConfig.enableDistanceChecks.set(value),
                        (options) -> ToolkitConfig.enableDistanceChecks.get())
                .setImpact(OptionImpact.LOW)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(enableDistanceChecks)
                .build()
        );

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName("Max Entity Distance")
                .setTooltip("Hides and does not tick entities beyond this many blocks. Huge performance increase, especially around modded farms.")
                .setControl((option) -> new SliderControl(option, 16, 192, 8, ControlValueFormatter.quantity("Blocks")))
                .setBinding(
                        (options, value) -> ToolkitConfig.maxEntityRenderDistanceSquare.set(value * value),
                        (options) ->  Math.toIntExact(Math.round(Math.sqrt(ToolkitConfig.maxEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.EXTREME)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName("Vertical Entity Distance")
                .setTooltip("Hides and does not tick entities underneath this many blocks, improving performance above caves. This should ideally be set lower than the horizontal distance.")
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.quantity("Blocks")))
                .setBinding(
                        (options, value) -> ToolkitConfig.maxEntityRenderDistanceY.set(value ),
                        (options) -> ToolkitConfig.maxEntityRenderDistanceY.get())
                .setImpact(OptionImpact.EXTREME)
                .build();


        groups.add(OptionGroup
                .createBuilder()
                .add(maxEntityDistance)
                .add(maxEntityDistanceVertical)
                .build()
        );





        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistance = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName("Max Tile Distance")
                .setTooltip("Hides block entities beyond this many blocks. Huge performance increase, especially around lots of modded machines.")
                .setControl((option) -> new SliderControl(option, 16, 256, 8, ControlValueFormatter.quantity("Blocks")))
                .setBinding(
                        (options, value) -> ToolkitConfig.maxBlockEntityRenderDistanceSquare.set(value * value),
                        (options) -> Math.toIntExact(Math.round(Math.sqrt(ToolkitConfig.maxBlockEntityRenderDistanceSquare.get()))))
                .setImpact(OptionImpact.HIGH)
                .build();

        OptionImpl<SodiumGameOptions, Integer> maxTileEntityDistanceVertical = OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName("Vertical Tile Distance")
                .setTooltip("Hides block entities underneath this many blocks, improving performance above caves (if you have your machines in caves, for some reason). This should ideally be set lower than the horizontal distance.")
                .setControl((option) -> new SliderControl(option, 16, 64, 4, ControlValueFormatter.quantity("Blocks")))
                .setBinding(
                        (options, value) -> ToolkitConfig.maxBlockEntityRenderDistanceY.set(value ),
                        (options) -> ToolkitConfig.maxBlockEntityRenderDistanceY.get())
                .setImpact(OptionImpact.HIGH)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(maxTileEntityDistance)
                .add(maxTileEntityDistanceVertical)
                .build()
        );

        pages.add(new OptionPage(I18n.translate("rubidium_toolkit.tools.options.name"),ImmutableList.copyOf(groups)));
    }
}
