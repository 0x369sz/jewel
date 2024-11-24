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
import java.awt.geom.RoundRectangle2D;

/**
 * An icon representing the list view mode in a file chooser dialog for the
 * Jewel Look and Feel. Draws three rows, each containing an icon placeholder
 * and a label placeholder, representing a typical list view layout.
 */
public class JewelFileChooserListViewIcon extends JewelAbstractIcon {
	private static final String LIST_VIEW_COLOR_KEY = "FileChooser.icon.listViewColor";
	private static final int DEFAULT_SIZE = 16;

	// Icon dimensions and positions
	private static final float ICON_SIZE = 3f;
	private static final float LABEL_WIDTH = 7f;
	private static final float CORNER_RADIUS = 0.5f;
	private static final float ICON_X = 2f;
	private static final float LABEL_X = 7f;
	private static final float ROW_HEIGHT = 4.5f;
	private static final float INITIAL_Y = 2f;

	private static final int ROWS = 3;

	/**
	 * Creates a new list view icon with dimensions of 16x16 pixels.
	 * The icon color is determined by the "FileChooser.icon.listViewColor" UI property.
	 */
	public JewelFileChooserListViewIcon() {
		super(DEFAULT_SIZE, DEFAULT_SIZE, UIManager.getColor(LIST_VIEW_COLOR_KEY));
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		g.setRenderingHints(new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON));

		paintListViewRows(g);
	}

	/**
	 * Paints all rows of the list view, where each row consists of an icon
	 * placeholder and a label placeholder.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintListViewRows(Graphics2D g) {
		for (int i = 0; i < ROWS; i++) {
			float yPosition = INITIAL_Y + (i * ROW_HEIGHT);
			paintRow(g, yPosition);
		}
	}

	/**
	 * Paints a single row of the list view, consisting of an icon placeholder
	 * and a label placeholder.
	 *
	 * @param g The Graphics2D context to paint on
	 * @param y The vertical position to paint the row
	 */
	private void paintRow(Graphics2D g, float y) {
		// Paint icon placeholder
		g.fill(new RoundRectangle2D.Float(
			ICON_X, y, ICON_SIZE, ICON_SIZE,
			CORNER_RADIUS, CORNER_RADIUS));

		// Paint label placeholder
		g.fill(new RoundRectangle2D.Float(
			LABEL_X, y, LABEL_WIDTH, ICON_SIZE,
			CORNER_RADIUS, CORNER_RADIUS));
	}
}
