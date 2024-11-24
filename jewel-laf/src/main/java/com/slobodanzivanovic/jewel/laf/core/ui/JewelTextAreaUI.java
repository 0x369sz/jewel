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
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * UI delegate for text area components in the Jewel Look and Feel.
 * Provides custom background colors for different states and minimum width support.
 */
public class JewelTextAreaUI extends BasicTextAreaUI {

	private static final String KEY_MINIMUM_WIDTH = "Component.minimumWidth";
	private static final String KEY_DISABLED_BACKGROUND = "TextArea.disabledBackground";
	private static final String KEY_INACTIVE_BACKGROUND = "TextArea.inactiveBackground";

	protected int minimumWidth;
	protected Color disabledBackground;
	protected Color inactiveBackground;

	/**
	 * Creates or returns the UI delegate for text area components.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelTextAreaUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		minimumWidth = UIManager.getInt(KEY_MINIMUM_WIDTH);
		disabledBackground = UIManager.getColor(KEY_DISABLED_BACKGROUND);
		inactiveBackground = UIManager.getColor(KEY_INACTIVE_BACKGROUND);
	}

	@Override
	protected void uninstallDefaults() {
		super.uninstallDefaults();

		disabledBackground = null;
		inactiveBackground = null;
	}

	@Override
	protected void paintBackground(Graphics g) {
		JTextComponent component = getComponent();
		Color background = component.getBackground();

		Color paintColor = background;
		if (background instanceof UIResource) {
			if (!component.isEnabled()) {
				paintColor = disabledBackground;
			} else if (!component.isEditable()) {
				paintColor = inactiveBackground;
			}
		}

		g.setColor(paintColor);
		g.fillRect(0, 0, component.getWidth(), component.getHeight());
	}

	@Override
	public Dimension getPreferredSize(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return applyMinimumWidth(super.getPreferredSize(component));
	}

	@Override
	public Dimension getMinimumSize(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return applyMinimumWidth(super.getMinimumSize(component));
	}

	private Dimension applyMinimumWidth(Dimension size) {
		size.width = Math.max(size.width, scale(minimumWidth) - (scale(1) * 2));
		return size;
	}
}
