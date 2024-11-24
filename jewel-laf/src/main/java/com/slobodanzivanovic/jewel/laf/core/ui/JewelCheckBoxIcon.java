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
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * Base checkbox icon implementation for the Jewel Look and Feel.
 * Provides a modern, scalable checkbox appearance with support for different states
 * (selected, focused, disabled) and UI scaling.
 */
public class JewelCheckBoxIcon implements Icon, UIResource {

	protected static final float CHECKMARK_STROKE_WIDTH = 1.9f;

	protected final int focusWidth;
	protected final Color focusColor;
	protected final Color borderColor;
	protected final Color disabledBorderColor;
	protected final Color selectedBorderColor;
	protected final Color focusedBorderColor;
	protected final Color selectedFocusedBorderColor;
	protected final Color background;
	protected final Color disabledBackground;
	protected final Color selectedBackground;
	protected final Color checkmarkColor;
	protected final Color disabledCheckmarkColor;
	protected final int iconSize;

	public JewelCheckBoxIcon() {
		this.focusWidth = UIManager.getInt("Component.focusWidth");
		this.focusColor = getRequiredColor("Component.focusColor");
		this.borderColor = getRequiredColor("CheckBox.icon.borderColor");
		this.disabledBorderColor = getRequiredColor("CheckBox.icon.disabledBorderColor");
		this.selectedBorderColor = getRequiredColor("CheckBox.icon.selectedBorderColor");
		this.focusedBorderColor = getRequiredColor("CheckBox.icon.focusedBorderColor");
		this.selectedFocusedBorderColor = getRequiredColor("CheckBox.icon.selectedFocusedBorderColor");
		this.background = getRequiredColor("CheckBox.icon.background");
		this.disabledBackground = getRequiredColor("CheckBox.icon.disabledBackground");
		this.selectedBackground = getRequiredColor("CheckBox.icon.selectedBackground");
		this.checkmarkColor = getRequiredColor("CheckBox.icon.checkmarkColor");
		this.disabledCheckmarkColor = getRequiredColor("CheckBox.icon.disabledCheckmarkColor");
		this.iconSize = 15 + (focusWidth * 2);
	}

	protected static Color getRequiredColor(String key) {
		Color color = UIManager.getColor(key);
		return Objects.requireNonNull(color, key + " not defined in UI defaults");
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g.create();
		try {
			JewelUIUtils.setRenderingHints(g2);
			g2.translate(x, y);
			UIScale.scaleGraphics(g2);

			boolean enabled = c.isEnabled();
			boolean focused = c.hasFocus();
			boolean selected = (c instanceof AbstractButton button) && button.isSelected();

			paintCheckboxStates(g2, enabled, focused, selected);
		} finally {
			g2.dispose();
		}
	}

	protected void paintCheckboxStates(Graphics2D g2, boolean enabled, boolean focused, boolean selected) {
		if (focused) {
			g2.setColor(focusColor);
			paintFocusBorder(g2);
		}

		g2.setColor(determineCheckboxBorderColor(enabled, focused, selected));
		paintBorder(g2);

		g2.setColor(enabled ? (selected ? selectedBackground : background) : disabledBackground);
		paintBackground(g2);

		if (selected) {
			g2.setColor(enabled ? checkmarkColor : disabledCheckmarkColor);
			paintCheckmark(g2);
		}
	}

	protected Color determineCheckboxBorderColor(boolean enabled, boolean focused, boolean selected) {
		if (!enabled) return disabledBorderColor;
		if (selected) return focused ? selectedFocusedBorderColor : selectedBorderColor;
		return focused ? focusedBorderColor : borderColor;
	}

	protected void paintFocusBorder(Graphics2D g2) {
		g2.fillRoundRect(1, 0, iconSize - 1, iconSize - 1, 8, 8);
	}

	protected void paintBorder(Graphics2D g2) {
		g2.fillRoundRect(focusWidth + 1, focusWidth, 14, 14, 4, 4);
	}

	protected void paintBackground(Graphics2D g2) {
		g2.fillRoundRect(focusWidth + 2, focusWidth + 1, 12, 12, 4, 4);
	}

	protected void paintCheckmark(Graphics2D g2) {
		Path2D.Float path = new Path2D.Float();
		path.moveTo(4.5f, 7.5f);
		path.lineTo(6.6f, 10f);
		path.lineTo(11.25f, 3.5f);

		g2.translate(focusWidth, focusWidth);
		g2.setStroke(new BasicStroke(CHECKMARK_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.draw(path);
		g2.translate(-focusWidth, -focusWidth);
	}

	@Override
	public int getIconWidth() {
		return scale(iconSize);
	}

	@Override
	public int getIconHeight() {
		return scale(iconSize);
	}
}
