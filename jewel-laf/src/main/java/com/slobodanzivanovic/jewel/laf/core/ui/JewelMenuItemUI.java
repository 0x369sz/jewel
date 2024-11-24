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
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * UI delegate for JMenuItem components in the Jewel Look and Feel.
 * Provides UI scaling support for the text-icon gap.
 */
public class JewelMenuItemUI extends BasicMenuItemUI {
	private static final String ICON_TEXT_GAP_PROPERTY = "iconTextGap";

	/**
	 * Creates a new UI delegate for the specified component.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelMenuItemUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		defaultTextIconGap = scale(defaultTextIconGap);
	}

	@Override
	protected PropertyChangeListener createPropertyChangeListener(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		PropertyChangeListener superListener = super.createPropertyChangeListener(component);

		return propertyEvent -> {
			superListener.propertyChange(propertyEvent);
			if (Objects.equals(propertyEvent.getPropertyName(), ICON_TEXT_GAP_PROPERTY)) {
				defaultTextIconGap = scale(defaultTextIconGap);
			}
		};
	}
}
