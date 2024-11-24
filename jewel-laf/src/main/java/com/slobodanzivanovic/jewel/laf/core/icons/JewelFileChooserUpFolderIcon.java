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
 * An icon representing the "up folder" (parent directory) action in a file chooser
 * dialog for the Jewel Look and Feel. Draws a folder with an upward-pointing arrow
 * overlay, indicating navigation to the parent directory.
 */
public class JewelFileChooserUpFolderIcon extends JewelAbstractIcon {
	private static final String UP_FOLDER_COLOR_KEY = "FileChooser.icon.upFolderColor";
	private static final int DEFAULT_SIZE = 16;

	// Arrow color (blue)
	private static final Color ARROW_COLOR = new Color(56, 159, 214);

	// Folder shape coordinates
	private static final float FOLDER_LEFT = 2f;
	private static final float FOLDER_TOP = 3f;
	private static final float FOLDER_TAB_X = 5.5f;
	private static final float FOLDER_TAB_HEIGHT = 2f;
	private static final float FOLDER_RIGHT = 14f;
	private static final float FOLDER_BOTTOM = 13f;

	// Arrow coordinates
	private static final float ARROW_TOP = 1f;
	private static final float ARROW_LEFT = 8f;
	private static final float ARROW_RIGHT = 14f;
	private static final float ARROW_STEM_X = 10f;
	private static final float ARROW_STEM_RIGHT = 12f;
	private static final float ARROW_BASE = 4f;
	private static final float ARROW_STEM_BOTTOM = 8f;

	private final Color folderColor;

	/**
	 * Creates a new up folder icon with dimensions of 16x16 pixels.
	 * The folder color is determined by the "FileChooser.icon.upFolderColor" UI property,
	 * while the arrow is drawn in a fixed blue color.
	 */
	public JewelFileChooserUpFolderIcon() {
		super(DEFAULT_SIZE, DEFAULT_SIZE, null);
		this.folderColor = UIManager.getColor(UP_FOLDER_COLOR_KEY);
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		g.setRenderingHints(new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON));

		paintFolder(g);
		paintUpArrow(g);
	}

	/**
	 * Paints the folder shape with a tab on top.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintFolder(Graphics2D g) {
		g.setColor(folderColor);
		Path2D.Float folderPath = new Path2D.Float();

		// Draw main folder shape
		folderPath.moveTo(FOLDER_LEFT, FOLDER_TOP);
		folderPath.lineTo(FOLDER_TAB_X, FOLDER_TOP);
		folderPath.lineTo(FOLDER_TAB_X + FOLDER_TAB_HEIGHT, FOLDER_TOP + FOLDER_TAB_HEIGHT);
		folderPath.lineTo(9, 5);
		folderPath.lineTo(9, 9);
		folderPath.lineTo(13, 9);
		folderPath.lineTo(13, 5);
		folderPath.lineTo(FOLDER_RIGHT, 5);
		folderPath.lineTo(FOLDER_RIGHT, FOLDER_BOTTOM);
		folderPath.lineTo(FOLDER_LEFT, FOLDER_BOTTOM);
		folderPath.closePath();

		g.fill(folderPath);
	}

	/**
	 * Paints the upward-pointing arrow overlay.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintUpArrow(Graphics2D g) {
		g.setColor(ARROW_COLOR);
		Path2D.Float arrowPath = new Path2D.Float();

		// Draw arrow shape
		arrowPath.moveTo(ARROW_STEM_RIGHT, ARROW_BASE);
		arrowPath.lineTo(ARROW_STEM_RIGHT, ARROW_STEM_BOTTOM);
		arrowPath.lineTo(ARROW_STEM_X, ARROW_STEM_BOTTOM);
		arrowPath.lineTo(ARROW_STEM_X, ARROW_BASE);
		arrowPath.lineTo(ARROW_LEFT, ARROW_BASE);
		arrowPath.lineTo(11, ARROW_TOP);
		arrowPath.lineTo(ARROW_RIGHT, ARROW_BASE);
		arrowPath.closePath();

		g.fill(arrowPath);
	}
}
