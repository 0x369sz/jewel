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
 * An icon representing a directory (folder) in a file view for the
 * Jewel Look and Feel. Draws a folder with a tabbed top and a subtle
 * inner highlight effect for depth.
 */
public class JewelFileViewDirectoryIcon extends JewelAbstractIcon {
	private static final String DIRECTORY_COLOR_KEY = "FileView.icon.directoryColor";
	private static final int DEFAULT_SIZE = 16;

	// Outer folder dimensions
	private static final float OUTER_LEFT = 1f;
	private static final float OUTER_TOP = 2f;
	private static final float OUTER_TAB_X = 6f;
	private static final float OUTER_RIGHT = 15f;
	private static final float OUTER_BOTTOM = 13f;
	private static final float OUTER_TAB_HEIGHT = 2f;

	// Inner folder dimensions (for highlight)
	private static final float INNER_LEFT = 2f;
	private static final float INNER_TOP = 3f;
	private static final float INNER_TAB_X = 5.5f;
	private static final float INNER_RIGHT = 14f;
	private static final float INNER_BOTTOM = 11f;
	private static final float INNER_TAB_HEIGHT = 2f;

	// Highlight effect
	private static final float HIGHLIGHT_OPACITY = 0.1f;

	/**
	 * Creates a new directory icon with dimensions of 16x16 pixels.
	 * The icon color is determined by the "FileView.icon.directoryColor" UI property.
	 */
	public JewelFileViewDirectoryIcon() {
		super(DEFAULT_SIZE, DEFAULT_SIZE, UIManager.getColor(DIRECTORY_COLOR_KEY));
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		paintOuterFolder(g);
		paintInnerHighlight(g);
	}

	/**
	 * Paints the outer folder shape with a tab on top.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintOuterFolder(Graphics2D g) {
		Path2D.Float outerPath = new Path2D.Float();

		// Draw main folder shape
		outerPath.moveTo(OUTER_LEFT, OUTER_TOP);
		outerPath.lineTo(OUTER_TAB_X, OUTER_TOP);
		outerPath.lineTo(OUTER_TAB_X + OUTER_TAB_HEIGHT, OUTER_TOP + OUTER_TAB_HEIGHT);
		outerPath.lineTo(OUTER_RIGHT, OUTER_TOP + OUTER_TAB_HEIGHT);
		outerPath.lineTo(OUTER_RIGHT, OUTER_BOTTOM);
		outerPath.lineTo(OUTER_LEFT, OUTER_BOTTOM);
		outerPath.closePath();

		g.fill(outerPath);
	}

	/**
	 * Paints the inner highlight effect to give the folder depth.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintInnerHighlight(Graphics2D g) {
		Path2D.Float innerPath = new Path2D.Float();

		// Draw inner highlight shape
		innerPath.moveTo(INNER_LEFT, INNER_TOP);
		innerPath.lineTo(INNER_TAB_X, INNER_TOP);
		innerPath.lineTo(INNER_TAB_X + INNER_TAB_HEIGHT, INNER_TOP + INNER_TAB_HEIGHT);
		innerPath.lineTo(INNER_RIGHT, INNER_TOP + INNER_TAB_HEIGHT);
		innerPath.lineTo(INNER_RIGHT, INNER_BOTTOM);
		innerPath.lineTo(INNER_LEFT, INNER_BOTTOM);
		innerPath.closePath();

		// Apply highlight effect
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, HIGHLIGHT_OPACITY));
		g.setColor(Color.WHITE);
		g.fill(innerPath);
		g.setComposite(AlphaComposite.SrcOver);
	}
}
