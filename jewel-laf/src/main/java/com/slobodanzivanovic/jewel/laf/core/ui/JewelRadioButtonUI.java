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
import com.slobodanzivanovic.jewel.laf.core.util.MigLayoutVisualPadding;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import java.awt.*;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * UI delegate for radio buttons in the Jewel Look and Feel.
 */
public class JewelRadioButtonUI extends BasicRadioButtonUI {
	protected int iconTextGap;
	protected Color disabledText;

	private boolean defaultsInitialized = false;

	/**
	 * Creates a new UI delegate for the specified component.
	 *
	 * @param component the component to create the UI delegate for
	 * @return the UI delegate for the component
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component must not be null");
		return new JewelRadioButtonUI();
	}

	@Override
	public void installDefaults(AbstractButton b) {
		super.installDefaults(b);

		if (!defaultsInitialized) {
			String prefix = getPropertyPrefix();

			iconTextGap = JewelUIUtils.getUIInt(prefix + "iconTextGap", 4);
			disabledText = UIManager.getColor(prefix + "disabledText");

			defaultsInitialized = true;
		}

		LookAndFeel.installProperty(b, "iconTextGap", scale(iconTextGap));

		MigLayoutVisualPadding.install(b, null);
	}

	@Override
	protected void uninstallDefaults(AbstractButton b) {
		super.uninstallDefaults(b);

		MigLayoutVisualPadding.uninstall(b);
		defaultsInitialized = false;
	}

	@Override
	protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
		JewelButtonUI.paintText(g, b, textRect, text, b.isEnabled() ? b.getForeground() : disabledText);
	}
}
