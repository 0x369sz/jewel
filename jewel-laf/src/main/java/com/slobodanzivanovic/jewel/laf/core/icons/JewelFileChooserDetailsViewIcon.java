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

/**
 * An icon representing the details view mode in a file chooser dialog for the
 * Jewel Look and Feel. Draws three rows of dots and lines to represent a
 * detailed list view layout.
 */
public class JewelFileChooserDetailsViewIcon extends JewelAbstractIcon {
	private static final String DETAILS_VIEW_COLOR_KEY = "FileChooser.icon.detailsViewColor";
	private static final int DEFAULT_SIZE = 16;
	private static final int DOT_SIZE = 2;
	private static final int LINE_WIDTH = 9;
	private static final int SPACING = 5;
	private static final int INITIAL_OFFSET = 2;
	private static final int ROW_HEIGHT = 5;
	private static final int CORNER_RADIUS = 1;

	/**
	 * Creates a new details view icon with dimensions of 16x16 pixels.
	 * The icon color is determined by the "FileChooser.icon.detailsViewColor" UI property.
	 */
	public JewelFileChooserDetailsViewIcon() {
		super(DEFAULT_SIZE, DEFAULT_SIZE, UIManager.getColor(DETAILS_VIEW_COLOR_KEY));
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		g.setRenderingHints(new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON));

		// Draw three rows of dot-and-line combinations
		for (int y = INITIAL_OFFSET; y <= DEFAULT_SIZE - INITIAL_OFFSET; y += ROW_HEIGHT) {
			// Draw dot
			g.fillRoundRect(INITIAL_OFFSET, y, DOT_SIZE, DOT_SIZE, CORNER_RADIUS, CORNER_RADIUS);
			// Draw line
			g.fillRoundRect(SPACING, y, LINE_WIDTH, DOT_SIZE, CORNER_RADIUS, CORNER_RADIUS);
		}
	}
}
