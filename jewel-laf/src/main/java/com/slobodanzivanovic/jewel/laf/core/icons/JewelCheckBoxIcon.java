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

import com.slobodanzivanovic.jewel.laf.core.ui.JewelButtonUI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Icon implementation for checkboxes in the Jewel Look and Feel.
 * Supports multiple visual states including focus, hover, pressed,
 * selected, and disabled states with corresponding colors.
 */
public class JewelCheckBoxIcon extends JewelAbstractIcon {
	private static final String FOCUS_WIDTH_KEY = "Component.focusWidth";
	private static final String FOCUS_COLOR_KEY = "Component.focusColor";
	private static final String BORDER_COLOR_KEY = "CheckBox.icon.borderColor";
	private static final String DISABLED_BORDER_COLOR_KEY = "CheckBox.icon.disabledBorderColor";
	private static final String SELECTED_BORDER_COLOR_KEY = "CheckBox.icon.selectedBorderColor";
	private static final String FOCUSED_BORDER_COLOR_KEY = "CheckBox.icon.focusedBorderColor";
	private static final String HOVER_BORDER_COLOR_KEY = "CheckBox.icon.hoverBorderColor";
	private static final String SELECTED_FOCUSED_BORDER_COLOR_KEY = "CheckBox.icon.selectedFocusedBorderColor";
	private static final String BACKGROUND_KEY = "CheckBox.icon.background";
	private static final String DISABLED_BACKGROUND_KEY = "CheckBox.icon.disabledBackground";
	private static final String FOCUSED_BACKGROUND_KEY = "CheckBox.icon.focusedBackground";
	private static final String HOVER_BACKGROUND_KEY = "CheckBox.icon.hoverBackground";
	private static final String PRESSED_BACKGROUND_KEY = "CheckBox.icon.pressedBackground";
	private static final String SELECTED_BACKGROUND_KEY = "CheckBox.icon.selectedBackground";
	private static final String SELECTED_HOVER_BACKGROUND_KEY = "CheckBox.icon.selectedHoverBackground";
	private static final String SELECTED_PRESSED_BACKGROUND_KEY = "CheckBox.icon.selectedPressedBackground";
	private static final String CHECKMARK_COLOR_KEY = "CheckBox.icon.checkmarkColor";
	private static final String DISABLED_CHECKMARK_COLOR_KEY = "CheckBox.icon.disabledCheckmarkColor";

	protected final int focusWidth;
	protected final Color focusColor;

	protected final Color borderColor;
	protected final Color disabledBorderColor;
	protected final Color selectedBorderColor;
	protected final Color focusedBorderColor;
	protected final Color hoverBorderColor;
	protected final Color selectedFocusedBorderColor;

	protected final Color background;
	protected final Color disabledBackground;
	protected final Color focusedBackground;
	protected final Color hoverBackground;
	protected final Color pressedBackground;
	protected final Color selectedBackground;
	protected final Color selectedHoverBackground;
	protected final Color selectedPressedBackground;

	protected final Color checkmarkColor;
	protected final Color disabledCheckmarkColor;

	static final int ICON_SIZE = 15;

	/**
	 * Creates a new checkbox icon with the default size and colors from the UI defaults.
	 */
	public JewelCheckBoxIcon() {
		super(ICON_SIZE, ICON_SIZE, null);

		this.focusWidth = UIManager.getInt(FOCUS_WIDTH_KEY);
		this.focusColor = UIManager.getColor(FOCUS_COLOR_KEY);

		this.borderColor = UIManager.getColor(BORDER_COLOR_KEY);
		this.disabledBorderColor = UIManager.getColor(DISABLED_BORDER_COLOR_KEY);
		this.selectedBorderColor = UIManager.getColor(SELECTED_BORDER_COLOR_KEY);
		this.focusedBorderColor = UIManager.getColor(FOCUSED_BORDER_COLOR_KEY);
		this.hoverBorderColor = UIManager.getColor(HOVER_BORDER_COLOR_KEY);
		this.selectedFocusedBorderColor = UIManager.getColor(SELECTED_FOCUSED_BORDER_COLOR_KEY);

		this.background = UIManager.getColor(BACKGROUND_KEY);
		this.disabledBackground = UIManager.getColor(DISABLED_BACKGROUND_KEY);
		this.focusedBackground = UIManager.getColor(FOCUSED_BACKGROUND_KEY);
		this.hoverBackground = UIManager.getColor(HOVER_BACKGROUND_KEY);
		this.pressedBackground = UIManager.getColor(PRESSED_BACKGROUND_KEY);
		this.selectedBackground = UIManager.getColor(SELECTED_BACKGROUND_KEY);
		this.selectedHoverBackground = UIManager.getColor(SELECTED_HOVER_BACKGROUND_KEY);
		this.selectedPressedBackground = UIManager.getColor(SELECTED_PRESSED_BACKGROUND_KEY);

		this.checkmarkColor = UIManager.getColor(CHECKMARK_COLOR_KEY);
		this.disabledCheckmarkColor = UIManager.getColor(DISABLED_CHECKMARK_COLOR_KEY);
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g2) {
		boolean selected = c instanceof AbstractButton && ((AbstractButton) c).isSelected();

		if (c.hasFocus() && focusWidth > 0) {
			g2.setColor(focusColor);
			paintFocusBorder(g2);
		}

		g2.setColor(JewelButtonUI.buttonStateColor(c, selected ? selectedBorderColor : borderColor, disabledBorderColor,
			selected && selectedFocusedBorderColor != null ? selectedFocusedBorderColor : focusedBorderColor,
			hoverBorderColor, null));
		paintBorder(g2);

		g2.setColor(JewelButtonUI.buttonStateColor(c, selected ? selectedBackground : background, disabledBackground,
			focusedBackground,
			selected && selectedHoverBackground != null ? selectedHoverBackground : hoverBackground,
			selected && selectedPressedBackground != null ? selectedPressedBackground : pressedBackground));
		paintBackground(g2);

		if (selected) {
			g2.setColor(c.isEnabled() ? checkmarkColor : disabledCheckmarkColor);
			paintCheckmark(g2);
		}
	}

	protected void paintFocusBorder(Graphics2D g2) {
		int wh = ICON_SIZE - 1 + (focusWidth * 2);
		g2.fillRoundRect(-focusWidth + 1, -focusWidth, wh, wh, 8, 8);
	}

	protected void paintBorder(Graphics2D g2) {
		g2.fillRoundRect(1, 0, 14, 14, 4, 4);
	}

	protected void paintBackground(Graphics2D g2) {
		g2.fillRoundRect(2, 1, 12, 12, 3, 3);
	}

	protected void paintCheckmark(Graphics2D g2) {
		Path2D.Float path = new Path2D.Float();
		path.moveTo(4.5f, 7.5f);
		path.lineTo(6.6f, 10f);
		path.lineTo(11.25f, 3.5f);

		g2.setStroke(new BasicStroke(1.9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.draw(path);
	}
}
