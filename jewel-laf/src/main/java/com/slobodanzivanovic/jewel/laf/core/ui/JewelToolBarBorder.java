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

import com.slobodanzivanovic.jewel.laf.core.util.JewelUIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * Border implementation for toolbar components in the Jewel Look and Feel.
 * Provides custom grip painting for floatable toolbars with configurable
 * dot size and spacing.
 */
public class JewelToolBarBorder extends JewelMarginBorder {

	private static final String KEY_GRIP_COLOR = "ToolBar.gripColor";

	private static final int DOT_COUNT = 4;
	private static final int DOT_SIZE = 2;
	private static final int GRIP_WIDTH = DOT_SIZE * 3;

	protected final Color gripColor;

	/**
	 * Creates a new toolbar border with the configured grip color.
	 */
	public JewelToolBarBorder() {
		this.gripColor = UIManager.getColor(KEY_GRIP_COLOR);
	}

	@Override
	public void paintBorder(Component component, Graphics g, int x, int y, int width, int height) {
		Objects.requireNonNull(component, "Component cannot be null");
		Objects.requireNonNull(g, "Graphics cannot be null");

		if (component instanceof JToolBar toolBar && toolBar.isFloatable()) {
			Graphics2D g2 = (Graphics2D) g.create();
			try {
				JewelUIUtils.setRenderingHints(g2);
				g2.setColor(gripColor);
				paintGrip(component, g2, x, y, width, height);
			} finally {
				g2.dispose();
			}
		}
	}

	/**
	 * Paints the grip dots for floatable toolbars.
	 *
	 * @param component the toolbar component
	 * @param g         the graphics context
	 * @param x         the x coordinate
	 * @param y         the y coordinate
	 * @param width     the width of the toolbar
	 * @param height    the height of the toolbar
	 */
	protected void paintGrip(Component component, Graphics g, int x, int y, int width, int height) {
		Objects.requireNonNull(component, "Component cannot be null");
		Objects.requireNonNull(g, "Graphics cannot be null");

		JToolBar toolBar = (JToolBar) component;
		int dotSize = scale(DOT_SIZE);
		int gripSize = (dotSize * DOT_COUNT) + ((dotSize * (DOT_COUNT - 1)));
		Insets insets = getBorderInsets(component);
		boolean horizontal = toolBar.getOrientation() == SwingConstants.HORIZONTAL;

		int startX = calculateGripX(component, x, width, insets, horizontal, dotSize);
		int startY = calculateGripY(y, height, insets, horizontal, gripSize, dotSize);

		paintDots(g, startX, startY, dotSize, dotSize, horizontal);
	}

	private int calculateGripX(Component component, int x, int width, Insets insets,
							   boolean horizontal, int dotSize) {
		if (horizontal) {
			return component.getComponentOrientation().isLeftToRight()
				? x + insets.left - (dotSize * 2)
				: x + width - insets.right + dotSize;
		} else {
			return x + Math.round((width - ((dotSize * DOT_COUNT) +
				(dotSize * (DOT_COUNT - 1)))) / 2f);
		}
	}

	private int calculateGripY(int y, int height, Insets insets, boolean horizontal,
							   int gripSize, int dotSize) {
		if (horizontal) {
			return y + Math.round((height - gripSize) / 2f);
		} else {
			return y + insets.top - (dotSize * 2);
		}
	}

	private void paintDots(Graphics g, int x, int y, int dotSize, int gapSize, boolean horizontal) {
		for (int i = 0; i < DOT_COUNT; i++) {
			g.fillOval(x, y, dotSize, dotSize);
			if (horizontal) {
				y += dotSize + gapSize;
			} else {
				x += dotSize + gapSize;
			}
		}
	}

	@Override
	public Insets getBorderInsets(Component component, Insets insets) {
		Objects.requireNonNull(component, "Component cannot be null");
		Objects.requireNonNull(insets, "Insets cannot be null");

		insets = super.getBorderInsets(component, insets);

		if (component instanceof JToolBar toolBar && toolBar.isFloatable()) {
			int gripInset = scale(GRIP_WIDTH);
			if (toolBar.getOrientation() == SwingConstants.HORIZONTAL) {
				if (component.getComponentOrientation().isLeftToRight()) {
					insets.left += gripInset;
				} else {
					insets.right += gripInset;
				}
			} else {
				insets.top += gripInset;
			}
		}

		return insets;
	}
}
