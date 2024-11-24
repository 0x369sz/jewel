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
 * An icon representing an open folder in JTree components for the Jewel Look and Feel.
 * Draws a two-part folder shape with a protruding tab and inner highlight effect.
 */
public class JewelTreeOpenIcon extends JewelAbstractIcon {
	// Icon dimensions
	private static final int ICON_WIDTH = 16;
	private static final int ICON_HEIGHT = 16;

	// Upper part coordinates
	private static final float UPPER_LEFT = 1f;
	private static final float UPPER_TOP = 2f;
	private static final float UPPER_TAB_X = 6f;
	private static final float UPPER_FOLD_X = 8f;
	private static final float UPPER_FOLD_Y = 4f;
	private static final float UPPER_RIGHT = 14f;
	private static final float UPPER_BOTTOM = 6f;
	private static final float UPPER_FOLD_BOTTOM = 11f;
	private static final float UPPER_FOLD_LEFT = 3.5f;

	// Lower part coordinates
	private static final float LOWER_LEFT = 4f;
	private static final float LOWER_TOP = 7f;
	private static final float LOWER_RIGHT = 16f;
	private static final float LOWER_FOLD_RIGHT = 13f;
	private static final float LOWER_BOTTOM = 13f;
	private static final float LOWER_FOLD_LEFT = 1f;

	// Highlight coordinates
	private static final float HIGHLIGHT_LEFT = 2f;
	private static final float HIGHLIGHT_TOP = 3f;
	private static final float HIGHLIGHT_TAB = 5.5f;
	private static final float HIGHLIGHT_FOLD_X = 7.5f;
	private static final float HIGHLIGHT_FOLD_Y = 5f;
	private static final float HIGHLIGHT_RIGHT = 13f;
	private static final float HIGHLIGHT_BOTTOM = 6f;

	// Highlight effect opacity
	private static final float HIGHLIGHT_ALPHA = 0.1f;

	/**
	 * Creates a new open folder icon for JTree.
	 * Uses the "Tree.icon.openColor" UI property for the icon's color.
	 */
	public JewelTreeOpenIcon() {
		super(ICON_WIDTH, ICON_HEIGHT, UIManager.getColor("Tree.icon.openColor"));
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		// Paint upper part with tab
		g.fill(createUpperPart());

		// Paint lower part showing folder contents
		g.fill(createLowerPart());

		// Paint highlight effect
		paintHighlight(g);
	}

	/**
	 * Creates the upper part of the folder with the tab.
	 */
	private Path2D.Float createUpperPart() {
		Path2D.Float path = new Path2D.Float();
		path.moveTo(UPPER_LEFT, UPPER_TOP);
		path.lineTo(UPPER_TAB_X, UPPER_TOP);
		path.lineTo(UPPER_FOLD_X, UPPER_FOLD_Y);
		path.lineTo(UPPER_RIGHT, UPPER_FOLD_Y);
		path.lineTo(UPPER_RIGHT, UPPER_BOTTOM);
		path.lineTo(UPPER_FOLD_LEFT, UPPER_BOTTOM);
		path.lineTo(UPPER_LEFT, UPPER_FOLD_BOTTOM);
		path.closePath();
		return path;
	}

	/**
	 * Creates the lower part of the folder representing its contents.
	 */
	private Path2D.Float createLowerPart() {
		Path2D.Float path = new Path2D.Float();
		path.moveTo(LOWER_LEFT, LOWER_TOP);
		path.lineTo(LOWER_RIGHT, LOWER_TOP);
		path.lineTo(LOWER_FOLD_RIGHT, LOWER_BOTTOM);
		path.lineTo(LOWER_FOLD_LEFT, LOWER_BOTTOM);
		path.closePath();
		return path;
	}

	/**
	 * Paints the highlight effect on the upper part of the folder.
	 */
	private void paintHighlight(Graphics2D g) {
		Path2D.Float path = new Path2D.Float();
		path.moveTo(HIGHLIGHT_LEFT, HIGHLIGHT_TOP);
		path.lineTo(HIGHLIGHT_TAB, HIGHLIGHT_TOP);
		path.lineTo(HIGHLIGHT_FOLD_X, HIGHLIGHT_FOLD_Y);
		path.lineTo(HIGHLIGHT_RIGHT, HIGHLIGHT_FOLD_Y);
		path.lineTo(HIGHLIGHT_RIGHT, HIGHLIGHT_BOTTOM);
		path.lineTo(HIGHLIGHT_LEFT, HIGHLIGHT_BOTTOM);
		path.closePath();

		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, HIGHLIGHT_ALPHA));
		g.setColor(Color.WHITE);
		g.fill(path);
		g.setComposite(AlphaComposite.SrcOver);
	}
}
