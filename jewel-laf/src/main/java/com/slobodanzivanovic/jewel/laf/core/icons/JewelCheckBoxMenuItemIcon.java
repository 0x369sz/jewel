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
 * Icon implementation for checkbox menu items in the Jewel Look and Feel.
 * Displays a checkmark when the menu item is selected, with colors that
 * adapt to the menu item's state (armed, enabled, disabled).
 */
public class JewelCheckBoxMenuItemIcon extends JewelAbstractIcon {
	private static final String CHECKMARK_COLOR_KEY = "MenuItemCheckBox.icon.checkmarkColor";
	private static final String DISABLED_CHECKMARK_COLOR_KEY = "MenuItemCheckBox.icon.disabledCheckmarkColor";
	private static final String SELECTION_FOREGROUND_KEY = "Menu.selectionForeground";
	private static final int ICON_SIZE = 15;

	protected final Color checkmarkColor;
	protected final Color disabledCheckmarkColor;
	protected final Color selectionForeground;

	/**
	 * Creates a new checkbox menu item icon with the default size and colors
	 * from the UI defaults.
	 */
	public JewelCheckBoxMenuItemIcon() {
		super(ICON_SIZE, ICON_SIZE, null);
		this.checkmarkColor = UIManager.getColor(CHECKMARK_COLOR_KEY);
		this.disabledCheckmarkColor = UIManager.getColor(DISABLED_CHECKMARK_COLOR_KEY);
		this.selectionForeground = UIManager.getColor(SELECTION_FOREGROUND_KEY);
	}

	@Override
	protected void paintIcon(Component component, Graphics2D g2) {
		boolean selected = component instanceof AbstractButton &&
			((AbstractButton) component).isSelected();

		if (selected) {
			g2.setColor(getCheckmarkColor(component));
			paintCheckmark(g2);
		}
	}

	/**
	 * Paints the checkmark using the current graphics context.
	 *
	 * @param g2 the graphics context to paint with
	 */
	protected void paintCheckmark(Graphics2D g2) {
		Path2D.Float path = new Path2D.Float();
		path.moveTo(4.5f, 7.5f);
		path.lineTo(6.6f, 10f);
		path.lineTo(11.25f, 3.5f);

		g2.setStroke(new BasicStroke(1.9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.draw(path);
	}

	/**
	 * Determines the appropriate checkmark color based on the component's state.
	 *
	 * @param component the component to get the color for
	 * @return the color to use for the checkmark
	 */
	private Color getCheckmarkColor(Component component) {
		if (component instanceof JMenuItem && ((JMenuItem) component).isArmed()) {
			return selectionForeground;
		}

		return component.isEnabled() ? checkmarkColor : disabledCheckmarkColor;
	}
}
