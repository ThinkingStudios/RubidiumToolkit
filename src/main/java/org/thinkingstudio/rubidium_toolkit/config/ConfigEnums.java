package org.thinkingstudio.rubidium_toolkit.config;

//import it.unimi.dsi.fastutil.floats.FloatIntPair;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

@MethodsReturnNonnullByDefault
public class ConfigEnums {

	public enum ZoomPresets {
		DEFAULT,
		CLASSIC,
		PERSISTENT,
		SPYGLASS
	}
	public enum CinematicCameraOptions implements StringRepresentable {
		OFF,
		VANILLA,
		MULTIPLIED;

		@Override
		public String getSerializedName() {
			return this.toString();
		}
	}

	public enum ZoomTransitionOptions implements StringRepresentable {
		OFF,
		SMOOTH,
		LINEAR;

		@Override
		public String getSerializedName() {
			return this.toString();
		}
	}

	public enum ZoomModes implements StringRepresentable {
		HOLD,
		TOGGLE,
		PERSISTENT;

		@Override
		public String getSerializedName() {
			return this.toString();
		}
	}

	public enum ZoomOverlays implements StringRepresentable {
		OFF,
		VIGNETTE,
		SPYGLASS;

		@Override
		public String getSerializedName() {
			return this.toString();
		}
	}

	public enum SpyglassDependency implements StringRepresentable {
		OFF,
		REQUIRE_ITEM,
		REPLACE_ZOOM,
		BOTH;

		@Override
		public String getSerializedName() {
			return this.toString();
		}
	}

	public enum QualityMode {

		OFF("Off"),
		SLOW("Slow"),
		FAST("Fast"),
		REALTIME("Realtime");

		private final String name;

		private QualityMode(String name) {
			this.name = name;
		}

		public Component getLocalizedName() {
			return Component.nullToEmpty(this.name);
		}
	}
}
