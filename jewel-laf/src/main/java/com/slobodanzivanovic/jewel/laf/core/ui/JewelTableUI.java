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
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Objects;

/**
 * UI delegate for table components in the Jewel Look and Feel.
 * Provides custom selection colors and row height scaling, with
 * support for focused and unfocused states.
 */
public class JewelTableUI extends BasicTableUI {

	private static final String KEY_SELECTION_BACKGROUND = "Table.selectionBackground";
	private static final String KEY_SELECTION_FOREGROUND = "Table.selectionForeground";
	private static final String KEY_SELECTION_INACTIVE_BACKGROUND = "Table.selectionInactiveBackground";
	private static final String KEY_SELECTION_INACTIVE_FOREGROUND = "Table.selectionInactiveForeground";
	private static final String KEY_ROW_HEIGHT = "Table.rowHeight";

	protected Color selectionBackground;
	protected Color selectionForeground;
	protected Color selectionInactiveBackground;
	protected Color selectionInactiveForeground;

	/**
	 * Creates or returns the UI delegate for table components.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelTableUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		selectionBackground = UIManager.getColor(KEY_SELECTION_BACKGROUND);
		selectionForeground = UIManager.getColor(KEY_SELECTION_FOREGROUND);
		selectionInactiveBackground = UIManager.getColor(KEY_SELECTION_INACTIVE_BACKGROUND);
		selectionInactiveForeground = UIManager.getColor(KEY_SELECTION_INACTIVE_FOREGROUND);

		toggleSelectionColors(table.hasFocus());

		int rowHeight = JewelUIUtils.getUIInt(KEY_ROW_HEIGHT, 16);
		if (rowHeight > 0) {
			LookAndFeel.installProperty(table, "rowHeight", UIScale.scale(rowHeight));
		}
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
			if (table.getSelectionBackground() == selectionInactiveBackground) {
				table.setSelectionBackground(selectionBackground);
			}
			if (table.getSelectionForeground() == selectionInactiveForeground) {
				table.setSelectionForeground(selectionForeground);
			}
		} else {
			if (table.getSelectionBackground() == selectionBackground) {
				table.setSelectionBackground(selectionInactiveBackground);
			}
			if (table.getSelectionForeground() == selectionForeground) {
				table.setSelectionForeground(selectionInactiveForeground);
			}
		}
	}
}
