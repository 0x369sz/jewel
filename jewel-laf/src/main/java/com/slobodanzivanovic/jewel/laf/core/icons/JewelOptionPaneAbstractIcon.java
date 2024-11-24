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

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Objects;

/**
 * Abstract base class for JOptionPane icons in the Jewel Look and Feel.
 * Provides common functionality for painting two-layer icons with an outer
 * and inner shape. The icon can be painted either with separate colors for
 * each layer or as a single shape with holes using even-odd winding rule.
 */
public abstract class JewelOptionPaneAbstractIcon extends JewelAbstractIcon {
	private static final String FOREGROUND_KEY = "OptionPane.icon.foreground";
	private static final int DEFAULT_SIZE = 32;

	/**
	 * The foreground color for the inner shape of the icon
	 */
	protected final Color foreground;

	/**
	 * Creates a new option pane icon with the specified background color key.
	 * The icon will have default dimensions of 32x32 pixels.
	 *
	 * @param colorKey the UI manager key for the background color
	 * @throws NullPointerException if colorKey is null
	 */
	protected JewelOptionPaneAbstractIcon(String colorKey) {
		super(DEFAULT_SIZE, DEFAULT_SIZE, UIManager.getColor(Objects.requireNonNull(colorKey, "Color key cannot be null")));
		this.foreground = UIManager.getColor(FOREGROUND_KEY);
	}

	@Override
	protected void paintIcon(Component component, Graphics2D g) {
		if (foreground != null) {
			// Paint as two separate shapes with different colors
			g.fill(createOutside());

			g.setColor(foreground);
			g.fill(createInside());
		} else {
			// Paint as a single shape with holes using even-odd winding
			Path2D compositePath = new Path2D.Float(Path2D.WIND_EVEN_ODD);
			compositePath.append(createOutside(), false);
			compositePath.append(createInside(), false);
			g.fill(compositePath);
		}
	}

	/**
	 * Creates the outer shape of the icon.
	 *
	 * @return the Shape representing the outer part of the icon
	 */
	protected abstract Shape createOutside();

	/**
	 * Creates the inner shape of the icon. This shape will either be
	 * painted in the foreground color or used to create holes in the
	 * outer shape depending on whether a foreground color is available.
	 *
	 * @return the Shape representing the inner part of the icon
	 */
	protected abstract Shape createInside();
}
