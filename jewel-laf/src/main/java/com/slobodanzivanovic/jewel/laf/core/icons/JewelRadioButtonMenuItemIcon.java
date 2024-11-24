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

/**
 * An icon representing a radio button within menu items for the Jewel Look and Feel.
 * Provides circular checkmark rendering by overriding the checkmark method from
 * JewelCheckBoxMenuItemIcon.
 */
public class JewelRadioButtonMenuItemIcon extends JewelCheckBoxMenuItemIcon {
	// Checkmark circle dimensions
	private static final int CIRCLE_X = 4;
	private static final int CIRCLE_Y = 4;
	private static final int CIRCLE_SIZE = 7;

	@Override
	protected void paintCheckmark(Graphics2D g2) {
		g2.fillOval(CIRCLE_X, CIRCLE_Y, CIRCLE_SIZE, CIRCLE_SIZE);
	}
}
