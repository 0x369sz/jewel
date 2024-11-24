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

/**
 * An icon representing a file in a file view for the Jewel Look and Feel.
 * Draws a document shape with a folded corner in the top-right, creating
 * the classic "document" appearance commonly used for file icons.
 */
public class JewelFileViewFileIcon extends JewelAbstractIcon {
	private static final String FILE_COLOR_KEY = "FileView.icon.fileColor";
	private static final int DEFAULT_SIZE = 16;

	// Main document body coordinates
	private static final int DOC_LEFT = 3;
	private static final int DOC_TOP = 1;
	private static final int DOC_RIGHT = 13;
	private static final int DOC_BOTTOM = 15;
	private static final int FOLD_X = 8;
	private static final int FOLD_Y = 6;

	// Folded corner coordinates
	private static final int CORNER_LEFT = 3;
	private static final int CORNER_TOP = 5;
	private static final int CORNER_WIDTH = 4;
	private static final int CORNER_HEIGHT = 4;

	/**
	 * Creates a new file icon with dimensions of 16x16 pixels.
	 * The icon color is determined by the "FileView.icon.fileColor" UI property.
	 */
	public JewelFileViewFileIcon() {
		super(DEFAULT_SIZE, DEFAULT_SIZE, UIManager.getColor(FILE_COLOR_KEY));
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		paintMainDocument(g);
		paintFoldedCorner(g);
	}

	/**
	 * Paints the main document body shape.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintMainDocument(Graphics2D g) {
		g.fill(JewelUIUtils.createPath(
			FOLD_X, FOLD_Y,    // Start at fold point
			FOLD_X, DOC_TOP,   // Up to top edge
			DOC_RIGHT, DOC_TOP,   // Right to top-right corner
			DOC_RIGHT, DOC_BOTTOM,  // Down right edge
			DOC_LEFT, DOC_BOTTOM,   // Left to bottom-left corner
			DOC_LEFT, FOLD_Y    // Up to fold point
		));
	}

	/**
	 * Paints the folded corner in the top-right.
	 *
	 * @param g The Graphics2D context to paint on
	 */
	private void paintFoldedCorner(Graphics2D g) {
		g.fill(JewelUIUtils.createPath(
			CORNER_LEFT, CORNER_TOP,      // Start at left point
			CORNER_LEFT + CORNER_WIDTH, CORNER_TOP,  // Right to end of fold
			CORNER_LEFT + CORNER_WIDTH, CORNER_TOP - CORNER_HEIGHT  // Up to top of fold
		));
	}
}
