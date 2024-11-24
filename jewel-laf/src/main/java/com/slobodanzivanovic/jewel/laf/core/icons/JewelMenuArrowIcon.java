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
import java.awt.geom.Path2D;

/**
 * An icon representing a menu arrow in the Jewel Look and Feel.
 * Supports two styles: chevron (>) and triangle (►), with states for
 * RTL layout, selection, and disabled states. The arrow indicates
 * the presence of a submenu.
 */
public class JewelMenuArrowIcon extends JewelAbstractIcon {
	// UI configuration
	private static final String ARROW_TYPE_KEY = "Component.arrowType";
	private static final String CHEVRON_STYLE = "chevron";
	private static final float STROKE_WIDTH = 1f;

	// Icon dimensions
	private static final int DEFAULT_WIDTH = 6;
	private static final int DEFAULT_HEIGHT = 10;

	// Arrow coordinates
	private static final float CHEVRON_START_X = 1f;
	private static final float CHEVRON_START_Y = 1f;
	private static final float CHEVRON_MIDDLE_X = 5f;
	private static final float CHEVRON_MIDDLE_Y = 5f;
	private static final float CHEVRON_END_Y = 9f;

	private static final float TRIANGLE_START_X = 0f;
	private static final float TRIANGLE_START_Y = 0.5f;
	private static final float TRIANGLE_END_Y = 9.5f;
	private static final float TRIANGLE_POINT_X = 5f;
	private static final float TRIANGLE_POINT_Y = 5f;

	// Rotation angle for RTL layout
	private static final double RTL_ROTATION_ANGLE = Math.toRadians(180);

	// UI properties
	protected final boolean chevron;
	protected final Color arrowColor;
	protected final Color disabledArrowColor;
	protected final Color selectionForeground;

	/**
	 * Creates a new menu arrow icon with default dimensions of 6x10 pixels.
	 * The arrow style and colors are determined by UI properties.
	 */
	public JewelMenuArrowIcon() {
		super(DEFAULT_WIDTH, DEFAULT_HEIGHT, null);
		this.chevron = CHEVRON_STYLE.equals(UIManager.getString(ARROW_TYPE_KEY));
		this.arrowColor = UIManager.getColor("Menu.icon.arrowColor");
		this.disabledArrowColor = UIManager.getColor("Menu.icon.disabledArrowColor");
		this.selectionForeground = UIManager.getColor("Menu.selectionForeground");
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		setupGraphics(c, g);
		paintArrow(c, g);
	}

	/**
	 * Sets up the graphics context, including any necessary rotation for RTL layout.
	 *
	 * @param c The component to paint on
	 * @param g The graphics context
	 */
	private void setupGraphics(Component c, Graphics2D g) {
		if (!c.getComponentOrientation().isLeftToRight()) {
			g.rotate(RTL_ROTATION_ANGLE, width / 2.0, height / 2.0);
		}
		g.setColor(getArrowColor(c));
	}

	/**
	 * Paints the arrow in either chevron or triangle style.
	 *
	 * @param c The component to paint on
	 * @param g The graphics context
	 */
	private void paintArrow(Component c, Graphics2D g) {
		if (chevron) {
			paintChevron(g);
		} else {
			paintTriangle(g);
		}
	}

	/**
	 * Paints a chevron-style arrow (>).
	 *
	 * @param g The graphics context
	 */
	private void paintChevron(Graphics2D g) {
		Path2D path = JewelUIUtils.createPath(false,
			CHEVRON_START_X, CHEVRON_START_Y,
			CHEVRON_MIDDLE_X, CHEVRON_MIDDLE_Y,
			CHEVRON_START_X, CHEVRON_END_Y);
		g.setStroke(new BasicStroke(STROKE_WIDTH));
		g.draw(path);
	}

	/**
	 * Paints a solid triangle-style arrow (►).
	 *
	 * @param g The graphics context
	 */
	private void paintTriangle(Graphics2D g) {
		g.fill(JewelUIUtils.createPath(
			TRIANGLE_START_X, TRIANGLE_START_Y,
			TRIANGLE_START_X, TRIANGLE_END_Y,
			TRIANGLE_POINT_X, TRIANGLE_POINT_Y));
	}

	/**
	 * Determines the appropriate arrow color based on the component's state.
	 *
	 * @param c The component
	 * @return The color to use for the arrow
	 */
	private Color getArrowColor(Component c) {
		if (c instanceof JMenu && ((JMenu) c).isSelected()) {
			return selectionForeground;
		}
		return c.isEnabled() ? arrowColor : disabledArrowColor;
	}
}
