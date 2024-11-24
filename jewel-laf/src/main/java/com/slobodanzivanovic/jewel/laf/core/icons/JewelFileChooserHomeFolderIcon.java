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
 * An icon representing a home folder in a file chooser dialog for the
 * Jewel Look and Feel. Draws a house-shaped icon with a door, resembling
 * a typical home folder icon used in file systems.
 */
public class JewelFileChooserHomeFolderIcon extends JewelAbstractIcon {
	private static final String HOME_FOLDER_COLOR_KEY = "FileChooser.icon.homeFolderColor";
	private static final int DEFAULT_SIZE = 16;

	// Outer path coordinates
	private static final float ROOF_TOP_Y = 1f;
	private static final float HOUSE_CENTER_X = 8f;
	private static final float SIDE_WALL_X1 = 2f;
	private static final float SIDE_WALL_X2 = 14f;
	private static final float ROOF_TOP_X = 7f;
	private static final float BASE_Y = 14.5f;
	private static final float DOOR_TOP_Y = 10f;

	// Inner path coordinates
	private static final float INNER_ROOF_Y = 2.25f;
	private static final float INNER_WALL_X1 = 2.75f;
	private static final float INNER_WALL_X2 = 13.25f;
	private static final float INNER_BASE_Y = 13.75f;
	private static final float INNER_DOOR_Y = 9.75f;
	private static final float DOOR_CORNER_RADIUS = 9f;

	/**
	 * Creates a new home folder icon with dimensions of 16x16 pixels.
	 * The icon color is determined by the "FileChooser.icon.homeFolderColor" UI property.
	 */
	public JewelFileChooserHomeFolderIcon() {
		super(DEFAULT_SIZE, DEFAULT_SIZE, UIManager.getColor(HOME_FOLDER_COLOR_KEY));
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		g.setRenderingHints(new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON));

		Path2D.Float path = new Path2D.Float();

		// Draw outer house shape
		drawOuterHousePath(path);

		// Draw inner house details
		drawInnerHousePath(path);

		g.fill(path);
	}

	/**
	 * Draws the outer shape of the house including the roof and walls.
	 *
	 * @param path The Path2D.Float object to draw on
	 */
	private void drawOuterHousePath(Path2D.Float path) {
		path.moveTo(HOUSE_CENTER_X, ROOF_TOP_Y);
		path.lineTo(SIDE_WALL_X2, ROOF_TOP_X);
		path.lineTo(SIDE_WALL_X2, BASE_Y);
		path.lineTo(9, BASE_Y);
		path.lineTo(9, DOOR_TOP_Y);
		path.lineTo(ROOF_TOP_X, DOOR_TOP_Y);
		path.lineTo(ROOF_TOP_X, BASE_Y);
		path.lineTo(SIDE_WALL_X1, BASE_Y);
		path.lineTo(SIDE_WALL_X1, ROOF_TOP_X);
		path.closePath();
	}

	/**
	 * Draws the inner details of the house including the door and inner walls.
	 *
	 * @param path The Path2D.Float object to draw on
	 */
	private void drawInnerHousePath(Path2D.Float path) {
		path.moveTo(HOUSE_CENTER_X, INNER_ROOF_Y);
		path.lineTo(INNER_WALL_X1, ROOF_TOP_X);
		path.lineTo(INNER_WALL_X1, INNER_BASE_Y);
		path.lineTo(6.25f, INNER_BASE_Y);
		path.lineTo(6.25f, INNER_DOOR_Y);
		path.curveTo(6.25f, DOOR_CORNER_RADIUS, 6.59f, 9, 7, 9);
		path.lineTo(9, 9);
		path.curveTo(9.41f, 9, 9.75f, DOOR_CORNER_RADIUS, 9.75f, INNER_DOOR_Y);
		path.lineTo(9.75f, INNER_BASE_Y);
		path.lineTo(INNER_WALL_X2, INNER_BASE_Y);
		path.lineTo(INNER_WALL_X2, ROOF_TOP_X);
		path.closePath();
	}
}
