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
 * An icon representing a leaf (document) node in JTree components for the Jewel Look and Feel.
 * Draws a document shape with a folded corner and highlight effect.
 */
public class JewelTreeLeafIcon extends JewelAbstractIcon {
	// Icon dimensions
	private static final int ICON_WIDTH = 16;
	private static final int ICON_HEIGHT = 16;

	// Document shape coordinates
	private static final float DOC_LEFT = 3f;
	private static final float DOC_TOP = 1f;
	private static final float DOC_TAB_X = 8f;
	private static final float DOC_FOLD_X = 12f;
	private static final float DOC_FOLD_Y = 5f;
	private static final float DOC_BOTTOM = 15f;

	// Highlight effect coordinates
	private static final float HIGHLIGHT_LEFT = 4f;
	private static final float HIGHLIGHT_TOP = 2f;
	private static final float HIGHLIGHT_RIGHT = 7f;
	private static final float HIGHLIGHT_BOTTOM = 3f;

	// Highlight effect opacity
	private static final float HIGHLIGHT_ALPHA = 0.1f;

	/**
	 * Creates a new leaf icon for JTree.
	 * Uses the "Tree.icon.leafColor" UI property for the icon's color.
	 */
	public JewelTreeLeafIcon() {
		super(ICON_WIDTH, ICON_HEIGHT, UIManager.getColor("Tree.icon.leafColor"));
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g) {
		// Paint main document body
		g.fill(createDocumentBody());

		// Paint folded corner
		g.fill(createFoldedCorner());

		// Paint highlight effect
		paintHighlight(g);
	}

	/**
	 * Creates the main document body shape.
	 */
	private Path2D.Float createDocumentBody() {
		Path2D.Float path = new Path2D.Float();
		path.moveTo(DOC_LEFT, DOC_TOP);
		path.lineTo(DOC_TAB_X, DOC_TOP);
		path.lineTo(DOC_FOLD_X, DOC_FOLD_Y);
		path.lineTo(DOC_FOLD_X, DOC_BOTTOM);
		path.lineTo(DOC_LEFT, DOC_BOTTOM);
		path.closePath();
		return path;
	}

	/**
	 * Creates the folded corner shape.
	 */
	private Path2D.Float createFoldedCorner() {
		Path2D.Float path = new Path2D.Float();
		path.moveTo(DOC_TAB_X, DOC_TOP);
		path.lineTo(DOC_TAB_X, DOC_FOLD_Y);
		path.lineTo(DOC_FOLD_X, DOC_FOLD_Y);
		return path;
	}

	/**
	 * Paints the highlight effect on the document.
	 */
	private void paintHighlight(Graphics2D g) {
		Path2D.Float path = new Path2D.Float();
		path.moveTo(HIGHLIGHT_LEFT, HIGHLIGHT_TOP);
		path.lineTo(HIGHLIGHT_RIGHT, HIGHLIGHT_TOP);
		path.lineTo(HIGHLIGHT_RIGHT, HIGHLIGHT_BOTTOM);
		path.lineTo(HIGHLIGHT_LEFT, HIGHLIGHT_BOTTOM);
		path.closePath();

		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, HIGHLIGHT_ALPHA));
		g.setColor(Color.WHITE);
		g.fill(path);
		g.setComposite(AlphaComposite.SrcOver);
	}
}
