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
import java.util.Objects;

/**
 * UI delegate for popup menu separators in the Jewel Look and Feel.
 * Provides custom separator styling within popup menus.
 */
public class JewelPopupMenuSeparatorUI extends JewelSeparatorUI {

	private static final String PROPERTY_PREFIX = "PopupMenuSeparator";
	private static ComponentUI instance;

	/**
	 * Creates or returns the shared UI delegate.
	 *
	 * @param component the component for which to create the UI
	 * @return the shared UI delegate instance
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component must not be null");
		if (instance == null) {
			instance = new JewelPopupMenuSeparatorUI();
		}
		return instance;
	}

	@Override
	protected String getPropertyPrefix() {
		return PROPERTY_PREFIX;
	}
}
