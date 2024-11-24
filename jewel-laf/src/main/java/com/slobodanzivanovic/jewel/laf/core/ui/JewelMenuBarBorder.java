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
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * A border for menu bars in the Jewel Look and Feel.
 * Provides a bottom line border with UI scaling support.
 */
public class JewelMenuBarBorder extends JewelMarginBorder {
	private static final String BORDER_COLOR_KEY = "MenuBar.borderColor";
	private static final float LINE_HEIGHT = 1.0f;
	private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

	private final Color borderColor;

	/**
	 * Creates a new menu bar border with the default UI manager color.
	 */
	public JewelMenuBarBorder() {
		this.borderColor = UIManager.getColor(BORDER_COLOR_KEY);
	}

	@Override
	public void paintBorder(Component component, Graphics g, int x, int y, int width, int height) {
		Objects.requireNonNull(component, "Component cannot be null");
		Objects.requireNonNull(g, "Graphics cannot be null");

		if (width <= 0 || height <= 0) {
			return;
		}

		Graphics2D g2d = (Graphics2D) g.create();
		try {
			float scaledLineHeight = scale(LINE_HEIGHT);

			JewelUIUtils.setRenderingHints(g2d);
			g2d.setColor(borderColor);
			g2d.fill(new Rectangle2D.Float(x, y + height - scaledLineHeight, width, scaledLineHeight));
		} finally {
			g2d.dispose();
		}
	}

	@Override
	public Insets getBorderInsets(Component component, Insets insets) {
		Objects.requireNonNull(component, "Component cannot be null");
		Objects.requireNonNull(insets, "Insets cannot be null");

		Insets margin = (component instanceof JMenuBar menuBar)
			? menuBar.getMargin()
			: EMPTY_INSETS;

		insets.top = scale(margin.top);
		insets.left = scale(margin.left);
		insets.bottom = (int) scale(margin.bottom + LINE_HEIGHT);
		insets.right = scale(margin.right);

		return insets;
	}
}
