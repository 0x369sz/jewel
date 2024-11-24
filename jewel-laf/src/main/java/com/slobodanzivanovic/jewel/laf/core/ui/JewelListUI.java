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
import javax.swing.plaf.basic.BasicListUI;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Objects;

/**
 * UI delegate for JList components in the Jewel Look and Feel.
 */
public class JewelListUI extends BasicListUI {
	private static final String SELECTION_BACKGROUND_KEY = "List.selectionBackground";
	private static final String SELECTION_FOREGROUND_KEY = "List.selectionForeground";
	private static final String INACTIVE_SELECTION_BACKGROUND_KEY = "List.selectionInactiveBackground";
	private static final String INACTIVE_SELECTION_FOREGROUND_KEY = "List.selectionInactiveForeground";

	private Color selectionBackground;
	private Color selectionForeground;
	private Color selectionInactiveBackground;
	private Color selectionInactiveForeground;

	/**
	 * Creates a new UI delegate for the specified component.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelListUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		selectionBackground = UIManager.getColor(SELECTION_BACKGROUND_KEY);
		selectionForeground = UIManager.getColor(SELECTION_FOREGROUND_KEY);
		selectionInactiveBackground = UIManager.getColor(INACTIVE_SELECTION_BACKGROUND_KEY);
		selectionInactiveForeground = UIManager.getColor(INACTIVE_SELECTION_FOREGROUND_KEY);

		toggleSelectionColors(list.hasFocus());
	}

	@Override
	protected void uninstallDefaults() {
		super.uninstallDefaults();

		selectionBackground = null;
		selectionForeground = null;
		selectionInactiveBackground = null;
		selectionInactiveForeground = null;
	}

	@Override
	protected FocusListener createFocusListener() {
		return new FocusHandler() {
			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);
				toggleSelectionColors(true);
			}

			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				toggleSelectionColors(false);
			}
		};
	}

	private void toggleSelectionColors(boolean focused) {
		if (focused) {
			updateSelectionColors(selectionBackground, selectionForeground);
		} else {
			updateSelectionColors(selectionInactiveBackground, selectionInactiveForeground);
		}
	}

	private void updateSelectionColors(Color background, Color foreground) {
		if (list != null) {
			if (list.getSelectionBackground() != background) {
				list.setSelectionBackground(background);
			}
			if (list.getSelectionForeground() != foreground) {
				list.setSelectionForeground(foreground);
			}
		}
	}
}
