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
 * An icon representing an arrow specifically designed for menu items in the Jewel Look and Feel.
 * This class extends JewelMenuArrowIcon to provide specialized rendering for menu item arrows,
 * maintaining consistent appearance with the overall theme.
 */
public class JewelMenuItemArrowIcon extends JewelMenuArrowIcon {

	/**
	 * Creates a new menu item arrow icon.
	 * The icon's appearance will be determined by the Jewel Look and Feel theme settings.
	 */
	public JewelMenuItemArrowIcon() {
		super();
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (g instanceof Graphics2D) {
			paintIcon(c, (Graphics2D) g);
		}
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
	}
}
