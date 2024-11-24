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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * A circular help button icon with a question mark for the Jewel Look and Feel.
 * Features multiple visual states (enabled, disabled, focused, hover, pressed)
 * and supports UI scaling. The icon consists of a circular button with borders,
 * backgrounds, and a stylized question mark symbol.
 */
public class JewelHelpButtonIcon extends JewelAbstractIcon {
	// UI properties
	protected final int focusWidth;
	protected final Color focusColor;

	// Border colors for different states
	protected final Color borderColor;
	protected final Color disabledBorderColor;
	protected final Color focusedBorderColor;
	protected final Color hoverBorderColor;

	// Background colors for different states
	protected final Color background;
	protected final Color disabledBackground;
	protected final Color focusedBackground;
	protected final Color hoverBackground;
	protected final Color pressedBackground;

	// Question mark colors
	protected final Color questionMarkColor;
	protected final Color disabledQuestionMarkColor;

	// Icon dimensions
	protected final int baseIconSize = 22;
	protected final int iconSize;

	// Question mark path coordinates
	private static final float QM_CENTER_X = 11f;
	private static final float QM_TOP_Y = 5f;
	private static final float QM_CURVE_LEFT = 7f;
	private static final float QM_CURVE_RIGHT = 15f;
	private static final float QM_DOT_Y = 15f;
	private static final float QM_DOT_SIZE = 2f;

	/**
	 * Creates a new help button icon with scaling and focus border support.
	 * Initializes all colors from UIManager properties.
	 */
	public JewelHelpButtonIcon() {
		super(0, 0, null);

		// Initialize UI properties
		this.focusWidth = UIManager.getInt("Component.focusWidth");
		this.focusColor = UIManager.getColor("Component.focusColor");

		// Initialize border colors
		this.borderColor = UIManager.getColor("HelpButton.borderColor");
		this.disabledBorderColor = UIManager.getColor("HelpButton.disabledBorderColor");
		this.focusedBorderColor = UIManager.getColor("HelpButton.focusedBorderColor");
		this.hoverBorderColor = UIManager.getColor("HelpButton.hoverBorderColor");

		// Initialize background colors
		this.background = UIManager.getColor("HelpButton.background");
		this.disabledBackground = UIManager.getColor("HelpButton.disabledBackground");
		this.focusedBackground = UIManager.getColor("HelpButton.focusedBackground");
		this.hoverBackground = UIManager.getColor("HelpButton.hoverBackground");
		this.pressedBackground = UIManager.getColor("HelpButton.pressedBackground");

		// Initialize question mark colors
		this.questionMarkColor = UIManager.getColor("HelpButton.questionMarkColor");
		this.disabledQuestionMarkColor = UIManager.getColor("HelpButton.disabledQuestionMarkColor");

		// Calculate total icon size including focus border
		this.iconSize = baseIconSize + (focusWidth * 2);
	}

	@Override
	protected void paintIcon(Component c, Graphics2D g2) {
		paintButtonCircle(c, g2);
		paintQuestionMark(c, g2);
	}

	/**
	 * Paints the circular button background with appropriate borders and states.
	 *
	 * @param c  The component to paint on
	 * @param g2 The graphics context
	 */
	private void paintButtonCircle(Component c, Graphics2D g2) {
		boolean focused = c.hasFocus();
		float centerOffset = focusWidth;

		// Paint focus ring if focused
		if (focused) {
			g2.setColor(focusColor);
			g2.fill(new Ellipse2D.Float(centerOffset + 0.5f, centerOffset + 0.5f,
				baseIconSize - 1, baseIconSize - 1));
		}

		// Paint border
		g2.setColor(JewelButtonUI.buttonStateColor(c, borderColor, disabledBorderColor,
			focusedBorderColor, hoverBorderColor, null));
		g2.fill(new Ellipse2D.Float(centerOffset + 0.5f, centerOffset + 0.5f,
			baseIconSize - 1, baseIconSize - 1));

		// Paint background
		g2.setColor(JewelButtonUI.buttonStateColor(c, background, disabledBackground,
			focusedBackground, hoverBackground, pressedBackground));
		g2.fill(new Ellipse2D.Float(centerOffset + 1.5f, centerOffset + 1.5f,
			baseIconSize - 3, baseIconSize - 3));
	}

	/**
	 * Paints the question mark symbol with the appropriate color based on state.
	 *
	 * @param c  The component to paint on
	 * @param g2 The graphics context
	 */
	private void paintQuestionMark(Component c, Graphics2D g2) {
		float centerOffset = focusWidth;
		g2.translate(centerOffset, centerOffset);

		g2.setColor(c.isEnabled() ? questionMarkColor : disabledQuestionMarkColor);

		// Paint question mark curve
		Path2D questionMark = createQuestionMarkPath();
		g2.fill(questionMark);

		// Paint question mark dot
		g2.fillRect(10, (int) QM_DOT_Y, (int) QM_DOT_SIZE, (int) QM_DOT_SIZE);

		g2.translate(-centerOffset, -centerOffset);
	}

	/**
	 * Creates the path for the question mark curve.
	 *
	 * @return Path2D representing the question mark curve
	 */
	private Path2D createQuestionMarkPath() {
		Path2D questionMark = new Path2D.Float();
		questionMark.moveTo(QM_CENTER_X, QM_TOP_Y);
		questionMark.curveTo(8.8f, QM_TOP_Y, QM_CURVE_LEFT, 6.8f, QM_CURVE_LEFT, 9);
		questionMark.lineTo(9, 9);
		questionMark.curveTo(9, 7.9f, 9.9f, 7, QM_CENTER_X, 7);
		questionMark.curveTo(12.1f, 7, 13, 7.9f, 13, 9);
		questionMark.curveTo(13, 11, 10, 10.75f, 10, 14);
		questionMark.lineTo(12, 14);
		questionMark.curveTo(12, 11.75f, QM_CURVE_RIGHT, 11.5f, QM_CURVE_RIGHT, 9);
		questionMark.curveTo(QM_CURVE_RIGHT, 6.8f, 13.2f, QM_TOP_Y, QM_CENTER_X, QM_TOP_Y);
		questionMark.closePath();
		return questionMark;
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
