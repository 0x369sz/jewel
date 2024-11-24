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

import com.slobodanzivanovic.jewel.laf.core.util.UIScale;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.util.Objects;

/**
 * UI delegate for JSplitPane components in the Jewel Look and Feel.
 * Provides custom divider implementation with one-touch expand/collapse buttons
 * and proper UI scaling support.
 */
public class JewelSplitPaneUI extends BasicSplitPaneUI {
	private static final String KEY_ARROW_TYPE = "Component.arrowType";
	private static final String KEY_CONTINUOUS_LAYOUT = "SplitPane.continuousLayout";
	private static final String KEY_ARROW_COLOR = "SplitPaneDivider.oneTouchArrowColor";
	private static final String KEY_DISABLED_ARROW_COLOR = "SplitPaneDivider.oneTouchDisabledArrowColor";
	private static final String KEY_HOVER_ARROW_COLOR = "SplitPaneDivider.oneTouchHoverArrowColor";
	private static final int DISABLED_ALPHA = 128;

	protected String arrowType;
	private Boolean continuousLayout;
	private Color oneTouchArrowColor;
	private Color oneTouchDisabledArrowColor;
	private Color oneTouchHoverArrowColor;

	/**
	 * Creates a new UI delegate for the specified component.
	 *
	 * @param component the component to create the UI delegate for
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelSplitPaneUI();
	}

	@Override
	protected void installDefaults() {
		arrowType = UIManager.getString(KEY_ARROW_TYPE);

		oneTouchArrowColor = UIManager.getColor(KEY_ARROW_COLOR);
		oneTouchDisabledArrowColor = UIManager.getColor(KEY_DISABLED_ARROW_COLOR);
		oneTouchHoverArrowColor = UIManager.getColor(KEY_HOVER_ARROW_COLOR);

		if (oneTouchArrowColor == null) {
			oneTouchArrowColor = Color.GRAY;
		}
		if (oneTouchDisabledArrowColor == null) {
			oneTouchDisabledArrowColor = new Color(
				oneTouchArrowColor.getRed(),
				oneTouchArrowColor.getGreen(),
				oneTouchArrowColor.getBlue(),
				DISABLED_ALPHA
			);
		}

		super.installDefaults();

		continuousLayout = (Boolean) UIManager.get(KEY_CONTINUOUS_LAYOUT);
	}

	@Override
	public boolean isContinuousLayout() {
		return super.isContinuousLayout() || Boolean.TRUE.equals(continuousLayout);
	}

	@Override
	public BasicSplitPaneDivider createDefaultDivider() {
		return new JewelSplitPaneDivider(this);
	}

	/**
	 * Custom divider implementation for the Jewel Look and Feel.
	 */
	private class JewelSplitPaneDivider extends BasicSplitPaneDivider {
		/**
		 * Creates a new divider for the specified UI.
		 *
		 * @param ui the UI this divider belongs to
		 */
		public JewelSplitPaneDivider(BasicSplitPaneUI ui) {
			super(ui);
		}

		@Override
		public void setDividerSize(int newSize) {
			super.setDividerSize(UIScale.scale(newSize));
		}

		@Override
		protected JButton createLeftOneTouchButton() {
			return new JewelOneTouchButton(true);
		}

		@Override
		protected JButton createRightOneTouchButton() {
			return new JewelOneTouchButton(false);
		}

		/**
		 * Button implementation for one-touch expand/collapse functionality.
		 */
		private class JewelOneTouchButton extends JewelArrowButton {
			private final boolean left;

			/**
			 * Creates a new one-touch button.
			 *
			 * @param left true for left/up button, false for right/down button
			 */
			public JewelOneTouchButton(boolean left) {
				super(SwingConstants.NORTH,
					arrowType,
					oneTouchArrowColor,
					oneTouchDisabledArrowColor,
					oneTouchHoverArrowColor,
					null);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				this.left = left;
			}

			@Override
			public int getDirection() {
				return orientation == JSplitPane.VERTICAL_SPLIT
					? (left ? SwingConstants.NORTH : SwingConstants.SOUTH)
					: (left ? SwingConstants.WEST : SwingConstants.EAST);
			}
		}
	}
}
