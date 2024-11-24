/*
 * Copyright (C) 2024 Slobodan Zivanovic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.slobodanzivanovic.jewel.laf.core.util;

import com.slobodanzivanovic.jewel.util.logging.Logger;
import com.slobodanzivanovic.jewel.util.platform.PlatformInfo;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handles UI scaling for high DPI displays and user preferences.
 * Supports both system-level scaling and user-defined scaling factors.
 */
public final class UIScale {
	private static final Logger logger;
	private static final String HIDPI_PROPERTY = "hidpi";
	private static final String UI_SCALE_PROPERTY = "jewellaf.uiScale";
	private static final String JAVA_UI_SCALE_PROPERTY = "sun.java2d.uiScale";

	private static final AtomicBoolean initialized = new AtomicBoolean(false);
	private static volatile float scaleFactor = 1.0f;
	private static volatile Boolean jreHiDPI;

	static {
		try {
			logger = new Logger("jewel-ui-scale");
		} catch (java.io.IOException e) {
			System.err.println("Failed to initialize logger: " + e.getMessage());
			throw new ExceptionInInitializerError(e);
		}
	}

	private UIScale() {
		throw new AssertionError("No UIScale instances for you!");
	}

	/**
	 * Checks if system-level HiDPI scaling is enabled.
	 */
	public static boolean isSystemScalingEnabled() {
		if (jreHiDPI == null) {
			jreHiDPI = true;
			logger.debug("System scaling initialized: enabled");
		}
		return jreHiDPI;
	}

	/**
	 * Gets the system scaling factor for the given graphics configuration.
	 */
	public static double getSystemScaleFactor(GraphicsConfiguration gc) {
		if (gc == null) {
			throw new IllegalArgumentException("GraphicsConfiguration cannot be null");
		}
		return isSystemScalingEnabled() ? gc.getDefaultTransform().getScaleX() : 1;
	}

	/**
	 * Initializes the scaling system if not already initialized.
	 */
	private static void initialize() {
		if (!initialized.compareAndSet(false, true)) {
			return;
		}

		if (isUserScalingEnabled()) {
			PropertyChangeListener listener = createPropertyChangeListener();
			UIManager.addPropertyChangeListener(listener);
			UIManager.getLookAndFeelDefaults().addPropertyChangeListener(listener);
			updateScaleFactor();
			logger.debug("UI scaling initialized with factor: " + scaleFactor);
		}
	}

	/**
	 * Creates a property change listener for UI updates.
	 */
	private static PropertyChangeListener createPropertyChangeListener() {
		return e -> {
			String propName = e.getPropertyName();
			if ("lookAndFeel".equals(propName)) {
				if (e.getNewValue() instanceof LookAndFeel) {
					UIManager.getLookAndFeelDefaults().addPropertyChangeListener(event -> {
						if ("Label.font".equals(event.getPropertyName())) {
							updateScaleFactor();
						}
					});
				}
				updateScaleFactor();
			} else if ("Label.font".equals(propName)) {
				updateScaleFactor();
			}
		};
	}

	/**
	 * Updates the scale factor based on current UI settings.
	 */
	private static void updateScaleFactor() {
		if (!isUserScalingEnabled()) {
			return;
		}

		Font font = UIManager.getFont("Label.font");
		if (font != null) {
			setUserScaleFactor(computeScaleFactor(font));
		}
	}

	/**
	 * Computes the scale factor based on font size and platform.
	 */
	private static float computeScaleFactor(Font font) {
		float fontSizeDivider = switch (PlatformInfo.getPlatform()) {
			case WINDOWS -> "Tahoma".equals(font.getFamily()) ? 11f : 12f;
			case LINUX -> 14f;  // Use a standard value for Linux
			default -> 12f;
		};
		return font.getSize() / fontSizeDivider;
	}

