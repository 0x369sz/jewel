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
 * UI delegate for JFormattedTextField components in the Jewel Look and Feel.
 * Extends the base text field UI implementation while providing specific handling
 * for formatted text fields.
 */
public final class JewelFormattedTextFieldUI extends JewelTextFieldUI {

	/**
	 * Creates a new UI delegate for the specified component.
	 *
	 * @param component the component that will use this delegate
	 * @return a new UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelFormattedTextFieldUI();
	}

	@Override
	protected String getPropertyPrefix() {
		return "FormattedTextField";
	}
}
