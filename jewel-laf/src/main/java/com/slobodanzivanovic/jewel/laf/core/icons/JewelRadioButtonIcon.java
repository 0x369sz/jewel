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
import java.awt.geom.Ellipse2D;

/**
 * An icon representing a radio button in the Jewel Look and Feel.
 * Provides circular rendering for radio button states by overriding the
 * rectangular rendering methods from JewelCheckBoxIcon.
 */
public class JewelRadioButtonIcon extends JewelCheckBoxIcon {
	// Dimensions
	private static final float OUTER_CIRCLE_SIZE = 15f;
	private static final float INNER_CIRCLE_SIZE = 13f;
	private static final float INNER_CIRCLE_OFFSET = 1f;

	/**
	 * The diameter of the center dot when the radio button is selected.
	 * Retrieved from UI defaults with a default value of 8.
	 */
	protected final int centerDiameter = JewelUIUtils.getUIInt("RadioButton.icon.centerDiameter", 8);

	@Override
	protected void paintFocusBorder(Graphics2D g2) {
		// Draw focus ring as a circle
		int focusSize = ICON_SIZE + (focusWidth * 2);
		g2.fillOval(-focusWidth, -focusWidth, focusSize, focusSize);
	}

	@Override
	protected void paintBorder(Graphics2D g2) {
		// Draw outer circle border
		g2.fillOval(0, 0, (int) OUTER_CIRCLE_SIZE, (int) OUTER_CIRCLE_SIZE);
	}

	@Override
	protected void paintBackground(Graphics2D g2) {
		// Draw inner circle background
		g2.fillOval(
			(int) INNER_CIRCLE_OFFSET,
			(int) INNER_CIRCLE_OFFSET,
			(int) INNER_CIRCLE_SIZE,
			(int) INNER_CIRCLE_SIZE
		);
	}

	@Override
	protected void paintCheckmark(Graphics2D g2) {
		// Draw center dot when selected
		float centerOffset = (ICON_SIZE - centerDiameter) / 2f;
		g2.fill(new Ellipse2D.Float(
			centerOffset,
			centerOffset,
			centerDiameter,
			centerDiameter
		));
	}
}