	/**
	 * Checks if user-defined scaling is enabled.
	 */
	private static boolean isUserScalingEnabled() {
		if (isSystemScalingEnabled() && !PlatformInfo.IS_LINUX) {
			return false;
		}
		String hidpi = System.getProperty(HIDPI_PROPERTY);
		return hidpi == null || Boolean.parseBoolean(hidpi);
	}

	/**
	 * Applies custom scale factor to fonts.
	 */
	public static FontUIResource applyCustomScaleFactor(FontUIResource font) {
		if (font == null || isSystemScalingEnabled()) {
			return font;
		}

		String uiScale = System.getProperty(UI_SCALE_PROPERTY, System.getProperty(JAVA_UI_SCALE_PROPERTY));
		float customScaleFactor = parseScaleFactor(uiScale);

		if (customScaleFactor <= 0) {
			return font;
		}

		float fontScaleFactor = computeScaleFactor(font);
		if (Math.abs(customScaleFactor - fontScaleFactor) < 0.01f) {
			return font;
		}

		int newFontSize = Math.round((font.getSize() / fontScaleFactor) * customScaleFactor);
		return new FontUIResource(font.getFamily(), font.getStyle(), newFontSize);
	}

	/**
	 * Parses scale factor from string representation.
	 */
	private static float parseScaleFactor(String value) {
		if (value == null || value.isEmpty()) {
			return -1;
		}

		try {
			if (value.endsWith("x")) {
				return Float.parseFloat(value.substring(0, value.length() - 1));
			}
			if (value.endsWith("dpi")) {
				return Float.parseFloat(value.substring(0, value.length() - 3)) / 96;
			}
			if (value.endsWith("%")) {
				return Float.parseFloat(value.substring(0, value.length() - 1)) / 100;
			}
			return Float.parseFloat(value);
		} catch (NumberFormatException ex) {
			logger.error("Invalid scale factor format: " + value);
			return -1;
		}
	}

	// Scaling operation methods
	public static float scale(float value) {
		initialize();
		return scaleFactor == 1 ? value : value * scaleFactor;
	}

	public static int scale(int value) {
		initialize();
		return scaleFactor == 1 ? value : Math.round(value * scaleFactor);
	}

	public static int scale2(int value) {
		initialize();
		return scaleFactor == 1 ? value : (int) (value * scaleFactor);
	}

	public static void scaleGraphics(Graphics2D g) {
		if (g == null) {
			throw new IllegalArgumentException("Graphics context cannot be null");
		}
		initialize();
		if (scaleFactor != 1f) {
			g.scale(scaleFactor, scaleFactor);
		}
	}

	public static Dimension scale(Dimension dimension) {
		if (dimension == null || scaleFactor == 1f) {
			return dimension;
		}
		initialize();

		if (dimension instanceof UIResource) {
			return new DimensionUIResource(
				scale(dimension.width),
				scale(dimension.height)
			);
		}
		return new Dimension(
			scale(dimension.width),
			scale(dimension.height)
		);
	}

	public static Insets scale(Insets insets) {
		if (insets == null || scaleFactor == 1f) {
			return insets;
		}
		initialize();

		if (insets instanceof UIResource) {
			return new InsetsUIResource(
				scale(insets.top),
				scale(insets.left),
				scale(insets.bottom),
				scale(insets.right)
			);
		}
		return new Insets(
			scale(insets.top),
			scale(insets.left),
			scale(insets.bottom),
			scale(insets.right)
		);
	}

	/**
	 * Gets the current user scale factor.
	 */
	public static float getUserScaleFactor() {
		initialize();
		return scaleFactor;
	}

	/**
	 * Sets the user scale factor with proper rounding.
	 */
	private static void setUserScaleFactor(float newScaleFactor) {
		if (newScaleFactor <= 1f) {
			scaleFactor = 1f;
		} else {
			scaleFactor = Math.round(newScaleFactor * 4f) / 4f;
		}
		logger.debug("Scale factor updated to: " + scaleFactor);
	}
}
