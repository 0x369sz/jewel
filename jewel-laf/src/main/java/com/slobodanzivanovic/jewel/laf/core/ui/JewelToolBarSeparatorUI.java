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

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarSeparatorUI;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * UI delegate for JToolBar.Separator components in the Jewel Look and Feel.
 * Provides custom painting with configurable width and color for both horizontal
 * and vertical orientations. The separator appears as a thin line with equal padding
 * on both sides.
 */
public class JewelToolBarSeparatorUI extends BasicToolBarSeparatorUI {
	private static final String SEPARATOR_WIDTH_KEY = "ToolBar.separatorWidth";
	private static final String SEPARATOR_COLOR_KEY = "ToolBar.separatorColor";
	private static final float LINE_WIDTH = 1.0f;
	private static final float EDGE_OFFSET = 2.0f;

	protected int separatorWidth;
	protected Color separatorColor;

	private boolean defaultsInitialized;
	private static ComponentUI instance;

	/**
	 * Creates or returns the singleton UI delegate for JToolBar.Separator components.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		if (instance == null) {
			instance = new JewelToolBarSeparatorUI();
		}
		return instance;
	}

	@Override
	protected void installDefaults(JSeparator separator) {
		Objects.requireNonNull(separator, "Separator cannot be null");
		super.installDefaults(separator);

		if (!defaultsInitialized) {
			separatorWidth = UIManager.getInt(SEPARATOR_WIDTH_KEY);
			separatorColor = UIManager.getColor(SEPARATOR_COLOR_KEY);
			defaultsInitialized = true;
		}

		separator.setAlignmentX(0.0f);
	}

	@Override
	protected void uninstallDefaults(JSeparator separator) {
		Objects.requireNonNull(separator, "Separator cannot be null");
		super.uninstallDefaults(separator);
		defaultsInitialized = false;
	}

	@Override
	public Dimension getPreferredSize(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		JToolBar.Separator separator = (JToolBar.Separator) component;
		Dimension customSize = separator.getSeparatorSize();

		if (customSize != null) {
			return scale(customSize);
		}

		int scaledWidth = (int) ((scale((separatorWidth - LINE_WIDTH) / 2) * 2) + scale(LINE_WIDTH));
		boolean isVertical = isVertical(component);

		return new Dimension(
			isVertical ? scaledWidth : 0,
			isVertical ? 0 : scaledWidth
		);
	}

	@Override
	public Dimension getMaximumSize(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		Dimension preferredSize = getPreferredSize(component);

		return isVertical(component)
			? new Dimension(preferredSize.width, Short.MAX_VALUE)
			: new Dimension(Short.MAX_VALUE, preferredSize.height);
	}

	@Override
	public void paint(Graphics graphics, JComponent component) {
		Objects.requireNonNull(graphics, "Graphics cannot be null");
		Objects.requireNonNull(component, "Component cannot be null");

		Graphics2D g2d = (Graphics2D) graphics.create();
		try {
			JewelUIUtils.setRenderingHints(g2d);
			g2d.setColor(separatorColor);

			float scaledLineWidth = scale(LINE_WIDTH);
			float scaledOffset = scale(EDGE_OFFSET);
			float width = component.getWidth();
			float height = component.getHeight();

			if (isVertical(component)) {
				float x = Math.round((width - scaledLineWidth) / 2.0f);
				g2d.fill(new Rectangle2D.Float(x, scaledOffset, scaledLineWidth, height - (scaledOffset * 2)));
			} else {
				float y = Math.round((height - scaledLineWidth) / 2.0f);
				g2d.fill(new Rectangle2D.Float(scaledOffset, y, width - (scaledOffset * 2), scaledLineWidth));
			}
		} finally {
			g2d.dispose();
		}
	}

	/**
	 * Determines if the separator has a vertical orientation.
	 *
	 * @param component the separator component to check
	 * @return true if the separator is vertical, false otherwise
	 */
	private boolean isVertical(JComponent component) {
		return ((JToolBar.Separator) component).getOrientation() == SwingConstants.VERTICAL;
	}
}
