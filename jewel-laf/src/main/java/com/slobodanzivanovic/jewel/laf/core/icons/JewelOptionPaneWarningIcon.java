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

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * An icon representing a warning state in JOptionPane dialogs for the Jewel Look and Feel.
 * Draws a triangular icon with an exclamation mark and inner highlight effect.
 */
public class JewelOptionPaneWarningIcon extends JewelOptionPaneAbstractIcon {
	private static final float INNER_HIGHLIGHT_ALPHA = 0.1f;

	// Triangle corners
	private static final float TRIANGLE_TOP_X = 16f;
	private static final float TRIANGLE_TOP_Y = 2f;
	private static final float TRIANGLE_RIGHT_X = 31f;
	private static final float TRIANGLE_BOTTOM_Y = 28f;
	private static final float TRIANGLE_LEFT_X = 1f;

	// Inner highlight triangle corners
	private static final float INNER_TOP_X = 16f;
	private static final float INNER_TOP_Y = 4f;
	private static final float INNER_RIGHT_X = 29f;
	private static final float INNER_BOTTOM_Y = 27f;
	private static final float INNER_LEFT_X = 3f;

	// Exclamation mark dimensions
	private static final float MARK_X = 14f;
	private static final float MARK_WIDTH = 4f;
	private static final float MARK_BODY_Y = 10f;
	private static final float MARK_BODY_HEIGHT = 8f;
	private static final float MARK_DOT_Y = 21f;
	private static final float MARK_DOT_SIZE = 4f;

	/**
	 * Creates a new warning icon for JOptionPane.
	 * Uses the "OptionPane.icon.warningColor" UI property for the icon's color.
	 */
	public JewelOptionPaneWarningIcon() {
		super("OptionPane.icon.warningColor");
	}

	@Override
	protected Shape createOutside() {
		return JewelUIUtils.createPath(
			TRIANGLE_TOP_X, TRIANGLE_TOP_Y,
			TRIANGLE_RIGHT_X, TRIANGLE_BOTTOM_Y,
			TRIANGLE_LEFT_X, TRIANGLE_BOTTOM_Y
		);
	}

	@Override
	protected Shape createInside() {
		Path2D inside = new Path2D.Float(Path2D.WIND_EVEN_ODD);
		// Add exclamation mark body
		inside.append(new Rectangle2D.Float(
			MARK_X,
			MARK_BODY_Y,
			MARK_WIDTH,
			MARK_BODY_HEIGHT
		), false);
		// Add exclamation mark dot
		inside.append(new Rectangle2D.Float(
			MARK_X,
			MARK_DOT_Y,
			MARK_WIDTH,
			MARK_DOT_SIZE
		), false);
		return inside;
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		super.paintIcon(c, g);

		// Add inner highlight effect
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, INNER_HIGHLIGHT_ALPHA));
		g.setColor(Color.WHITE);
		g.fill(JewelUIUtils.createPath(
			INNER_TOP_X, INNER_TOP_Y,
			INNER_RIGHT_X, INNER_BOTTOM_Y,
			INNER_LEFT_X, INNER_BOTTOM_Y
		));
		g.setComposite(AlphaComposite.SrcOver);
	}
}
