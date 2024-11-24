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
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * Jewel Look and Feel implementation of CheckBoxMenuItem UI.
 * Provides scaled text-icon gap and handles dynamic updates to the gap.
 */
public class JewelCheckBoxMenuItemUI extends BasicCheckBoxMenuItemUI {

	private static final String ICON_TEXT_GAP_PROPERTY = "iconTextGap";

	/**
	 * Creates a new UI delegate for JCheckBoxMenuItem.
	 *
	 * @param c the component to create UI for
	 * @return new instance of JewelCheckBoxMenuItemUI
	 */
	public static ComponentUI createUI(JComponent c) {
		return new JewelCheckBoxMenuItemUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		updateScaledTextIconGap();
	}

	@Override
	protected PropertyChangeListener createPropertyChangeListener(JComponent c) {
		PropertyChangeListener superListener = super.createPropertyChangeListener(c);
		return event -> handlePropertyChange(superListener, event);
	}

	private void handlePropertyChange(PropertyChangeListener superListener, PropertyChangeEvent event) {
		superListener.propertyChange(event);
		if (ICON_TEXT_GAP_PROPERTY.equals(event.getPropertyName())) {
			updateScaledTextIconGap();
		}
	}

	private void updateScaledTextIconGap() {
		defaultTextIconGap = scale(defaultTextIconGap);
	}
}
