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

/**
 * An icon representing a computer or desktop system in a file view for the
 * Jewel Look and Feel. Draws a stylized monitor with a screen, highlight
 * reflection, and a stand base.
 */
public class JewelFileViewComputerIcon extends JewelAbstractIcon {
	private static final String COMPUTER_COLOR_KEY = "FileView.icon.computerColor";
	private static final int DEFAULT_SIZE = 16;

	// Monitor dimensions
	private static final float MONITOR_LEFT = 2f;
	private static final float MONITOR_RIGHT = 14f;
	private static final float MONITOR_TOP = 3f;
	private static final float MONITOR_BOTTOM = 11f;

	// Screen dimensions
	private static final float SCREEN_LEFT = 4f;
	private static final float SCREEN_RIGHT = 12f;
	private static final float SCREEN_TOP = 5f;
	private static final float SCREEN_BOTTOM = 9f;

	// Stand dimensions
	private static final float STAND_WIDTH = 12f;
	private static final float STAND_HEIGHT = 1f;
	private static final float BASE_WIDTH = 6f;
	private static final float STAND_Y = 12f;
	private static final float BASE_Y = 13f;
	private static final float BASE_LEFT = 5f;

	// Highlight effect
	private static final float HIGHLIGHT_OPACITY = 0.1f;
	private static final float HIGHLIGHT_HEIGHT = 1f;

	/**
	 * Creates a new computer icon with dimensions of 16x16 pixels.
	 * The icon color is determined by the "FileView.icon.computerColor" UI property.
	 */
	public JewelFileViewComputerIcon() {
		super(DEFAULT_SIZE, DEFAULT_SIZE, UIManager.getColor(COMPUTER_COLOR_KEY));
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		paintMonitor(g);
		paintScreen(g);
		paintHighlight(g);
		paintStand(g);
	}

	/**
	 * Paints the outer monitor frame.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintMonitor(Graphics2D g) {
		Path2D.Float monitorPath = new Path2D.Float();
		monitorPath.moveTo(MONITOR_LEFT, MONITOR_TOP);
		monitorPath.lineTo(MONITOR_RIGHT, MONITOR_TOP);
		monitorPath.lineTo(MONITOR_RIGHT, MONITOR_BOTTOM);
		monitorPath.lineTo(MONITOR_LEFT, MONITOR_BOTTOM);
		monitorPath.closePath();
		g.fill(monitorPath);
	}

	/**
	 * Paints the inner screen area.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintScreen(Graphics2D g) {
		Path2D.Float screenPath = new Path2D.Float();
		screenPath.moveTo(SCREEN_LEFT, SCREEN_TOP);
		screenPath.lineTo(SCREEN_RIGHT, SCREEN_TOP);
		screenPath.lineTo(SCREEN_RIGHT, SCREEN_BOTTOM);
		screenPath.lineTo(SCREEN_LEFT, SCREEN_BOTTOM);
		screenPath.closePath();
		g.fill(screenPath);
	}

	/**
	 * Paints the highlight reflection effect at the top of the screen.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintHighlight(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, HIGHLIGHT_OPACITY));
		g.setColor(Color.WHITE);
		g.fillRect((int) SCREEN_LEFT, (int) SCREEN_TOP,
			(int) (SCREEN_RIGHT - SCREEN_LEFT), (int) HIGHLIGHT_HEIGHT);
		g.setComposite(AlphaComposite.SrcOver);
		g.setColor(color);
	}

	/**
	 * Paints the monitor stand and base.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintStand(Graphics2D g) {
		// Monitor stand
		g.fillRect((int) MONITOR_LEFT, (int) STAND_Y, (int) STAND_WIDTH, (int) STAND_HEIGHT);

		// Stand base
		g.fillRect((int) BASE_LEFT, (int) BASE_Y, (int) BASE_WIDTH, (int) STAND_HEIGHT);
	}
}
