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
 * An icon representing a question state in JOptionPane dialogs for the Jewel Look and Feel.
 * Draws a circular icon with a question mark symbol and inner highlight effect.
 */
public class JewelOptionPaneQuestionIcon extends JewelOptionPaneAbstractIcon {
	private static final float INNER_HIGHLIGHT_ALPHA = 0.1f;

	// Circle dimensions
	private static final float OUTER_CIRCLE_X = 2f;
	private static final float OUTER_CIRCLE_Y = 2f;
	private static final float OUTER_CIRCLE_SIZE = 28f;
	private static final float INNER_CIRCLE_X = 3f;
	private static final float INNER_CIRCLE_Y = 3f;
	private static final float INNER_CIRCLE_SIZE = 26f;

	// Question mark dimensions
	private static final float MARK_START_X = 14f;
	private static final float MARK_START_Y = 20f;
	private static final float MARK_WIDTH = 4f;
	private static final float MARK_END_X = 18f;

	// Question mark curve control points
	private static final float CURVE_OUTER_X = 23f;
	private static final float CURVE_OUTER_Y = 12f;
	private static final float CURVE_INNER_X = 19f;
	private static final float CURVE_TOP_Y = 6f;
	private static final float CURVE_BOTTOM_Y = 9f;
	private static final float CURVE_MIDDLE_Y = 15f;

	// Question mark dot
	private static final float DOT_Y = 22f;
	private static final float DOT_SIZE = 4f;

	/**
	 * Creates a new question icon for JOptionPane.
	 * Uses the "OptionPane.icon.questionColor" UI property for the icon's color.
	 */
	public JewelOptionPaneQuestionIcon() {
		super("OptionPane.icon.questionColor");
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
		// Create the question mark curve
		Path2D questionCurve = new Path2D.Float();
		questionCurve.moveTo(MARK_START_X, MARK_START_Y);
		questionCurve.lineTo(MARK_END_X, MARK_START_Y);
		questionCurve.curveTo(MARK_END_X, CURVE_MIDDLE_Y, CURVE_OUTER_X, CURVE_MIDDLE_Y, CURVE_OUTER_X, CURVE_OUTER_Y);
		questionCurve.curveTo(CURVE_OUTER_X, CURVE_BOTTOM_Y, 20, CURVE_TOP_Y, 16, CURVE_TOP_Y);
		questionCurve.curveTo(12, CURVE_TOP_Y, 9, CURVE_BOTTOM_Y, 9, CURVE_OUTER_Y);
		questionCurve.lineTo(13, CURVE_OUTER_Y);
		questionCurve.curveTo(13, 10, 14, CURVE_BOTTOM_Y, 16, CURVE_BOTTOM_Y);
		questionCurve.curveTo(18, CURVE_BOTTOM_Y, CURVE_INNER_X, 10, CURVE_INNER_X, CURVE_OUTER_Y);
		questionCurve.curveTo(CURVE_INNER_X, CURVE_MIDDLE_Y, MARK_START_X, CURVE_MIDDLE_Y, MARK_START_X, MARK_START_Y);
		questionCurve.closePath();

		// Combine curve and dot into final shape
		Path2D inside = new Path2D.Float(Path2D.WIND_EVEN_ODD);
		inside.append(questionCurve, false);
		inside.append(new Rectangle2D.Float(
			MARK_START_X,
			DOT_Y,
			MARK_WIDTH,
			DOT_SIZE
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
