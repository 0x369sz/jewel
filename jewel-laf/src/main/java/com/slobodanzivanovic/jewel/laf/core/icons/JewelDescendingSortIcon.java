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

/**
 * An icon representing a descending sort indicator for table headers in the
 * Jewel Look and Feel. Draws a downward-pointing triangle using the color
 * specified by the "Table.sortIconColor" UI property.
 */
public class JewelDescendingSortIcon extends JewelAbstractIcon {
	private static final String SORT_ICON_COLOR_KEY = "Table.sortIconColor";
	private static final int DEFAULT_WIDTH = 10;
	private static final int DEFAULT_HEIGHT = 5;

	protected final Color sortIconColor;

	/**
	 * Creates a new descending sort icon with default dimensions of 10x5 pixels.
	 */
	public JewelDescendingSortIcon() {
		super(DEFAULT_WIDTH, DEFAULT_HEIGHT, null);
		this.sortIconColor = UIManager.getColor(SORT_ICON_COLOR_KEY);
	}

	@Override
	protected void paintIcon(Component component, Graphics2D g) {
		g.setRenderingHints(new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON));

		g.setColor(sortIconColor);

		Path2D.Float path = new Path2D.Float();
		path.moveTo(1, 0.5);
		path.lineTo(5, 4.5);
		path.lineTo(9, 0.5);
		path.closePath();

		g.fill(path);
	}
}
