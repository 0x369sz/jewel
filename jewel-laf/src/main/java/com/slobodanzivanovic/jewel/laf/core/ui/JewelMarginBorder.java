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

import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * A custom margin border that applies UI scaling to the insets.
 */
public class JewelMarginBorder extends BasicBorders.MarginBorder {

	@Override
	public Insets getBorderInsets(Component component, Insets insets) {
		Objects.requireNonNull(component, "Component cannot be null");
		Objects.requireNonNull(insets, "Insets cannot be null");

		insets = super.getBorderInsets(component, insets);

		// Scale all inset values
		insets.top = scale(insets.top);
		insets.left = scale(insets.left);
		insets.bottom = scale(insets.bottom);
		insets.right = scale(insets.right);

		return insets;
	}
}
