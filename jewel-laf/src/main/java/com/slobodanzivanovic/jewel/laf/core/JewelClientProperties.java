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

package com.slobodanzivanovic.jewel.laf.core;

import javax.swing.*;
import java.util.Objects;

/**
 * Defines client properties for customizing Jewel Look and Feel components.
 * These properties provide a way to modify the appearance and behavior of
 * components without subclassing.
 *
 * <p>Client properties are used to:
 * <ul>
 *   <li>Customize component appearance</li>
 *   <li>Modify component behavior</li>
 *   <li>Support special component variants</li>
 * </ul>
 */
public interface JewelClientProperties {
	/**
	 * Property key for specifying button type.
	 * Used to customize button appearance and behavior.
	 */
	String BUTTON_TYPE = "JButton.buttonType";

	/**
	 * Value for help button type.
	 * When this value is set as the BUTTON_TYPE property,
	 * the button will be rendered as a help button.
	 */
	String BUTTON_TYPE_HELP = "help";

	/**
	 * Checks if a component's client property matches the expected value.
	 * Provides a null-safe way to compare client properties.
	 *
	 * @param c     the component to check
	 * @param key   the client property key
	 * @param value the expected value
	 * @return true if the property value matches the expected value
	 * @throws NullPointerException if the component is null
	 */
	static boolean clientPropertyEquals(JComponent c, String key, Object value) {
		return Objects.equals(c.getClientProperty(key), value);
	}
}
