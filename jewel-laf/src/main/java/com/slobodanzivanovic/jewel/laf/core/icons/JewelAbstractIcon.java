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

package com.slobodanzivanovic.jewel.laf.core.icons;

import com.slobodanzivanovic.jewel.laf.core.util.JewelUIUtils;
import com.slobodanzivanovic.jewel.laf.core.util.UIScale;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * Abstract base class for Jewel Look and Feel icons. Provides common functionality for
 * icon scaling, rendering hints, and basic icon properties. Implementing classes should
 * override {@link #paintIcon(Component, Graphics2D)} to define specific icon appearance.
 */
public abstract class JewelAbstractIcon implements Icon, UIResource {
	/**
	 * The unscaled width of the icon
	 */
	protected final int width;

	/**
	 * The unscaled height of the icon
	 */
	protected final int height;

	/**
	 * The color to paint the icon with, may be null for implementation-specific colors
	 */
	protected final Color color;

	/**
	 * Creates a new icon with the specified dimensions and color.
	 *
	 * @param width  the unscaled width of the icon
	 * @param height the unscaled height of the icon
	 * @param color  the color to paint the icon with, may be null
	 * @throws IllegalArgumentException if width or height is less than or equal to 0
	 */
	public JewelAbstractIcon(int width, int height, Color color) {
		if (width <= 0) {
			throw new IllegalArgumentException("Width must be positive: " + width);
		}
		if (height <= 0) {
			throw new IllegalArgumentException("Height must be positive: " + height);
		}
		this.width = width;
		this.height = height;
		this.color = color;
	}

	@Override
	public void paintIcon(Component component, Graphics g, int x, int y) {
		Objects.requireNonNull(component, "Component cannot be null");
		Objects.requireNonNull(g, "Graphics cannot be null");

		Graphics2D g2 = (Graphics2D) g.create();
		try {
			JewelUIUtils.setRenderingHints(g2);
			g2.translate(x, y);
			UIScale.scaleGraphics(g2);

			if (color != null) {
				g2.setColor(color);
			}

			paintIcon(component, g2);
		} finally {
			g2.dispose();
		}
	}

	/**
	 * Paints the icon using the provided Graphics2D context. The context is already
	 * translated to (x,y) and scaled according to the UI scale factor.
	 *
	 * @param component the component to paint relative to
	 * @param g2        the Graphics2D context to paint with
	 */
	protected abstract void paintIcon(Component component, Graphics2D g2);

	@Override
	public int getIconWidth() {
		return scale(width);
	}

	@Override
	public int getIconHeight() {
		return scale(height);
	}
}
