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

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * An icon representing an error state in JOptionPane dialogs for the Jewel Look and Feel.
 * Draws a circular icon with an exclamation mark symbol and inner highlight effect.
 */
public class JewelOptionPaneErrorIcon extends JewelOptionPaneAbstractIcon {
	private static final float INNER_HIGHLIGHT_ALPHA = 0.1f;

	// Circle dimensions
	private static final float OUTER_CIRCLE_X = 2f;
	private static final float OUTER_CIRCLE_Y = 2f;
	private static final float OUTER_CIRCLE_SIZE = 28f;
	private static final float INNER_CIRCLE_X = 3f;
	private static final float INNER_CIRCLE_Y = 3f;
	private static final float INNER_CIRCLE_SIZE = 26f;

	// Exclamation mark dimensions
	private static final float MARK_X = 14f;
	private static final float MARK_BODY_Y = 7f;
	private static final float MARK_WIDTH = 4f;
	private static final float MARK_BODY_HEIGHT = 11f;
	private static final float MARK_DOT_Y = 21f;
	private static final float MARK_DOT_HEIGHT = 4f;

	/**
	 * Creates a new error icon for JOptionPane.
	 * Uses the "OptionPane.icon.errorColor" UI property for the icon's color.
	 */
	public JewelOptionPaneErrorIcon() {
		super("OptionPane.icon.errorColor");
	}

	@Override
	protected Shape createOutside() {
		return new Ellipse2D.Float(
			OUTER_CIRCLE_X,
			OUTER_CIRCLE_Y,
			OUTER_CIRCLE_SIZE,
			OUTER_CIRCLE_SIZE
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
			MARK_DOT_HEIGHT
		), false);
		return inside;
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		super.paintIcon(c, g);

		// Add inner highlight effect
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, INNER_HIGHLIGHT_ALPHA));
		g.setColor(Color.WHITE);
		g.fill(new Ellipse2D.Float(
			INNER_CIRCLE_X,
			INNER_CIRCLE_Y,
			INNER_CIRCLE_SIZE,
			INNER_CIRCLE_SIZE
		));
		g.setComposite(AlphaComposite.SrcOver);
	}
}
