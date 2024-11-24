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
import java.awt.*;
import java.util.Objects;

/**
 * UI delegate for toggle button components in the Jewel Look and Feel.
 * Extends the basic button UI with support for selected states and
 * toolbar-specific styling.
 */
public class JewelToggleButtonUI extends JewelButtonUI {

	private static final String KEY_SELECTED_BACKGROUND = "ToggleButton.selectedBackground";
	private static final String KEY_SELECTED_FOREGROUND = "ToggleButton.selectedForeground";
	private static final String KEY_DISABLED_SELECTED_BACKGROUND = "ToggleButton.disabledSelectedBackground";
	private static final String KEY_TOOLBAR_SELECTED_BACKGROUND = "ToggleButton.toolbar.selectedBackground";

	protected Color selectedBackground;
	protected Color selectedForeground;
	protected Color disabledSelectedBackground;
	protected Color toolbarSelectedBackground;

	private boolean defaultsInitialized;
	private static ComponentUI instance;

	/**
	 * Creates or returns the singleton UI delegate for toggle button components.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		if (instance == null) {
			instance = new JewelToggleButtonUI();
		}
		return instance;
	}

	@Override
	protected String getPropertyPrefix() {
		return "ToggleButton.";
	}

	@Override
	protected void installDefaults(AbstractButton button) {
		Objects.requireNonNull(button, "Button cannot be null");
		super.installDefaults(button);

		if (!defaultsInitialized) {
			selectedBackground = UIManager.getColor(KEY_SELECTED_BACKGROUND);
			selectedForeground = UIManager.getColor(KEY_SELECTED_FOREGROUND);
			disabledSelectedBackground = UIManager.getColor(KEY_DISABLED_SELECTED_BACKGROUND);
			toolbarSelectedBackground = UIManager.getColor(KEY_TOOLBAR_SELECTED_BACKGROUND);
			defaultsInitialized = true;
		}
	}

	@Override
	protected void uninstallDefaults(AbstractButton button) {
		Objects.requireNonNull(button, "Button cannot be null");
		super.uninstallDefaults(button);
		defaultsInitialized = false;
	}

	@Override
	protected Color getBackground(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		ButtonModel model = ((AbstractButton) component).getModel();

		if (model.isSelected()) {
			boolean toolBarButton = isToolBarButton(component);
			Color selectedBg = toolBarButton ? toolbarSelectedBackground : selectedBackground;
			Color disabledBg = toolBarButton ? toolbarSelectedBackground : disabledSelectedBackground;
			Color pressedBg = toolBarButton ? toolbarPressedBackground : pressedBackground;

			return buttonStateColor(component, selectedBg, disabledBg, null, null, pressedBg);
		}

		return super.getBackground(component);
	}

	@Override
	protected Color getForeground(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		ButtonModel model = ((AbstractButton) component).getModel();

		if (model.isSelected() && !isToolBarButton(component)) {
			return selectedForeground;
		}

		return super.getForeground(component);
	}
}
