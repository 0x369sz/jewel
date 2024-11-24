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
import com.slobodanzivanovic.jewel.laf.core.util.UIScale;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;
import java.util.Objects;

/**
 * UI delegate for JLabel components in the Jewel Look and Feel.
 * Provides custom painting and layout handling with UI scaling support.
 */
public final class JewelLabelUI extends BasicLabelUI {
	private static final String DISABLED_FOREGROUND_KEY = "Label.disabledForeground";

	private Color disabledForeground;
	private boolean defaultsInitialized;

	// Thread-safe singleton instance using volatile
	private static volatile ComponentUI instance;

	private JewelLabelUI() {
		// Private constructor to enforce singleton
	}

	/**
	 * Creates or returns the UI delegate for the specified component.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		ComponentUI result = instance;
		if (result == null) {
			synchronized (JewelLabelUI.class) {
				result = instance;
				if (result == null) {
					instance = result = new JewelLabelUI();
				}
			}
		}
		return result;
	}

	@Override
	protected void installDefaults(JLabel label) {
		Objects.requireNonNull(label, "Label cannot be null");
		super.installDefaults(label);

		if (!defaultsInitialized) {
			disabledForeground = UIManager.getColor(DISABLED_FOREGROUND_KEY);
			defaultsInitialized = true;
		}
	}

	@Override
	protected void uninstallDefaults(JLabel label) {
		Objects.requireNonNull(label, "Label cannot be null");
		super.uninstallDefaults(label);
		defaultsInitialized = false;
	}

	@Override
	protected void paintEnabledText(JLabel label, Graphics g, String text, int textX, int textY) {
		Objects.requireNonNull(label, "Label cannot be null");
		Objects.requireNonNull(g, "Graphics cannot be null");

		int mnemIndex = JewelLaf.isShowMnemonics() ? label.getDisplayedMnemonicIndex() : -1;
		g.setColor(label.getForeground());
		JewelUIUtils.drawStringUnderlineCharAt(label, g, text, mnemIndex, textX, textY);
	}

	@Override
	protected void paintDisabledText(JLabel label, Graphics g, String text, int textX, int textY) {
		Objects.requireNonNull(label, "Label cannot be null");
		Objects.requireNonNull(g, "Graphics cannot be null");

		int mnemIndex = JewelLaf.isShowMnemonics() ? label.getDisplayedMnemonicIndex() : -1;
		g.setColor(disabledForeground);
		JewelUIUtils.drawStringUnderlineCharAt(label, g, text, mnemIndex, textX, textY);
	}

	@Override
	protected String layoutCL(JLabel label, FontMetrics fontMetrics, String text, Icon icon,
							  Rectangle viewR, Rectangle iconR, Rectangle textR) {
		Objects.requireNonNull(label, "Label cannot be null");
		Objects.requireNonNull(fontMetrics, "FontMetrics cannot be null");

		return SwingUtilities.layoutCompoundLabel(
			label,
			fontMetrics,
			text,
			icon,
			label.getVerticalAlignment(),
			label.getHorizontalAlignment(),
			label.getVerticalTextPosition(),
			label.getHorizontalTextPosition(),
			viewR,
			iconR,
			textR,
			UIScale.scale(label.getIconTextGap())
		);
	}
}
