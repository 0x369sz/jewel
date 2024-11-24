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
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

/**
 * UI delegate for table header components in the Jewel Look and Feel.
 * Provides custom painting with configurable separators, colors, and height,
 * supporting both LTR and RTL orientations.
 */
public class JewelTableHeaderUI extends BasicTableHeaderUI {
	private static final String DEFAULT_RENDERER_CLASS = "sun.swing.table.DefaultTableCellHeaderRenderer";

	// UI Manager keys
	private static final String KEY_SEPARATOR_COLOR = "TableHeader.separatorColor";
	private static final String KEY_BOTTOM_SEPARATOR_COLOR = "TableHeader.bottomSeparatorColor";
	private static final String KEY_HEIGHT = "TableHeader.height";

	protected Color separatorColor;
	protected Color bottomSeparatorColor;
	protected int height;

	/**
	 * Creates or returns the UI delegate for table header components.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelTableHeaderUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		separatorColor = UIManager.getColor(KEY_SEPARATOR_COLOR);
		bottomSeparatorColor = UIManager.getColor(KEY_BOTTOM_SEPARATOR_COLOR);
		height = UIManager.getInt(KEY_HEIGHT);
	}

	@Override
	protected void uninstallDefaults() {
		super.uninstallDefaults();

		separatorColor = null;
		bottomSeparatorColor = null;
	}

	@Override
	public void paint(Graphics g, JComponent component) {
		Objects.requireNonNull(g, "Graphics cannot be null");
		Objects.requireNonNull(component, "Component cannot be null");

		boolean paintBorders = header.getDefaultRenderer().getClass().getName().equals(DEFAULT_RENDERER_CLASS);

		if (paintBorders) {
			paintColumnBorders(g, component);
		}

		super.paint(g, component);

		if (paintBorders) {
			paintDraggedColumnBorders(g, component);
		}
	}

	private void paintColumnBorders(Graphics g, JComponent component) {
		Objects.requireNonNull(g, "Graphics cannot be null");
		Objects.requireNonNull(component, "Component cannot be null");

		int width = component.getWidth();
		int height = component.getHeight();
		float lineWidth = UIScale.scale(1f);
		float bottomLineIndent = lineWidth * 3;
		TableColumnModel columnModel = header.getColumnModel();
		int columnCount = columnModel.getColumnCount();

		Graphics2D g2 = (Graphics2D) g.create();
		try {
			JewelUIUtils.setRenderingHints(g2);

			// Paint bottom separator
			g2.setColor(bottomSeparatorColor);
			g2.fill(new Rectangle2D.Float(0, height - lineWidth, width, lineWidth));

			g2.setColor(separatorColor);

			int sepCount = columnCount;
			if (header.getTable().getAutoResizeMode() != JTable.AUTO_RESIZE_OFF && !isVerticalScrollBarVisible()) {
				sepCount--;
			}

			if (header.getComponentOrientation().isLeftToRight()) {
				paintLeftToRightSeparators(g2, columnModel, sepCount, lineWidth, lineWidth, bottomLineIndent);
			} else {
				paintRightToLeftSeparators(g2, columnModel, sepCount, width, lineWidth, lineWidth, bottomLineIndent);
			}
		} finally {
			g2.dispose();
		}
	}

	private void paintLeftToRightSeparators(Graphics2D g2, TableColumnModel columnModel,
											int sepCount, float lineWidth, float topLineIndent, float bottomLineIndent) {
		int x = 0;
		for (int i = 0; i < sepCount; i++) {
			x += columnModel.getColumn(i).getWidth();
			g2.fill(new Rectangle2D.Float(x - lineWidth, topLineIndent, lineWidth,
				header.getHeight() - bottomLineIndent));
		}
	}

	private void paintRightToLeftSeparators(Graphics2D g2, TableColumnModel columnModel,
											int sepCount, int width, float lineWidth, float topLineIndent, float bottomLineIndent) {
		int x = width;
		for (int i = 0; i < sepCount; i++) {
			x -= columnModel.getColumn(i).getWidth();
			g2.fill(new Rectangle2D.Float(x - (i < sepCount - 1 ? lineWidth : 0),
				topLineIndent, lineWidth, header.getHeight() - bottomLineIndent));
		}
	}

	private void paintDraggedColumnBorders(Graphics g, JComponent component) {
		TableColumn draggedColumn = header.getDraggedColumn();
		if (draggedColumn == null) {
			return;
		}

		int draggedColumnIndex = getDraggedColumnIndex(draggedColumn);
		if (draggedColumnIndex < 0) {
			return;
		}

		float lineWidth = UIScale.scale(1f);
		float bottomLineIndent = lineWidth * 3;
		Rectangle r = header.getHeaderRect(draggedColumnIndex);
		r.x += header.getDraggedDistance();

		Graphics2D g2 = (Graphics2D) g.create();
		try {
			JewelUIUtils.setRenderingHints(g2);

			g2.setColor(bottomSeparatorColor);
			g2.fill(new Rectangle2D.Float(r.x, r.y + r.height - lineWidth, r.width, lineWidth));

			g2.setColor(separatorColor);
			g2.fill(new Rectangle2D.Float(r.x, lineWidth, lineWidth, r.height - bottomLineIndent));
			g2.fill(new Rectangle2D.Float(r.x + r.width - lineWidth, r.y + lineWidth,
				lineWidth, r.height - bottomLineIndent));
		} finally {
			g2.dispose();
		}
	}

	private int getDraggedColumnIndex(TableColumn draggedColumn) {
		TableColumnModel columnModel = header.getColumnModel();
		int columnCount = columnModel.getColumnCount();

		for (int i = 0; i < columnCount; i++) {
			if (columnModel.getColumn(i) == draggedColumn) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public Dimension getPreferredSize(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");

		Dimension size = super.getPreferredSize(component);
		if (size.height > 0) {
			size.height = Math.max(size.height, UIScale.scale(height));
		}
		return size;
	}

	private boolean isVerticalScrollBarVisible() {
		JScrollPane scrollPane = getScrollPane();
		return scrollPane != null &&
			scrollPane.getVerticalScrollBar() != null &&
			scrollPane.getVerticalScrollBar().isVisible();
	}

	private JScrollPane getScrollPane() {
		Container parent = header.getParent();
		if (parent == null) {
			return null;
		}

		parent = parent.getParent();
		return (parent instanceof JScrollPane scrollPane) ? scrollPane : null;
	}
}
