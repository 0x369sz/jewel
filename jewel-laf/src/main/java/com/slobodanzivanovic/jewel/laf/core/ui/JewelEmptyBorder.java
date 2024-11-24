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

import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * A UI-specific empty border that supports UI scaling.
 * This border scales its insets based on the current UI scale factor.
 */
public class JewelEmptyBorder extends BorderUIResource.EmptyBorderUIResource {

	/**
	 * Creates a JewelEmptyBorder with no insets (0, 0, 0, 0).
	 */
	public JewelEmptyBorder() {
		super(0, 0, 0, 0);
	}

	/**
	 * Creates a JewelEmptyBorder with the specified insets.
	 *
	 * @param top    the top inset
	 * @param left   the left inset
	 * @param bottom the bottom inset
	 * @param right  the right inset
	 */
	public JewelEmptyBorder(int top, int left, int bottom, int right) {
		super(top, left, bottom, right);
	}

	/**
	 * Creates a JewelEmptyBorder with insets copied from another Insets object.
	 *
	 * @param insets the Insets object to copy from
	 * @throws NullPointerException if insets is null
	 */
	public JewelEmptyBorder(Insets insets) {
		super(Objects.requireNonNull(insets, "insets cannot be null"));
	}

	/**
	 * Returns scaled border insets.
	 *
	 * @return new Insets object with scaled values
	 */
	@Override
	public Insets getBorderInsets() {
		return new Insets(
			scale(top),
			scale(left),
			scale(bottom),
			scale(right)
		);
	}

	/**
	 * Updates the provided Insets object with scaled border insets.
	 *
	 * @param c      the component for which insets are being determined (unused)
	 * @param insets the Insets object to be updated with scaled values
	 * @return the updated Insets object
	 * @throws NullPointerException if insets is null
	 */
	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		Objects.requireNonNull(insets, "insets cannot be null");

		insets.top = scale(top);
		insets.left = scale(left);
		insets.bottom = scale(bottom);
		insets.right = scale(right);

		return insets;
	}
}
