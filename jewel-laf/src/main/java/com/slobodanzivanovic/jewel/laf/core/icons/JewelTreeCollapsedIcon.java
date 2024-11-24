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

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * An icon representing a collapsed state in JTree components for the Jewel Look and Feel.
 * Supports both chevron and triangle styles based on UI settings, with proper RTL handling.
 */
public class JewelTreeCollapsedIcon extends JewelAbstractIcon {
	// Icon dimensions
	private static final int ICON_SIZE = 11;

	// Chevron coordinates
	private static final float CHEVRON_LEFT = 3f;
	private static final float CHEVRON_TOP = 1f;
	private static final float CHEVRON_MID_LEFT = 6f;
	private static final float CHEVRON_MID_Y = 5.5f;
	private static final float CHEVRON_BOTTOM = 10f;
	private static final float CHEVRON_RIGHT = 9f;
	private static final float CHEVRON_CURVE = 2.5f;
	private static final float CHEVRON_CURVE_BOTTOM = 8.5f;
	private static final float CHEVRON_INDENT = 4.5f;

	// Triangle coordinates
	private static final float TRIANGLE_LEFT = 2f;
	private static final float TRIANGLE_TOP = 1f;
	private static final float TRIANGLE_BOTTOM = 10f;
	private static final float TRIANGLE_RIGHT = 10f;
	private static final float TRIANGLE_MID_Y = 5.5f;

	private final boolean chevron;

	/**
	 * Creates a new collapsed icon for JTree with default color.
	 * Uses the "Tree.icon.collapsedColor" UI property for the icon's color.
	 */
	public JewelTreeCollapsedIcon() {
		this(UIManager.getColor("Tree.icon.collapsedColor"));
	}

	/**
	 * Creates a new collapsed icon for JTree with specified color.
	 *
	 * @param color The color to use for the icon
	 */
	JewelTreeCollapsedIcon(Color color) {
		super(ICON_SIZE, ICON_SIZE, color);
		chevron = "chevron".equals(UIManager.getString("Component.arrowType"));
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		AffineTransform originalTransform = g.getTransform();

		// Enable anti-aliasing for smooth rendering
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		// Handle RTL orientation
		rotate(c, g);

		// Draw either chevron or triangle based on UI settings
		if (chevron) {
			g.fill(JewelUIUtils.createPath(
				CHEVRON_LEFT, CHEVRON_TOP,
				CHEVRON_LEFT, CHEVRON_CURVE,
				CHEVRON_MID_LEFT, CHEVRON_MID_Y,
				CHEVRON_LEFT, CHEVRON_CURVE_BOTTOM,
				CHEVRON_LEFT, CHEVRON_BOTTOM,
				CHEVRON_INDENT, CHEVRON_BOTTOM,
				CHEVRON_RIGHT, CHEVRON_MID_Y,
				CHEVRON_INDENT, CHEVRON_TOP
			));
		} else {
			g.fill(JewelUIUtils.createPath(
				TRIANGLE_LEFT, TRIANGLE_TOP,
				TRIANGLE_LEFT, TRIANGLE_BOTTOM,
				TRIANGLE_RIGHT, TRIANGLE_MID_Y
			));
		}

		g.setTransform(originalTransform);
	}

	/**
	 * Rotates the graphics context for RTL orientation.
	 *
	 * @param c The component being painted
	 * @param g The graphics context to transform
	 */
	void rotate(Component c, Graphics2D g) {
		if (!c.getComponentOrientation().isLeftToRight()) {
			g.rotate(Math.toRadians(180), width / 2.0, height / 2.0);
		}
	}
}
