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

import com.slobodanzivanovic.jewel.laf.core.JewelLaf;
import com.slobodanzivanovic.jewel.laf.core.util.JewelUIUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolTipUI;
import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * UI delegate for JToolTip components in the Jewel Look and Feel.
 * Provides support for both single-line and multi-line tooltips,
 * with proper text rendering and size calculations.
 */
public class JewelToolTipUI extends BasicToolTipUI {
	private static final String HTML_PROPERTY_KEY = "html";
	private static ComponentUI instance;

	/**
	 * Creates or returns the singleton UI delegate for JToolTip components.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		if (instance == null) {
			instance = new JewelToolTipUI();
		}
		return instance;
	}

	@Override
	public Dimension getPreferredSize(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");

		if (isMultiLine(component)) {
			JToolTip toolTip = (JToolTip) component;
			FontMetrics fontMetrics = component.getFontMetrics(component.getFont());
			Insets insets = component.getInsets();

			List<String> lines = JewelLaf.split(toolTip.getTipText(), '\n');
			int lineCount = Math.max(lines.size(), 1);

			int maxWidth = lines.stream()
				.mapToInt(line -> SwingUtilities.computeStringWidth(fontMetrics, line))
				.max()
				.orElse(0);

			int totalHeight = fontMetrics.getHeight() * lineCount;

			return new Dimension(
				insets.left + maxWidth + insets.right,
				insets.top + totalHeight + insets.bottom
			);
		}

		return super.getPreferredSize(component);
	}

	@Override
	public void paint(Graphics graphics, JComponent component) {
		Objects.requireNonNull(graphics, "Graphics cannot be null");
		Objects.requireNonNull(component, "Component cannot be null");

		if (isMultiLine(component)) {
			Graphics2D g2d = (Graphics2D) graphics.create();
			try {
				JToolTip toolTip = (JToolTip) component;
				FontMetrics fontMetrics = component.getFontMetrics(component.getFont());
				Insets insets = component.getInsets();

				JewelUIUtils.setRenderingHints(g2d);
				g2d.setColor(component.getForeground());

				List<String> lines = JewelLaf.split(toolTip.getTipText(), '\n');
				int x = insets.left;
				int y = insets.top - fontMetrics.getDescent();
				int lineHeight = fontMetrics.getHeight();

				for (String line : lines) {
					y += lineHeight;
					g2d.drawString(line, x, y);
				}
			} finally {
				g2d.dispose();
			}
		} else {
			super.paint(graphics, component);
		}
	}

	/**
	 * Determines if the tooltip contains multi-line text.
	 * A tooltip is considered multi-line if it contains newline characters
	 * and is not using HTML formatting.
	 *
	 * @param component the tooltip component to check
	 * @return true if the tooltip contains multiple lines, false otherwise
	 */
	private boolean isMultiLine(JComponent component) {
		JToolTip toolTip = (JToolTip) component;
		String text = toolTip.getTipText();

		return component.getClientProperty(HTML_PROPERTY_KEY) == null
			&& text != null
			&& text.indexOf('\n') >= 0;
	}
}
