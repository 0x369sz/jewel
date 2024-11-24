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
import javax.swing.plaf.basic.BasicRadioButtonMenuItemUI;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * UI delegate for radio button menu items in the Jewel Look and Feel.
 * Handles custom scaling for text-icon gap and property change events.
 */
public class JewelRadioButtonMenuItemUI extends BasicRadioButtonMenuItemUI {

	private static final String ICON_TEXT_GAP_PROPERTY = "iconTextGap";

	/**
	 * Creates a new UI delegate for the specified component.
	 *
	 * @param component the component to create the UI delegate for
	 * @return the UI delegate for the component
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component must not be null");
		return new JewelRadioButtonMenuItemUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		defaultTextIconGap = scale(defaultTextIconGap);
	}

	@Override
	protected PropertyChangeListener createPropertyChangeListener(JComponent component) {
		Objects.requireNonNull(component, "Component must not be null");
		PropertyChangeListener superListener = super.createPropertyChangeListener(component);

		return propertyEvent -> {
			superListener.propertyChange(propertyEvent);
			if (Objects.equals(propertyEvent.getPropertyName(), ICON_TEXT_GAP_PROPERTY)) {
				defaultTextIconGap = scale(defaultTextIconGap);
			}
		};
	}
}
