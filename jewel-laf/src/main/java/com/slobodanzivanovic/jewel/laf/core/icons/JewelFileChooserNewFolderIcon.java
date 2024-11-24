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
 * An icon representing a new folder action in a file chooser dialog for the
 * Jewel Look and Feel. Draws a folder with a plus symbol overlay, indicating
 * the action to create a new folder.
 */
public class JewelFileChooserNewFolderIcon extends JewelAbstractIcon {
	private static final String NEW_FOLDER_COLOR_KEY = "FileChooser.icon.newFolderColor";
	private static final int DEFAULT_SIZE = 16;

	// Plus symbol color (green)
	private static final Color PLUS_COLOR = new Color(89, 168, 105);

	// Folder shape coordinates
	private static final float FOLDER_LEFT = 2f;
	private static final float FOLDER_TOP = 3f;
	private static final float FOLDER_TAB_X = 5.5f;
	private static final float FOLDER_TAB_HEIGHT = 2f;
	private static final float FOLDER_RIGHT = 14f;
	private static final float FOLDER_BOTTOM = 13f;

	// Plus symbol coordinates
	private static final float PLUS_LEFT = 10f;
	private static final float PLUS_RIGHT = 16f;
	private static final float PLUS_TOP = 9f;
	private static final float PLUS_BOTTOM = 15f;
	private static final float PLUS_CENTER_X = 12f;
	private static final float PLUS_CENTER_Y = 11f;

	private final Color folderColor;

	/**
	 * Creates a new folder icon with dimensions of 16x16 pixels.
	 * The folder color is determined by the "FileChooser.icon.newFolderColor" UI property,
	 * while the plus symbol is drawn in a fixed green color.
	 */
	public JewelFileChooserNewFolderIcon() {
		super(DEFAULT_SIZE, DEFAULT_SIZE, null);
		this.folderColor = UIManager.getColor(NEW_FOLDER_COLOR_KEY);
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		g.setRenderingHints(new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON));

		paintFolder(g);
		paintPlusSymbol(g);
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
		folderPath.lineTo(FOLDER_RIGHT, FOLDER_TOP + FOLDER_TAB_HEIGHT);
		folderPath.lineTo(FOLDER_RIGHT, 8);
		folderPath.lineTo(11, 8);
		folderPath.lineTo(11, 10);
		folderPath.lineTo(9, 10);
		folderPath.lineTo(9, FOLDER_BOTTOM);
		folderPath.lineTo(FOLDER_LEFT, FOLDER_BOTTOM);
		folderPath.closePath();

		g.fill(folderPath);
	}

	/**
	 * Paints the plus symbol overlay.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintPlusSymbol(Graphics2D g) {
		g.setColor(PLUS_COLOR);
		Path2D.Float plusPath = new Path2D.Float();

		// Draw plus symbol
		plusPath.moveTo(FOLDER_RIGHT, PLUS_CENTER_Y);
		plusPath.lineTo(PLUS_RIGHT, PLUS_CENTER_Y);
		plusPath.lineTo(PLUS_RIGHT, FOLDER_BOTTOM);
		plusPath.lineTo(FOLDER_RIGHT, FOLDER_BOTTOM);
		plusPath.lineTo(FOLDER_RIGHT, PLUS_BOTTOM);
		plusPath.lineTo(PLUS_CENTER_X, PLUS_BOTTOM);
		plusPath.lineTo(PLUS_CENTER_X, FOLDER_BOTTOM);
		plusPath.lineTo(PLUS_LEFT, FOLDER_BOTTOM);
		plusPath.lineTo(PLUS_LEFT, PLUS_CENTER_Y);
		plusPath.lineTo(PLUS_CENTER_X, PLUS_CENTER_Y);
		plusPath.lineTo(PLUS_CENTER_X, PLUS_TOP);
		plusPath.lineTo(FOLDER_RIGHT, PLUS_TOP);
		plusPath.closePath();

		g.fill(plusPath);
	}
}
