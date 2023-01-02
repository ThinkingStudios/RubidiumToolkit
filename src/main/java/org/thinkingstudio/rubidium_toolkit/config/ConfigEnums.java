package org.thinkingstudio.rubidium_toolkit.config;

//import it.unimi.dsi.fastutil.floats.FloatIntPair;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.StringRepresentable;

@MethodsReturnNonnullByDefault
public class ConfigEnums {
	public enum Complexity implements StringRepresentable
	{
		OFF("Off"),
		SIMPLE("Simple"),
		ADVANCED("Advanced");

		private final String name;

		Complexity(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}
	}
	public enum DarknessOptions {
		PITCH_BLACK(0f),
		REALLY_DARK (0.04f),
		DARK(0.08f),
		DIM(0.12f);

		public final float value;

		DarknessOptions(float value) {
			this.value = value;
		}
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
}
