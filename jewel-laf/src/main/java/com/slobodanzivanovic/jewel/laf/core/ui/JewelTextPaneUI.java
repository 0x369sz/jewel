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
import javax.swing.plaf.basic.BasicTextPaneUI;
import java.awt.*;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * UI delegate for text pane components in the Jewel Look and Feel.
 * Provides custom minimum width support while maintaining basic text pane functionality.
 */
public class JewelTextPaneUI extends BasicTextPaneUI {

	private static final String KEY_MINIMUM_WIDTH = "Component.minimumWidth";

	protected int minimumWidth;

	/**
	 * Creates or returns the UI delegate for text pane components.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelTextPaneUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		minimumWidth = UIManager.getInt(KEY_MINIMUM_WIDTH);
	}

	@Override
	public Dimension getPreferredSize(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return applyMinimumWidth(super.getPreferredSize(component));
	}

	@Override
	public Dimension getMinimumSize(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return applyMinimumWidth(super.getMinimumSize(component));
	}

	private Dimension applyMinimumWidth(Dimension size) {
		size.width = Math.max(size.width, scale(minimumWidth) - (scale(1) * 2));
		return size;
	}
}
