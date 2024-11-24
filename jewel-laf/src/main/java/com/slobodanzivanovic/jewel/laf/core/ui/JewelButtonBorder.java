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

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * A border for JButton components in the Jewel Look and Feel.
 * This border provides custom styling for different button states including
 * default, hover, focused, and disabled states.
 */
public class JewelButtonBorder extends JewelBorder {

	protected final Color borderColor;
	protected final Color disabledBorderColor;
	protected final Color focusedBorderColor;
	protected final Color hoverBorderColor;
	protected final Color defaultBorderColor;
	protected final Color defaultHoverBorderColor;
	protected final Color defaultFocusedBorderColor;
	protected final Color defaultFocusColor;
	protected final int defaultBorderWidth;
	protected final int arc;

	/**
	 * Creates a new JewelButtonBorder instance with UI properties.
	 */
	public JewelButtonBorder() {
		this.borderColor = UIManager.getColor("Button.borderColor");
		this.disabledBorderColor = UIManager.getColor("Button.disabledBorderColor");
		this.focusedBorderColor = UIManager.getColor("Button.focusedBorderColor");
		this.hoverBorderColor = UIManager.getColor("Button.hoverBorderColor");
		this.defaultBorderColor = UIManager.getColor("Button.default.borderColor");
		this.defaultHoverBorderColor = UIManager.getColor("Button.default.hoverBorderColor");
		this.defaultFocusedBorderColor = UIManager.getColor("Button.default.focusedBorderColor");
		this.defaultFocusColor = UIManager.getColor("Button.default.focusColor");
		this.defaultBorderWidth = UIManager.getInt("Button.default.borderWidth");
		this.arc = UIManager.getInt("Button.arc");
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		if (c == null || g == null) return;

		if (JewelButtonUI.isContentAreaFilled(c) && !JewelButtonUI.isHelpButton(c)) {
			super.paintBorder(c, g, x, y, width, height);
		}
	}

	@Override
	protected Color getFocusColor(Component c) {
		if (c == null) return super.getFocusColor(null);
		return JewelButtonUI.isDefaultButton(c) ? defaultFocusColor : super.getFocusColor(c);
	}

	@Override
	protected Paint getBorderColor(Component c) {
		if (c == null) return borderColor;

		boolean isDefault = JewelButtonUI.isDefaultButton(c);
		return JewelButtonUI.buttonStateColor(c,
			isDefault ? defaultBorderColor : borderColor,
			disabledBorderColor,
			isDefault ? defaultFocusedBorderColor : focusedBorderColor,
			isDefault ? defaultHoverBorderColor : hoverBorderColor,
			null);
	}

	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		if (c == null || insets == null) {
			return new Insets(0, 0, 0, 0);
		}

		insets = super.getBorderInsets(c, insets);

		if (JewelButtonUI.isIconOnlyButton(c) && ((JButton) c).getMargin() instanceof UIResource) {
			int minInset = Math.min(insets.top, insets.bottom);
			insets.left = insets.right = minInset;
		}

		return insets;
	}

	@Override
	protected float getBorderWidth(Component c) {
		if (c == null) return super.getBorderWidth(null);
		return JewelButtonUI.isDefaultButton(c) ? scale((float) defaultBorderWidth) : super.getBorderWidth(c);
	}

	@Override
	protected float getArc() {
		return scale((float) arc);
	}
}
