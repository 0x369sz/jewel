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

import com.slobodanzivanovic.jewel.laf.core.ui.JewelBorder;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.Serial;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale2;

/**
 * Handles visual padding for components using MigLayout.
 * This class provides utilities to manage component padding when using MigLayout,
 * with support for dynamic updates based on property changes.
 */
public final class MigLayoutVisualPadding {
	/**
	 * Property key for storing visual padding information.
	 */
	public static final String VISUAL_PADDING_PROPERTY = "visualPadding";

	private static final JewelMigInsets ZERO = new JewelMigInsets(0, 0, 0, 0);
	private static final boolean migLayoutAvailable;

	static {
		migLayoutAvailable = isMigLayoutAvailable();
	}

	private MigLayoutVisualPadding() {
		throw new AssertionError("No MigLayoutVisualPadding instances for you!");
	}

	/**
	 * Checks if MigLayout is available in the classpath.
	 *
	 * @return true if MigLayout is available, false otherwise
	 */
	private static boolean isMigLayoutAvailable() {
		try {
			Class.forName("net.miginfocom.swing.MigLayout");
			return true;
		} catch (ClassNotFoundException ignored) {
			return false;
		}
	}

	/**
	 * Installs visual padding for a component with specified insets.
	 *
	 * @param component The target component
	 * @param insets    The padding insets to apply
	 */
	public static void install(JComponent component, Insets insets) {
		if (!migLayoutAvailable || component == null) return;

		setVisualPadding(component, insets);
	}

	/**
	 * Installs visual padding for a component with a focus width.
	 *
	 * @param component  The target component
	 * @param focusWidth The width of the focus border
	 */
	public static void install(JComponent component, int focusWidth) {
		if (!migLayoutAvailable || component == null) return;

		install(component, c ->
				(c.getBorder() instanceof JewelBorder)
					? new Insets(focusWidth, focusWidth, focusWidth, focusWidth)
					: null,
			"border"
		);
	}

	/**
	 * Installs visual padding for a component with a dynamic padding function and property triggers.
	 *
	 * @param component       The target component
	 * @param paddingFunction Function to calculate padding
	 * @param propertyNames   Properties that trigger padding updates
	 */
	public static void install(JComponent component,
							   Function<JComponent, Insets> paddingFunction,
							   String... propertyNames) {
		if (!migLayoutAvailable || component == null || paddingFunction == null) return;

		setVisualPadding(component, paddingFunction.apply(component));

		PropertyChangeListener listener = createPropertyChangeListener(component, paddingFunction, propertyNames);
		component.addPropertyChangeListener(listener);
	}

	/**
	 * Creates a property change listener for dynamic padding updates.
	 */
	private static PropertyChangeListener createPropertyChangeListener(
		JComponent component,
		Function<JComponent, Insets> paddingFunction,
		String... propertyNames) {
		return (JewelMigListener) e -> {
			String propertyName = e.getPropertyName();
			if (Arrays.asList(propertyNames).contains(propertyName)) {
				setVisualPadding(component, paddingFunction.apply(component));
			}
		};
	}

	/**
	 * Sets the visual padding for a component.
	 *
	 * @param component     The target component
	 * @param visualPadding The padding to apply
	 */
	private static void setVisualPadding(JComponent component, Insets visualPadding) {
		Object oldPadding = component.getClientProperty(VISUAL_PADDING_PROPERTY);

		if (oldPadding == null || oldPadding instanceof JewelMigInsets) {
			JewelMigInsets scaledPadding = Optional.ofNullable(visualPadding)
				.map(padding -> new JewelMigInsets(
					scale2(padding.top),
					scale2(padding.left),
					scale2(padding.bottom),
					scale2(padding.right)
				))
				.orElse(ZERO);

			component.putClientProperty(VISUAL_PADDING_PROPERTY, scaledPadding);
		}
	}

	/**
	 * Uninstalls visual padding from a component.
	 *
	 * @param component The component to uninstall padding from
	 */
	public static void uninstall(JComponent component) {
		if (!migLayoutAvailable || component == null) return;

		Arrays.stream(component.getPropertyChangeListeners())
			.filter(l -> l instanceof JewelMigListener)
			.findFirst()
			.ifPresent(component::removePropertyChangeListener);

		if (component.getClientProperty(VISUAL_PADDING_PROPERTY) instanceof JewelMigInsets) {
			component.putClientProperty(VISUAL_PADDING_PROPERTY, null);
		}
	}

	/**
	 * Custom Insets class for MigLayout padding.
	 */
	private static final class JewelMigInsets extends Insets {
		@Serial
		private static final long serialVersionUID = 1L;

		JewelMigInsets(int top, int left, int bottom, int right) {
			super(top, left, bottom, right);
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) return true;
			if (!(other instanceof JewelMigInsets that)) return false;
			return this.top == that.top
				&& this.left == that.left
				&& this.bottom == that.bottom
				&& this.right == that.right;
		}

		@Override
		public int hashCode() {
			return Objects.hash(top, left, bottom, right);
		}

		@Override
		public String toString() {
			return String.format("JewelMigInsets[top=%d,left=%d,bottom=%d,right=%d]",
				top, left, bottom, right);
		}
	}

	/**
	 * Marker interface for property change listeners specific to MigLayout padding.
	 */
	@FunctionalInterface
	private interface JewelMigListener extends PropertyChangeListener {
	}
}
