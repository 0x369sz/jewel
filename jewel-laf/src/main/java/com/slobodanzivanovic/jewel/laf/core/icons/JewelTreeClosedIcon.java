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
 * An icon representing a closed folder in JTree components for the Jewel Look and Feel.
 * Draws a folder shape with a tab and inner highlight effect.
 */
public class JewelTreeClosedIcon extends JewelAbstractIcon {
	// Icon dimensions
	private static final int ICON_WIDTH = 16;
	private static final int ICON_HEIGHT = 16;

	// Outer folder path coordinates
	private static final float OUTER_LEFT = 1f;
	private static final float OUTER_TOP = 2f;
	private static final float OUTER_TAB_X = 6f;
	private static final float OUTER_TAB_FOLD_X = 8f;
	private static final float OUTER_TAB_Y = 4f;
	private static final float OUTER_RIGHT = 15f;
	private static final float OUTER_BOTTOM = 13f;

	// Inner highlight path coordinates
	private static final float INNER_LEFT = 2f;
	private static final float INNER_TOP = 3f;
	private static final float INNER_TAB_X = 5.5f;
	private static final float INNER_TAB_FOLD_X = 7.5f;
	private static final float INNER_TAB_Y = 5f;
	private static final float INNER_RIGHT = 14f;
	private static final float INNER_BOTTOM = 11f;

	// Highlight effect opacity
	private static final float INNER_HIGHLIGHT_ALPHA = 0.1f;

	/**
	 * Creates a new closed folder icon for JTree.
	 * Uses the "Tree.icon.closedColor" UI property for the icon's color.
	 */
	public JewelTreeClosedIcon() {
		super(ICON_WIDTH, ICON_HEIGHT, UIManager.getColor("Tree.icon.closedColor"));
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		// Paint outer folder shape
		Path2D.Float outerPath = createOuterPath();
		g.fill(outerPath);

		// Paint inner highlight effect
		Path2D.Float innerPath = createInnerPath();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, INNER_HIGHLIGHT_ALPHA));
		g.setColor(Color.WHITE);
		g.fill(innerPath);
		g.setComposite(AlphaComposite.SrcOver);
	}

	/**
	 * Creates the outer path representing the folder shape.
	 *
	 * @return Path2D.Float representing the outer folder shape
	 */
	private Path2D.Float createOuterPath() {
		Path2D.Float path = new Path2D.Float();
		path.moveTo(OUTER_LEFT, OUTER_TOP);
		path.lineTo(OUTER_TAB_X, OUTER_TOP);
		path.lineTo(OUTER_TAB_FOLD_X, OUTER_TAB_Y);
		path.lineTo(OUTER_RIGHT, OUTER_TAB_Y);
		path.lineTo(OUTER_RIGHT, OUTER_BOTTOM);
		path.lineTo(OUTER_LEFT, OUTER_BOTTOM);
		path.closePath();
		return path;
	}

	/**
	 * Creates the inner path for the highlight effect.
	 *
	 * @return Path2D.Float representing the inner highlight shape
	 */
	private Path2D.Float createInnerPath() {
		Path2D.Float path = new Path2D.Float();
		path.moveTo(INNER_LEFT, INNER_TOP);
		path.lineTo(INNER_TAB_X, INNER_TOP);
		path.lineTo(INNER_TAB_FOLD_X, INNER_TAB_Y);
		path.lineTo(INNER_RIGHT, INNER_TAB_Y);
		path.lineTo(INNER_RIGHT, INNER_BOTTOM);
		path.lineTo(INNER_LEFT, INNER_BOTTOM);
		path.closePath();
		return path;
	}
}
