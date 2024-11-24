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

package com.slobodanzivanovic.jewel.laf.core.ui;

import com.slobodanzivanovic.jewel.laf.core.util.JewelUIUtils;
import com.slobodanzivanovic.jewel.laf.core.util.UIScale;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Objects;

/**
 * UI delegate for JTree components in the Jewel Look and Feel.
 * Provides custom selection painting with focus-aware colors and UI scaling support.
 */
public class JewelTreeUI extends BasicTreeUI {
	private static final String TREE_PREFIX = "Tree.";
	private static final String ROW_HEIGHT_KEY = TREE_PREFIX + "rowHeight";
	private static final String SELECTION_BACKGROUND_KEY = TREE_PREFIX + "selectionBackground";
	private static final String SELECTION_FOREGROUND_KEY = TREE_PREFIX + "selectionForeground";
	private static final String SELECTION_INACTIVE_BACKGROUND_KEY = TREE_PREFIX + "selectionInactiveBackground";
	private static final String SELECTION_INACTIVE_FOREGROUND_KEY = TREE_PREFIX + "selectionInactiveForeground";
	private static final String BORDER_KEY = TREE_PREFIX + "border";
	private static final int DEFAULT_ROW_HEIGHT = 16;

	protected Color selectionBackground;
	protected Color selectionForeground;
	protected Color selectionInactiveBackground;
	protected Color selectionInactiveForeground;

	/**
	 * Creates a new UI delegate for JTree.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelTreeUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		LookAndFeel.installBorder(tree, BORDER_KEY);

		// Install selection colors
		selectionBackground = UIManager.getColor(SELECTION_BACKGROUND_KEY);
		selectionForeground = UIManager.getColor(SELECTION_FOREGROUND_KEY);
		selectionInactiveBackground = UIManager.getColor(SELECTION_INACTIVE_BACKGROUND_KEY);
		selectionInactiveForeground = UIManager.getColor(SELECTION_INACTIVE_FOREGROUND_KEY);

		// Configure row height and indentation with UI scaling
		int rowHeight = JewelUIUtils.getUIInt(ROW_HEIGHT_KEY, DEFAULT_ROW_HEIGHT);
		if (rowHeight > 0) {
			LookAndFeel.installProperty(tree, "rowHeight", UIScale.scale(rowHeight));
		}

		setLeftChildIndent(UIScale.scale(getLeftChildIndent()));
		setRightChildIndent(UIScale.scale(getRightChildIndent()));
	}

	@Override
	protected void uninstallDefaults() {
		super.uninstallDefaults();
		LookAndFeel.uninstallBorder(tree);

		selectionBackground = null;
		selectionForeground = null;
		selectionInactiveBackground = null;
		selectionInactiveForeground = null;
	}

	@Override
	protected void paintRow(Graphics graphics, Rectangle clipBounds, Insets insets,
							Rectangle bounds, TreePath path, int row, boolean isExpanded,
							boolean hasBeenExpanded, boolean isLeaf) {
		Objects.requireNonNull(graphics, "Graphics cannot be null");
		Objects.requireNonNull(path, "TreePath cannot be null");
		Objects.requireNonNull(bounds, "Bounds cannot be null");

		// Skip painting if currently editing this row
		if (editingComponent != null && editingRow == row) {
			return;
		}

		boolean hasFocus = tree.hasFocus();
		boolean cellHasFocus = hasFocus && (row == getLeadSelectionRow());
		boolean isSelected = tree.isRowSelected(row);

		Component rendererComponent = currentCellRenderer.getTreeCellRendererComponent(
			tree, path.getLastPathComponent(), isSelected, isExpanded, isLeaf, row, cellHasFocus);

		// Handle selection colors for unfocused state
		Color oldBackgroundSelectionColor = null;
		if (isSelected && !hasFocus) {
			oldBackgroundSelectionColor = updateSelectionColors(rendererComponent);
		}

		// Paint the component
		rendererPane.paintComponent(
			graphics, rendererComponent, tree,
			bounds.x, bounds.y, bounds.width, bounds.height, true);

		// Restore original selection color if modified
		if (oldBackgroundSelectionColor != null && rendererComponent instanceof DefaultTreeCellRenderer) {
			((DefaultTreeCellRenderer) rendererComponent).setBackgroundSelectionColor(oldBackgroundSelectionColor);
		}
	}

	/**
	 * Updates the selection colors for unfocused state.
	 *
	 * @param rendererComponent the cell renderer component
	 * @return the original background selection color if modified, null otherwise
	 */
	private Color updateSelectionColors(Component rendererComponent) {
		Color oldBackgroundSelectionColor = null;

		if (rendererComponent instanceof DefaultTreeCellRenderer renderer) {
			if (renderer.getBackgroundSelectionColor() == selectionBackground) {
				oldBackgroundSelectionColor = renderer.getBackgroundSelectionColor();
				renderer.setBackgroundSelectionColor(selectionInactiveBackground);
			}
		} else if (rendererComponent.getBackground() == selectionBackground) {
			rendererComponent.setBackground(selectionInactiveBackground);
		}

		if (rendererComponent.getForeground() == selectionForeground) {
			rendererComponent.setForeground(selectionInactiveForeground);
		}

		return oldBackgroundSelectionColor;
	}
}
