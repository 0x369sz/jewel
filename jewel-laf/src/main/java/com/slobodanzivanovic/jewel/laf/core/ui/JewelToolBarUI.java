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

package com.slobodanzivanovic.jewel.laf.core.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicToolBarUI;
import java.awt.*;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * UI delegate for JToolBar components in the Jewel Look and Feel.
 * Provides consistent border handling for toolbar buttons in both regular
 * and rollover states, with support for UI scaling and custom margins.
 */
public class JewelToolBarUI extends BasicToolBarUI {
	private static final String BUTTON_MARGINS_KEY = "ToolBar.buttonMargins";

	private Border rolloverBorder;

	/**
	 * Creates a new UI delegate for JToolBar.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelToolBarUI();
	}

	@Override
	public void uninstallUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		super.uninstallUI(component);
		rolloverBorder = null;
	}

	@Override
	protected Border createRolloverBorder() {
		return getRolloverBorder();
	}

	@Override
	protected Border createNonRolloverBorder() {
		return getRolloverBorder();
	}

	@Override
	protected Border getNonRolloverBorder(AbstractButton button) {
		Objects.requireNonNull(button, "Button cannot be null");
		return getRolloverBorder();
	}

	/**
	 * Returns the border used for both rollover and non-rollover states.
	 *
	 * @return the toolbar button border
	 */
	private Border getRolloverBorder() {
		if (rolloverBorder == null) {
			rolloverBorder = new JewelRolloverMarginBorder();
		}
		return rolloverBorder;
	}

	@Override
	public void setOrientation(int orientation) {
		if (orientation != toolBar.getOrientation()) {
			Insets currentMargin = toolBar.getMargin();
			Insets newMargin = new Insets(
				currentMargin.left,
				currentMargin.top,
				currentMargin.right,
				currentMargin.bottom
			);

			if (!newMargin.equals(currentMargin)) {
				toolBar.setMargin(newMargin);
			}
		}

		super.setOrientation(orientation);
	}

	/**
	 * Border implementation that handles button margins with UI scaling support.
	 * Uses the ToolBar.buttonMargins UI default value and applies scaling to all insets.
	 */
	private static class JewelRolloverMarginBorder extends EmptyBorder {
		/**
		 * Creates a new border with insets from UI defaults.
		 */
		public JewelRolloverMarginBorder() {
			super(Objects.requireNonNull(
				UIManager.getInsets(BUTTON_MARGINS_KEY),
				"ToolBar.buttonMargins not defined in UI defaults"
			));
		}

		@Override
		public Insets getBorderInsets(Component component, Insets insets) {
			Objects.requireNonNull(component, "Component cannot be null");
			Objects.requireNonNull(insets, "Insets cannot be null");

			Insets margin = null;
			if (component instanceof AbstractButton button) {
				margin = button.getMargin();
			}

			if (margin == null || margin instanceof UIResource) {
				// Use the border's own insets from EmptyBorder
				insets.top = top;
				insets.left = left;
				insets.bottom = bottom;
				insets.right = right;
			} else {
				copyInsets(margin, insets);
			}

			// Apply UI scaling to all insets
			scaleInsets(insets);
			return insets;
		}

		/**
		 * Copies insets values from source to target.
		 *
		 * @param source the source insets
		 * @param target the target insets to update
		 */
		private void copyInsets(Insets source, Insets target) {
			target.top = source.top;
			target.left = source.left;
			target.bottom = source.bottom;
			target.right = source.right;
		}

		/**
		 * Applies UI scaling to all insets values.
		 *
		 * @param insets the insets to scale
		 */
		private void scaleInsets(Insets insets) {
			insets.top = scale(insets.top);
			insets.left = scale(insets.left);
			insets.bottom = scale(insets.bottom);
			insets.right = scale(insets.right);
		}
	}
}
