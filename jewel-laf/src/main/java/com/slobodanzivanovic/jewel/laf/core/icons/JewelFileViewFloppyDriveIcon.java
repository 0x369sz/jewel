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
 * An icon representing a floppy disk drive in a file view for the
 * Jewel Look and Feel. Draws a classic 3.5" floppy disk shape with
 * a label area, shutter window, and write-protect tab.
 */
public class JewelFileViewFloppyDriveIcon extends JewelAbstractIcon {
	private static final String FLOPPY_DRIVE_COLOR_KEY = "FileView.icon.floppyDriveColor";
	private static final int DEFAULT_SIZE = 16;

	// Main floppy disk body coordinates
	private static final int BODY_LEFT = 2;
	private static final int BODY_TOP = 2;
	private static final int BODY_RIGHT = 14;
	private static final int BODY_BOTTOM = 14;

	// Shutter window coordinates
	private static final int SHUTTER_LEFT = 4;
	private static final int SHUTTER_TOP = 4;
	private static final int SHUTTER_RIGHT = 12;
	private static final int SHUTTER_BOTTOM = 8;

	// Write-protect tab coordinates
	private static final int TAB_LEFT = 5;
	private static final int TAB_RIGHT = 11;
	private static final int TAB_TOP = 11;

	// Label area coordinates
	private static final int LABEL_LEFT = 6;
	private static final int LABEL_TOP = 12;
	private static final int LABEL_WIDTH = 4;
	private static final int LABEL_HEIGHT = 2;

	/**
	 * Creates a new floppy drive icon with dimensions of 16x16 pixels.
	 * The icon color is determined by the "FileView.icon.floppyDriveColor" UI property.
	 */
	public JewelFileViewFloppyDriveIcon() {
		super(DEFAULT_SIZE, DEFAULT_SIZE, UIManager.getColor(FLOPPY_DRIVE_COLOR_KEY));
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		paintFloppyDisk(g);
		paintLabel(g);
	}

	/**
	 * Paints the main floppy disk body with shutter window and write-protect tab.
	 * Uses WIND_EVEN_ODD rule to create the cutout effect.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintFloppyDisk(Graphics2D g) {
		Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);

		// Add main disk body with write-protect tab
		path.append(JewelUIUtils.createPath(
			TAB_RIGHT, BODY_BOTTOM,    // Start at right of tab
			TAB_RIGHT, TAB_TOP,        // Up to top of tab
			TAB_LEFT, TAB_TOP,         // Left to start of tab
			TAB_LEFT, BODY_BOTTOM,     // Down to bottom
			BODY_LEFT, BODY_BOTTOM,    // Left to edge
			BODY_LEFT, BODY_TOP,       // Up left side
			BODY_RIGHT, BODY_TOP,      // Right to edge
			BODY_RIGHT, BODY_BOTTOM,   // Down right side
			TAB_RIGHT, BODY_BOTTOM     // Back to start
		), false);

		// Add shutter window cutout
		path.append(JewelUIUtils.createPath(
			SHUTTER_LEFT, SHUTTER_TOP,      // Start at top-left
			SHUTTER_LEFT, SHUTTER_BOTTOM,   // Down left side
			SHUTTER_RIGHT, SHUTTER_BOTTOM,  // Right to edge
			SHUTTER_RIGHT, SHUTTER_TOP,     // Up right side
			SHUTTER_LEFT, SHUTTER_TOP       // Back to start
		), false);

		g.fill(path);
	}

	/**
	 * Paints the label area on the floppy disk.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintLabel(Graphics2D g) {
		g.fillRect(LABEL_LEFT, LABEL_TOP, LABEL_WIDTH, LABEL_HEIGHT);
	}
}
