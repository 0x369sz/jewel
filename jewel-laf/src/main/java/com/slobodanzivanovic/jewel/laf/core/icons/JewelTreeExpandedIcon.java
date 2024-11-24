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

/**
 * An icon representing an expanded state in JTree components for the Jewel Look and Feel.
 * Extends JewelTreeCollapsedIcon and rotates it 90 degrees to show the expanded state,
 * with proper RTL handling.
 */
public class JewelTreeExpandedIcon extends JewelTreeCollapsedIcon {
	private static final double ROTATION_ANGLE = 90.0;
	private static final double RTL_ROTATION = 180.0;

	/**
	 * Creates a new expanded icon for JTree.
	 * Uses the "Tree.icon.expandedColor" UI property for the icon's color.
	 */
	public JewelTreeExpandedIcon() {
		super(UIManager.getColor("Tree.icon.expandedColor"));
	}

	@Override
	void rotate(Component c, Graphics2D g) {
		// Store current transform - not used but kept for consistency with parent class
		g.getTransform();

		// Rotate 90 degrees for expanded state
		g.rotate(Math.toRadians(ROTATION_ANGLE), width / 2.0, height / 2.0);

		// Additional 180-degree rotation for RTL
		if (!c.getComponentOrientation().isLeftToRight()) {
			g.rotate(Math.toRadians(RTL_ROTATION), width / 2.0, height / 2.0);
		}
	}
}
