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
import java.awt.geom.Rectangle2D;

/**
 * An icon representing a hard drive in a file view for the Jewel Look and Feel.
 * Draws a simplified hard drive chassis with LED indicators, using nested
 * rectangles to create a cut-out effect for depth.
 */
public class JewelFileViewHardDriveIcon extends JewelAbstractIcon {
	private static final String HARD_DRIVE_COLOR_KEY = "FileView.icon.hardDriveColor";
	private static final int DEFAULT_SIZE = 16;

	// Outer case dimensions
	private static final float CASE_LEFT = 2f;
	private static final float CASE_TOP = 4f;
	private static final float CASE_WIDTH = 12f;
	private static final float CASE_HEIGHT = 8f;

	// Inner case dimensions (for depth effect)
	private static final float INNER_MARGIN = 1f;
	private static final float INNER_WIDTH = 10f;
	private static final float INNER_HEIGHT = 6f;

	// LED indicators dimensions
	private static final float LED_WIDTH = 1f;
	private static final float LED_HEIGHT = 2f;
	private static final float LED_TOP = 7f;
	private static final float LED1_LEFT = 11f;
	private static final float LED2_LEFT = 9f;

	/**
	 * Creates a new hard drive icon with dimensions of 16x16 pixels.
	 * The icon color is determined by the "FileView.icon.hardDriveColor" UI property.
	 */
	public JewelFileViewHardDriveIcon() {
		super(DEFAULT_SIZE, DEFAULT_SIZE, UIManager.getColor(HARD_DRIVE_COLOR_KEY));
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		paintHardDrive(g);
	}

	/**
	 * Paints the hard drive with outer case, inner cut-out, and LED indicators.
	 * Uses WIND_EVEN_ODD rule to create the cut-out effect.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintHardDrive(Graphics2D g) {
		Path2D.Float path = new Path2D.Float(Path2D.WIND_EVEN_ODD);

		// Add outer case
		path.append(new Rectangle2D.Float(
			CASE_LEFT, CASE_TOP, CASE_WIDTH, CASE_HEIGHT), false);

		// Add inner cut-out
		path.append(new Rectangle2D.Float(
			CASE_LEFT + INNER_MARGIN,
			CASE_TOP + INNER_MARGIN,
			INNER_WIDTH,
			INNER_HEIGHT), false);

		// Add LED indicators
		path.append(new Rectangle2D.Float(
			LED1_LEFT, LED_TOP, LED_WIDTH, LED_HEIGHT), false);
		path.append(new Rectangle2D.Float(
			LED2_LEFT, LED_TOP, LED_WIDTH, LED_HEIGHT), false);

		g.fill(path);
	}
}
