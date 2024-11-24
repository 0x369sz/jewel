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

/**
 * Jewel Look and Feel UI delegate for JCheckBox.
 */
public class JewelCheckBoxUI extends JewelRadioButtonUI {

	private static final String PROPERTY_PREFIX = "CheckBox.";
	private static volatile ComponentUI instance;

	/**
	 * Creates or returns the singleton UI delegate for JCheckBox components.
	 *
	 * @param c the component for which to create the UI
	 * @return the singleton UI delegate instance
	 */
	public static ComponentUI createUI(JComponent c) {
		ComponentUI result = instance;
		if (result == null) {
			synchronized (JewelCheckBoxUI.class) {
				result = instance;
				if (result == null) {
					instance = result = new JewelCheckBoxUI();
				}
			}
		}
		return result;
	}

	@Override
	public String getPropertyPrefix() {
		return PROPERTY_PREFIX;
	}

	private JewelCheckBoxUI() {
		// enforce singleton pattern
	}
}
