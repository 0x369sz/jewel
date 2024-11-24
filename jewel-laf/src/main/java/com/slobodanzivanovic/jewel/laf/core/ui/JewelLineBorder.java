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

import java.awt.*;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * A custom border implementation that draws a single-line border with
 * specified insets and color, while supporting UI scaling.
 */
public class JewelLineBorder extends JewelEmptyBorder {
	private final Color lineColor;

	/**
	 * Creates a new line border with specified insets and color.
	 *
	 * @param insets    the border insets
	 * @param lineColor the color of the borderline
	 * @throws NullPointerException if either insets or lineColor is null
	 */
	public JewelLineBorder(Insets insets, Color lineColor) {
		super(Objects.requireNonNull(insets, "Insets cannot be null"));
		this.lineColor = Objects.requireNonNull(lineColor, "Line color cannot be null");
	}

	@Override
	public void paintBorder(Component component, Graphics g, int x, int y, int width, int height) {
		Objects.requireNonNull(component, "Component cannot be null");
		Objects.requireNonNull(g, "Graphics cannot be null");

		Graphics2D g2d = (Graphics2D) g.create();
		try {
			JewelUIUtils.setRenderingHints(g2d);
			g2d.setColor(lineColor);
			JewelUIUtils.drawRoundRectangle(g2d, x, y, width, height, 0f, scale(1f), 0f);
		} finally {
			g2d.dispose();
		}
	}
}
